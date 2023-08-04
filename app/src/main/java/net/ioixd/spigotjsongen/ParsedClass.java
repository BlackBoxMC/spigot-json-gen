package net.ioixd.spigotjsongen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedClass {
    public String[] generics;
    public String name;
    public String packageName;

    public ArrayList<ParsedClass> classes;
    public ParsedClass superClass;
    public ArrayList<ParsedConstructor> constructors;
    public HashMap<String, String> fields;
    public ArrayList<ParsedMethod> methods;
    public ArrayList<ParsedClass> interfaces;

    // public @Nullable ArrayList<Annotation> annotations;
    public ArrayList<Object> enums;

    public int modifiers;

    public boolean isEnum = false;
    public boolean isInterface = false;

    public HashMap<String, ArrayList<String>> paramAnnotations = new HashMap<>();
    public ArrayList<String> returnAnnotations = new ArrayList<String>();
    public ArrayList<String> annotations = new ArrayList<String>();

    ParsedClass(Class<?> cls, String doclink, String packageName, WebScraper webScraper) {
        this.packageName = cls.getPackageName();
        this.name = cls.getName().replace(this.packageName + ".", "");
        this.isInterface = cls.isInterface();

        if (cls.getSuperclass() != null) {
            this.superClass = new ParsedClass(cls.getSuperclass(), doclink, packageName, webScraper);
        }

        if (cls.getDeclaredClasses().length >= 1) {
            this.classes = new ArrayList<ParsedClass>();
            for (Class<?> cl : cls.getDeclaredClasses()) {
                if (cl.getPackageName().startsWith("java")) {
                    // hell naw
                    continue;
                }
                this.classes.add(new ParsedClass(cl, doclink, packageName, webScraper));
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
                this.interfaces.add(new ParsedClass(i, doclink, packageName, webScraper));
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
        if (cls.getMethods().length >= 1) {
            this.methods = new ArrayList<ParsedMethod>();
            for (Method m : cls.getMethods()) {
                String[] parts = cls.getPackageName().split("\\.");
                String[] fuckyou = new String[] {
                        parts[0],
                        parts[1]
                };
                this.methods.add(new ParsedMethod(m, cls, String.join(".", fuckyou), webScraper));
            }
        }

        if (cls.isEnum()) {
            this.enums = new ArrayList<Object>();
            for (Object o : cls.getEnumConstants()) {
                this.enums.add(o);
            }
        }

        this.modifiers = cls.getModifiers();

        // Ok time for the information that Java just doesn't fucking give us because
        // fuck you that's why
        if (cls.getPackageName() != packageName) {
            return;
        }
        String[] parts = cls.getPackageName().split("\\.");
        String[] fuckyou = new String[] {
                parts[0],
                parts[1]
        };
        // Generics
        /*
         * var generics = new ArrayList<String>();
         * var typeParams = cls.getTypeParameters();
         * for (var parm : typeParams) {
         * var bounds = parm.getBounds();
         * for (var bound : bounds) {
         * generics.add(bound.toString());
         * }
         * }
         * this.generics = (String[]) generics.toArray();
         */

        this.generics = webScraper.getGenerics(String.join(".", fuckyou), cls);
    }
}