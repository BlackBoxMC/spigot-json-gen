package net.ioixd.spigotjsongen;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ParsedEnum {
    public String name;
    public String packageName;
    public ArrayList<String> values = new ArrayList<String>();
    public int ordinal;
    public ArrayList<ParsedClass> classes;
    public ArrayList<ParsedMethod> methods;

    public boolean isEnum = true;
    ParsedEnum(Enum<?> e) {
        this.name = e.getClass().getSimpleName();
        this.packageName = e.getClass().getPackageName();

        Enum<?>[] constants = e.getClass().getEnumConstants();
        if(constants != null) {
            for(Enum<?> en : constants) {
                values.add(en.name());
            };
        }
        this.ordinal = e.ordinal();

        if(e.getClass().getDeclaredMethods().length >= 1) {
            this.methods = new ArrayList<ParsedMethod>();
            for(Method m : e.getClass().getDeclaredMethods()) {
                this.methods.add(new ParsedMethod(m));
            }
        }

        if(e.getClass().getClasses().length >= 1) {
            this.classes = new ArrayList<ParsedClass>();
            for(Class<?> cl : e.getClass().getClasses()) {
                if(cl.getPackageName().startsWith("java")) {
                    // hell naw
                    continue;
                }
                this.classes.add(new ParsedClass(cl));
            }
        }
    }
}
