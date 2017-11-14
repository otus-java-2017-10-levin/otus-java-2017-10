package ru.otus.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionHelper {

    public static <T> T instantiate(Class<T> type, Object... args) {
        try {
            if (args.length == 0) {
                return type.getConstructor().newInstance();
            } else {
                return type.getConstructor(toClasses(args)).newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends Annotation> T getMethodAnnotation(Class<?> clazz, String name, Class<T> annotation) {
        T result = null;
        try {
            result = clazz.getMethod(name).getDeclaredAnnotation(annotation);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Annotation[] getMethodAnnotations(Class<?> clazz, String name) {
        try {
            Method m = clazz.getMethod(name);
            return m.getDeclaredAnnotations();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new Annotation[0];
    }

    public static List<String> getMethods(Class<?> clazz) {
        List<Method> methods = Arrays.asList(clazz.getMethods());

        List<String> result = new ArrayList<>();
        for (Method m: methods) {
            if (m.getGenericParameterTypes().length == 0)
            result.add(m.getName());
        }
        return result;
    }

    public static Object callMethod(Object object, String name, Object... args) throws RuntimeException {
        Method method = null;
        boolean isAccessible = false;
        try {
            method = object.getClass().getDeclaredMethod(name, toClasses(args));
            isAccessible = method.isAccessible();
            method.setAccessible(true);
            return method.invoke(object, args);
        } catch (IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } finally {
            if (method != null && !isAccessible) {
                method.setAccessible(false);
            }
        }
        return null;
    }

    private static  Class<?>[] toClasses(Object[] args) {
        List<Class<?>> classes = Arrays.stream(args)
                .map(Object::getClass)
                .collect(Collectors.toList());
        return classes.toArray(new Class<?>[classes.size()]);
    }

}