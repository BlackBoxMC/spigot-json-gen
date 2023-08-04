use std::{
    error::Error,
    fs::File,
    io::{Read, Write},
    path::Path,
};

use eyre::eyre;
use jni::{
    objects::{JClass, JObject, JObjectArray, JString},
    JNIEnv,
};
use lazy_static::lazy_static;
use phf::phf_map;
use regex::Regex;
use scraper::{element_ref::Select, html::Html, ElementRef, Selector};
use tokio::runtime::Runtime;

lazy_static! {
    pub static ref NON_ALPHABET: Regex = Regex::new("[^a-zA-Z\\d\\s:]").unwrap();
    pub static ref PATTERN_GENERICS: Regex =
        Regex::new("(<|&lt;)([A-Za-z,\\s]*?)(>|&gt;)").unwrap();
    pub static ref METHOD_PATTERN_GENERICS: Regex =
        Regex::new("(<)([A-Za-z,\\s]*?)(>\\s)").unwrap();
    pub static ref RUNTIME: Runtime = tokio::runtime::Builder::new_multi_thread()
        .enable_all()
        .build()
        .unwrap();
}

static DOC_LINKS: phf::Map<&'static str, &'static str> = phf_map! {
    "org.bukkit" => "https://hub.spigotmc.org/javadocs/spigot/",
    "net.md_5" => "https://javadoc.io/doc/net.md-5/bungeecord-api/latest/",
    "java.util" => "https://docs.oracle.com/javase/8/docs/api/",
    "java.lang" => "https://docs.oracle.com/javase/8/docs/api/",
    "java.io" => "https://docs.oracle.com/javase/8/docs/api/",
    "java.security" => "https://docs.oracle.com/javase/8/docs/api/",
};

pub fn element_get_class(el: ElementRef, class_name: impl Into<String>) -> Option<ElementRef> {
    let sel = Selector::parse(&class_name.into()).unwrap();
    let mut ele = el.select(&sel);
    if let Some(e) = ele.next() {
        return Some(e);
    } else {
        return None;
    }
}

pub fn element_get_text(el: ElementRef) -> String {
    el.text().collect::<Vec<&str>>().join("")
}

pub fn get_url<'a>(
    module_name: String,
    env: &mut JNIEnv<'a>,
    cls: JClass<'a>,
) -> Result<Option<Html>, Box<dyn Error>> {
    match DOC_LINKS.get(&module_name) {
        Some(doclink) => {
            let class_name_obj = env.call_method(cls, "getName", "()Ljava/lang/String;", &[])?;
            let class_name = env
                .get_string(unsafe { &jni::objects::JString::from_raw(class_name_obj.as_jni().l) })?
                .to_string_lossy()
                .to_string();
            let url = format!("{}{}.html", doclink, class_name.replace(".", "/"));
            let url_filename = NON_ALPHABET.replace_all(&url, "").to_string();
            let url_path = Path::join(Path::new(".javadocCache"), format!("{}.txt", url_filename));
            match File::open(&url_path) {
                Ok(mut file) => {
                    let mut buf = String::new();
                    file.read_to_string(&mut buf)?;
                    Ok(Some(Html::parse_document(&buf)))
                }
                Err(err) => {
                    if let std::io::ErrorKind::NotFound = err.kind() {
                        let req = reqwest::blocking::get(url)?;
                        let mut f = File::create(url_path)?;
                        let text = &req.text()?;
                        f.write(text.as_bytes())?;
                        Ok(Some(Html::parse_document(text)))
                    } else {
                        Err(Box::new(err))
                    }
                }
            }
        }
        None => return Ok(None),
    }
}

pub fn get_generics<'a>(
    env: &mut JNIEnv<'a>,
    cls: JClass<'a>,
    module_name: String,
    _method_name: Option<String>,
) -> Result<Vec<String>, Box<dyn Error>> {
    let doc = get_url(module_name, env, cls).unwrap();
    if let None = doc {
        return Ok(Vec::new());
    };
    let doc = doc.unwrap();
    let mut names = Vec::new();
    let sel = Selector::parse(".title").unwrap();
    let mut ele = doc.select(&sel);
    if let Some(title) = ele.next() {
        if let Some(cap) = PATTERN_GENERICS.captures(&element_get_text(title)) {
            let fuck = cap
                .get(2)
                .unwrap()
                .as_str()
                .replace("<", "")
                .replace(">", "");
            let mut ok = fuck
                .split(",")
                .map(|f| f.to_string())
                .collect::<Vec<String>>();
            names.append(&mut ok);
        }
    }
    Ok(names)
}

