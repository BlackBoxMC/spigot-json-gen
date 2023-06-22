package net.ioixd.spigotjsongen;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ParsedClass {
    public String name;
    public String packageName;


    public ArrayList<String> classes;
    public ArrayList<ParsedConstructor> constructors;
    public HashMap<String,String> fields;
    public ArrayList<ParsedMethod> methods;
    public ArrayList<String> interfaces;

    //public @Nullable ArrayList<Annotation> annotations;
    public ArrayList<Object> enums;


    public int modifiers;

    public boolean isEnum = false;

    ParsedClass(Class<?> cls) {
        this.packageName = cls.getPackageName();
        this.name = cls.getName().replace(this.packageName+".","");

        if(cls.getClasses().length >= 1) {
            this.classes = new ArrayList<String>();
            for(Class<?> cl : cls.getClasses()) {
                this.classes.add(cl.getName());
            }
        }
        if(cls.getConstructors().length >= 1) {
            this.constructors = new ArrayList<ParsedConstructor>();
            for(Constructor<?> c : cls.getConstructors()) {
                this.constructors.add(new ParsedConstructor(c));
            }
        }
        if(cls.getFields().length >= 1) {
            this.fields = new HashMap<>();
            for(Field f: cls.getFields()) {
                this.fields.put(f.getName(), f.getType().getName());
            }
        }
        if(cls.getMethods().length >= 1) {
            this.methods = new ArrayList<ParsedMethod>();
            for(Method m : cls.getMethods()) {
                this.methods.add(new ParsedMethod(m));
            }
        }
        if(cls.getInterfaces().length >= 1) {
            this.interfaces = new ArrayList<String>();
            for(Class<?> i : cls.getInterfaces()) {
                this.interfaces.add(i.getName());
            }
        }

        /*if(cls.isAnnotation()) {
            this.annotations = new ArrayList<Annotation>();
            for(Annotation a : cls.getAnnotations()) {
                this.annotations.add(a);
            }
        }*/
        if(cls.isEnum()) {
            this.enums = new ArrayList<Object>();
            for(Object o : cls.getEnumConstants()) {
                this.enums.add(o);
            }
        }

        this.modifiers = cls.getModifiers();

    }
}