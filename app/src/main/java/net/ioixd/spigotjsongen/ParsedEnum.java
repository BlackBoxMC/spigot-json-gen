package net.ioixd.spigotjsongen;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedEnum {
    public String name;
    public String packageName;
    public ArrayList<String> values = new ArrayList<String>();
    public int ordinal;
    public ArrayList<ParsedClass> classes;
    public ArrayList<ParsedMethod> methods;
    public HashMap<String, ArrayList<String[]>> annotations = new HashMap<>();

    public boolean isEnum = true;

    ParsedEnum(Enum<?> e, String doclink, String packageName, WebScraper webScraper) {
        this.name = e.getClass().getSimpleName();
        this.packageName = e.getClass().getPackageName();

        Enum<?>[] constants = e.getClass().getEnumConstants();
        if (constants != null) {
            for (Enum<?> en : constants) {
                try {
                    Annotation[] annotations = en.getDeclaringClass().getField(en.name()).getAnnotations();
                    for (Annotation annotation : annotations) {
                        for (Method method_ : annotation.annotationType().getDeclaredMethods()) {
                            Object value;
                            try {
                                value = method_.invoke(annotation, (Object[]) null);
                                if (!this.annotations.containsKey(en.name())) {
                                    this.annotations.put(en.name(), new ArrayList<String[]>());
                                }
                                this.annotations.get(en.name())
                                        .add(new String[] { method_.getName(), value.toString() });
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException f) {
                                f.printStackTrace();
                            }
                        }
                    }
                } catch (NoSuchFieldException | SecurityException e1) {
                    e1.printStackTrace();
                }
                values.add(en.name());
            }
            ;
        }
        this.ordinal = e.ordinal();

        if (e.getClass().getDeclaredMethods().length >= 1) {
            this.methods = new ArrayList<ParsedMethod>();
            for (Method m : e.getClass().getDeclaredMethods()) {
                this.methods.add(new ParsedMethod(m, doclink));
            }
        }

        if (e.getClass().getClasses().length >= 1) {
            this.classes = new ArrayList<ParsedClass>();
            for (Class<?> cl : e.getClass().getClasses()) {
                if (cl.getPackageName().startsWith("java")) {
                    // hell naw
                    continue;
                }
                if ((cl.getModifiers() & Modifier.PUBLIC) != Modifier.PUBLIC) {
                    continue;
                }
                this.classes.add(new ParsedClass(cl, doclink, packageName, webScraper));
            }
        }
    }
}
