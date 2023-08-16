package net.ioixd.spigotjsongen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedClass {
    public String[] generics;
    public String name;
    public String packageName;

    public ArrayList<ParsedClass> classes;
    public ArrayList<ParsedEnum> enums;

    public ParsedClass superClass;
    public ArrayList<ParsedConstructor> constructors;
    public HashMap<String, String> fields;
    public ArrayList<ParsedMethod> methods;
    public ArrayList<ParsedClass> interfaces;

    // public @Nullable ArrayList<Annotation> annotations;

    public int modifiers;

    public boolean isEnum = false;
    public boolean isInterface = false;

    public HashMap<String, ArrayList<String>> paramAnnotations = new HashMap<>();
    public ArrayList<String> returnAnnotations = new ArrayList<String>();
    public ArrayList<String> annotations = new ArrayList<String>();

    public String comment;

    public ArrayList<String> values = new ArrayList<String>();
    public int ordinal;

    ParsedClass(Class<?> cls, String doclink, String packageName, WebScraper webScraper, boolean toplevel) {
        this.packageName = cls.getPackageName();
        this.name = cls.getName().replace(this.packageName + ".", "");
        this.isInterface = cls.isInterface();

        if (cls.getSuperclass() != null) {
            this.superClass = new ParsedClass(cls.getSuperclass(), doclink, packageName, webScraper, false);
        }

        if (cls.getDeclaredClasses().length >= 1) {
            this.classes = new ArrayList<ParsedClass>();
            for (Class<?> cl : cls.getDeclaredClasses()) {
                if (cl.getPackageName().startsWith("java")) {
                    // hell naw
                    continue;
                }
                this.classes.add(new ParsedClass(cl, doclink, packageName, webScraper, false));
            }
        }
        if (cls.getInterfaces().length >= 1) {
            this.interfaces = new ArrayList<ParsedClass>();
            for (Class<?> i : cls.getInterfaces()) {
                if (cls.getDeclaringClass() != null) {
                    if (cls.getDeclaringClass().getName() == i.getName()) {
                        continue;
                    }
                }
                this.interfaces.add(new ParsedClass(i, doclink, packageName, webScraper, false));
            }
        }
        if (cls.getConstructors().length >= 1) {
            this.constructors = new ArrayList<ParsedConstructor>();
            for (Constructor<?> c : cls.getConstructors()) {
                this.constructors.add(new ParsedConstructor(c, doclink));
            }
        }
        if (cls.getDeclaredFields().length >= 1) {
            this.fields = new HashMap<>();
            for (Field f : cls.getDeclaredFields()) {
                this.fields.put(f.getName(), f.getType().getName());
            }
        }

        String[] parts = cls.getPackageName().split("\\.");
        String[] fuckyou = new String[] {
                parts[0],
                parts[1]
        };

        /*
         * if (cls.getPackageName() == packageName) {
         * this.generics = webScraper.getGenerics(String.join(".", fuckyou), cls);
         * } else {
         * this.generics = new String[] {};
         * }
         */

        Method[] methods;

        if (cls.isEnum()) {
            methods = cls.getDeclaredMethods();
        } else {
            methods = cls.getMethods();
        }

        if (methods.length >= 1) {
            this.methods = new ArrayList<ParsedMethod>();
            for (Method m : methods) {
                this.methods
                        .add(new ParsedMethod(m, cls, String.join(".", fuckyou), this.generics, webScraper, toplevel));
            }
        }

        this.isEnum = cls.isEnum();
        if (this.isEnum) {
            try {
                Method valueOf = cls.getDeclaredMethod("valueOf", String.class);
                String value = cls.getEnumConstants()[0].toString();
                if (value.toUpperCase() != value) {
                    value = value.replaceAll("([A-Z])", "_$1").toUpperCase();
                }
                Enum<?> en = (Enum<?>) valueOf.invoke(null, value);
                var ok = new ParsedEnum(en, doclink, packageName, webScraper);
                this.values = ok.values;
                this.ordinal = ok.ordinal;
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException
                    | InvocationTargetException e) {
                e.printStackTrace();
                this.isEnum = false;
                this.values = null;
                this.ordinal = 0;
            }
        }

        this.modifiers = cls.getModifiers();

        if (toplevel) {
            this.comment = webScraper.getComment(String.join(".", fuckyou), cls);
        }

    }
}