pub fn get_method_generics<'a>(
    env: &mut JNIEnv<'a>,
    cls: JClass<'a>,
    module_name: String,
    method_name: Option<String>,
) -> Result<Vec<String>, Box<dyn Error>> {
    let method_name = method_name.unwrap();

    let doc = get_url(module_name, env, cls).unwrap();
    if let None = doc {
        return Ok(Vec::new());
    };
    let doc = doc.unwrap();

    let rows_class = Selector::parse("tr").unwrap();
    let code_class = Selector::parse("code").unwrap();
    let mut rows = doc.select(&rows_class);
    let mut names = Vec::new();
    while let Some(row) = rows.next() {
        let col_first = element_get_class(row, ".colFirst");
        let col_last = element_get_class(row, ".colLast");
        if let Some(first) = col_first {
            if let Some(last) = col_last {
                if let Some(member_name_link) = element_get_class(last, ".memberNameLink") {
                    if !element_get_text(member_name_link).contains(&method_name) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            let code = first.select(&code_class).collect::<Vec<ElementRef>>();
            if let Some(c) = code.get(0) {
                if !&c.inner_html().contains("<") {
                    continue;
                }
                if let Some(cap) = METHOD_PATTERN_GENERICS.captures(&element_get_text(*c)) {
                    let fuck = cap
                        .get(2)
                        .unwrap()
                        .as_str()
                        .replace("<", "")
                        .replace(">", "");
                    let mut ok = fuck
                        .split(",")
                        .map(|f| f.to_string())
                        .collect::<Vec<String>>();
                    names.append(&mut ok);
                }
            }
        }
    }
    Ok(names)
}

pub fn call_method_return_java_array<'a>(
    mut env: JNIEnv<'a>,
    cls: JClass<'a>,
    module_name: String,
    method_name: Option<String>,
    f: impl Fn(
        &mut JNIEnv<'a>,
        JClass<'a>,
        String,
        Option<String>,
    ) -> Result<Vec<String>, Box<dyn Error>>,
) -> JObjectArray<'a> {
    let str_cls = env.find_class("java/lang/String").unwrap();
    let mut names = f(&mut env, cls, module_name, method_name).unwrap();

    let arr = env
        .new_object_array(names.len() as i32, str_cls, env.new_string("").unwrap())
        .unwrap();
    let mut n = 0;
    for name in names {
        env.set_object_array_element(&arr, n, env.new_string(name).unwrap())
            .unwrap();
        n += 1;
    }
    arr
}

#[no_mangle]
pub extern "system" fn Java_net_ioixd_spigotjsongen_WebScraper_getGenerics<'a>(
    mut env: JNIEnv<'a>,
    this: JObject,
    module_name_raw: JString<'a>,
    cls: JClass<'a>,
) -> JObjectArray<'a> {
    let module_name = env
        .get_string(&module_name_raw)
        .unwrap()
        .to_string_lossy()
        .to_string();

    call_method_return_java_array(env, cls, module_name, None, get_generics)
}

#[no_mangle]
pub extern "system" fn Java_net_ioixd_spigotjsongen_WebScraper_getMethodGenerics<'a>(
    mut env: JNIEnv<'a>,
    this: JObject,
    module_name_raw: JString<'a>,
    cls: JClass<'a>,
    method_name_raw: JString<'a>,
) -> JObjectArray<'a> {
    let module_name = env
        .get_string(&module_name_raw)
        .unwrap()
        .to_string_lossy()
        .to_string();
    let method_name = env
        .get_string(&method_name_raw)
        .unwrap()
        .to_string_lossy()
        .to_string();

    call_method_return_java_array(
        env,
        cls,
        module_name,
        Some(method_name),
        get_method_generics,
    )
}
