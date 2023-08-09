package net.ioixd.spigotjsongen;

// We moved all of this to a native Rust library because the Java version was taking too damn long !!

public class WebScraper {
    WebScraper() {
        System.loadLibrary("native_web_scraper");
    }

    public native String[] getGenerics(String moduleName, Class<?> cls);

    public native String[] getMethodGenerics(String moduleName, Class<?> cls, String methodName) throws Exception;

    public native String getComment(String moduleName, Class<?> cls);

    public native String getMethodComment(String moduleName, Class<?> cls, String methodName);
}
