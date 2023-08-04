package net.ioixd.spigotjsongen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class MethodParserFuture implements Runnable {
    private Thread t;
    private int n;
    private ArrayList<ParsedMethod> methods;
    private Class<?> cls;
    private Method m;
    private String[] fuckyou;
    private WebScraper webScraper;
    private CountDownLatch latch;

    MethodParserFuture(int n, ArrayList<ParsedMethod> methods, Class<?> cls, Method m, String[] fuckyou,
            WebScraper webScraper, CountDownLatch latch) {
        this.n = n;
        this.methods = methods;
        this.cls = cls;
        this.m = m;
        this.fuckyou = fuckyou;
        this.webScraper = webScraper;
        this.latch = latch;
    }

    public void run() {
        this.methods.add(new ParsedMethod(m, cls, String.join(".", fuckyou), webScraper));
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, String.valueOf(n));
            t.start();
        }
    }
}
