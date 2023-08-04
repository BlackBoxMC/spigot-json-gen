package net.ioixd.spigotjsongen;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ClassParserFuture implements Runnable {
    private Thread t;
    private int n;
    private Class cls;
    private ArrayList<ParsedClass> classes;
    private String doclink;
    private WebScraper webScraper;
    private CountDownLatch latch;

    ClassParserFuture(int n, Class<?> cls, ArrayList<ParsedClass> classes, String doclink, WebScraper webScraper,
            CountDownLatch latch) {
        this.n = n;
        this.cls = cls;
        this.classes = classes;
        this.doclink = doclink;
        this.webScraper = webScraper;
        this.latch = latch;
    }

    public void run() {
        if ((cls.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC) {
            return;
        }
        classes.add(new ParsedClass(cls, doclink, cls.getPackageName(), webScraper));
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, String.valueOf(n));
            t.start();
        }
    }
}