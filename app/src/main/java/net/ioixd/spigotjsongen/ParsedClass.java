package net.ioixd.spigotjsongen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedClass {
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

    ParsedClass(Class<?> cls) {
        this.packageName = cls.getPackageName();
        this.name = cls.getName().replace(this.packageName + ".", "");
        this.isInterface = cls.isInterface();

        if (cls.getSuperclass() != null) {
            this.superClass = new ParsedClass(cls.getSuperclass());
        }

        if (cls.getDeclaredClasses().length >= 1) {
            this.classes = new ArrayList<ParsedClass>();
            for (Class<?> cl : cls.getDeclaredClasses()) {
                if (cl.getPackageName().startsWith("java")) {
                    // hell naw
                    continue;
                }
                this.classes.add(new ParsedClass(cl));
            }
        }
        if (cls.getConstructors().length >= 1) {
            this.constructors = new ArrayList<ParsedConstructor>();
            for (Constructor<?> c : cls.getConstructors()) {
                this.constructors.add(new ParsedConstructor(c));
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
                this.methods.add(new ParsedMethod(m));
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
                this.interfaces.add(new ParsedClass(i));
            }
        }

        if (cls.isEnum()) {
            this.enums = new ArrayList<Object>();
            for (Object o : cls.getEnumConstants()) {
                this.enums.add(o);
            }
        }

        this.modifiers = cls.getModifiers();
    }
}