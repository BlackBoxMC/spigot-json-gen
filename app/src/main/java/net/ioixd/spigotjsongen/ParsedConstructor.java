package net.ioixd.spigotjsongen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import java.lang.reflect.Method;

import java.lang.annotation.Annotation;

public class ParsedConstructor {
    public String name;
    public ArrayList<String> exceptionTypes = new ArrayList<String>();
    public ArrayList<String> genericExceptionTypes = new ArrayList<String>();
    public ArrayList<String> genericParameterTypes = new ArrayList<String>();
    public int modifiers;
    public ArrayList<String[]> parameters = new ArrayList<String[]>();
    public ArrayList<String> typeParameters = new ArrayList<String>();
    public ArrayList<String[]> annotations = new ArrayList<String[]>();

    public ParsedConstructor(Constructor<?> constructor) {
        this.name = constructor.getName();
        this.exceptionTypes = (ArrayList<String>) new ArrayList<>(Arrays.asList(constructor.getExceptionTypes()))
                .stream().map(f -> {
                    return f.getName();
                }).collect(Collectors.toList());
        this.genericExceptionTypes = (ArrayList<String>) new ArrayList<>(
                Arrays.asList(constructor.getGenericExceptionTypes())).stream().map(f -> {
                    return f.getTypeName();
                }).collect(Collectors.toList());
        this.genericParameterTypes = (ArrayList<String>) new ArrayList<>(
                Arrays.asList(constructor.getGenericParameterTypes())).stream().map(f -> {
                    return f.getTypeName();
                }).collect(Collectors.toList());
        this.modifiers = constructor.getModifiers();

        this.parameters = (ArrayList<String[]>) new ArrayList<>(Arrays.asList(constructor.getParameters())).stream()
                .map(f -> {
                    return new String[] { f.getName(), f.getType().getTypeName() };
                }).collect(Collectors.toList());

        this.annotations = new ArrayList<>(Arrays.asList());
        for (Annotation annotation : constructor.getAnnotations()) {
            for (Method method : annotation.annotationType().getDeclaredMethods()) {
                Object value;
                try {
                    value = method.invoke(annotation, (Object[]) null);
                    this.annotations.add(new String[] { method.getName(), value.toString() });
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
