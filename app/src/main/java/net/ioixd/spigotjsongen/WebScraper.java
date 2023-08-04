package net.ioixd.spigotjsongen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebScraper {
    public static HashMap<String, String> docLinks = new HashMap<>() {
        {
            this.put("org.bukkit", "https://hub.spigotmc.org/javadocs/spigot/");
            this.put("net.md_5", "https://javadoc.io/doc/net.md-5/bungeecord-api/latest/");
            this.put("java.util", "https://docs.oracle.com/javase/8/docs/api/");
            this.put("java.lang", "https://docs.oracle.com/javase/8/docs/api/");
            this.put("java.io", "https://docs.oracle.com/javase/8/docs/api/");
            this.put("java.security", "https://docs.oracle.com/javase/8/docs/api/");
        }
    };

    Pattern patternGenerics = Pattern.compile("(<|&lt;)([A-Za-z,\s]*?)(>|&gt;)");
    Pattern methodPatternGenerics = Pattern.compile("(<)([A-Za-z,\s]*?)(>\s)");
    Pattern nonAlphabet = Pattern.compile("[^a-zA-Z\\d\\s:]");

    WebScraper() {
        File file = new File(".javadocCache");
        file.mkdir();
        System.loadLibrary("native_web_scraper");
    }

    public native String[] getGenerics(String moduleName, Class<?> cls);

    public native String[] getMethodGenerics(String moduleName, Class<?> cls, String methodName) throws Exception;

}
