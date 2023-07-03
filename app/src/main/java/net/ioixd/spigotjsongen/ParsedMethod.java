package net.ioixd.spigotjsongen;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ParsedMethod {
    public String name;
    public ArrayList<String> exceptionTypes = new ArrayList<String>();
    public ArrayList<String> genericExceptionTypes = new ArrayList<String>();
    public ArrayList<String> genericParameterTypes = new ArrayList<String>();
    public int modifiers;
    public ArrayList<String[]> parameters = new ArrayList<String[]>();
    public ArrayList<String> typeParameters = new ArrayList<String>();

    public String genericReturnType;

    public String returnType;

    public ParsedMethod(Method method) {
        this.name = method.getName();
        this.exceptionTypes = (ArrayList<String>) new ArrayList<>(Arrays.asList(method.getExceptionTypes())).stream().map(f -> {
            return f.getName();
        }).collect(Collectors.toList());
        this.genericExceptionTypes = (ArrayList<String>) new ArrayList<>(Arrays.asList(method.getGenericExceptionTypes())).stream().map(f -> {
            return f.getTypeName();
        }).collect(Collectors.toList());
        this.genericParameterTypes = (ArrayList<String>) new ArrayList<>(Arrays.asList(method.getGenericParameterTypes())).stream().map(f -> {
            return f.getTypeName();
        }).collect(Collectors.toList());
        this.modifiers = method.getModifiers();

        this.genericReturnType = method.getGenericReturnType().getTypeName();

        this.parameters = (ArrayList<String[]>) new ArrayList<>(Arrays.asList(method.getParameters())).stream().map(f -> {
            return new String[] {f.getName(), f.getType().getTypeName()};
        }).collect(Collectors.toList());

        this.returnType = method.getReturnType().getTypeName();
    }

}
