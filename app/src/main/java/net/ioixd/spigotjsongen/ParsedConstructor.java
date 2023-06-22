package net.ioixd.spigotjsongen;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ParsedConstructor {
    public String name;
    public String[] exceptionTypes = new String[] {};
    public String[] genericExceptionTypes = new String[] {};
    public String[] genericParameterTypes = new String[] {};
    public int modifiers;
    public String[] parameterTypes = new String[] {};
    public String[] typeParameters = new String[] {};

    public ParsedConstructor(Constructor<?> constructor) {
        this.name = constructor.getName();
        new ArrayList<>(Arrays.asList(constructor.getExceptionTypes())).stream().map(f -> {
            return f.getName();
        }).collect(Collectors.toList()).toArray(this.exceptionTypes);
        new ArrayList<>(Arrays.asList(constructor.getGenericExceptionTypes())).stream().map(f -> {
            return f.getTypeName();
        }).collect(Collectors.toList()).toArray(this.genericExceptionTypes);
        new ArrayList<>(Arrays.asList(constructor.getGenericParameterTypes())).stream().map(f -> {
            return f.getTypeName();
        }).collect(Collectors.toList()).toArray(this.genericParameterTypes);
        this.modifiers = constructor.getModifiers();
        new ArrayList<>(Arrays.asList(constructor.getParameterTypes())).stream().map(f -> {
            return f.getTypeName();
        }).collect(Collectors.toList()).toArray(this.parameterTypes);
        new ArrayList<>(Arrays.asList(constructor.getTypeParameters())).stream().map(f -> {
            return f.getTypeName();

        }).collect(Collectors.toList()).toArray(this.typeParameters);
    }
}
