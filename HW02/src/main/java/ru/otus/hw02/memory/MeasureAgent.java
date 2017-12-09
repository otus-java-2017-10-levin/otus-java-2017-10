package ru.otus.hw02.memory;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;

class MeasureAgent {

    private static volatile Instrumentation globalInstrumentation;
    private static boolean SKIP_POOLED_OBJECTS = false;

    public static void premain(String agentArgs, Instrumentation inst) {
        globalInstrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        globalInstrumentation = inst;
    }

    /**
     * Calculates deep size
     *
     * @param obj object to calculate size of
     * @return object deep size
     */
    public static long deepSizeOfObject(Object obj) {

        Map<Object, Object> previouslyVisited = new IdentityHashMap<>();
        long result = deepSizeOf(obj, previouslyVisited);
        previouslyVisited.clear();
        return result;
    }

    public static long shallowSizeOfObject(final Object obj) {
        if (globalInstrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        if (SKIP_POOLED_OBJECTS && isPooled(obj))
            return 0;

        return globalInstrumentation.getObjectSize(obj);
    }

    private static boolean isPooled(Object paramObject) {
        if ((paramObject instanceof Comparable)) {
            if ((paramObject instanceof Enum)) {
                return true;
            }
            if ((paramObject instanceof String)) {
                return paramObject == ((String) paramObject).intern();
            }
            if ((paramObject instanceof Boolean)) {
                return (paramObject == Boolean.TRUE) || (paramObject == Boolean.FALSE);
            }
            if ((paramObject instanceof Integer)) {
                return paramObject == Integer.valueOf(((Integer) paramObject).intValue());
            }
            if ((paramObject instanceof Short)) {
                return paramObject == Short.valueOf(((Short) paramObject).shortValue());
            }
            if ((paramObject instanceof Byte)) {
                return paramObject == Byte.valueOf(((Byte) paramObject).byteValue());
            }
            if ((paramObject instanceof Long)) {
                return paramObject == Long.valueOf(((Long) paramObject).longValue());
            }
            if ((paramObject instanceof Character)) {
                return paramObject == Character.valueOf(((Character) paramObject).charValue());
            }
        }
        return false;
    }



    private static boolean skipObject(Object obj, Map<Object, Object> previouslyVisited) {
        if (SKIP_POOLED_OBJECTS && isPooled(obj))
            return true;

        return (obj == null) || previouslyVisited == null || previouslyVisited.containsKey(obj);
    }

    private static long deepSizeOf(Object obj, Map<Object, Object> previouslyVisited) {
        if (skipObject(obj, previouslyVisited)) {
            return 0;
        }
        previouslyVisited.put(obj, null);

        long returnVal = 0;
        // get size of object + primitive variables + member pointers
        // for array header + len + if primitive total value for primitives
        returnVal += MeasureAgent.shallowSizeOfObject(obj);

        // recursively call all array elements
        Class objClass = obj.getClass();
        if (objClass == null)
            return 0;

        if (objClass.isArray()) {
            if (objClass.getName().length() != 2) {// primitive type arrays has length two, skip them (they included in the shallow size)
                int lengthOfArray = Array.getLength(obj);
                for (int i = 0; i < lengthOfArray; i++) {
                    returnVal += deepSizeOf(Array.get(obj, i), previouslyVisited);
                }
            }
        } else {
            // recursively call all fields of the object including the superclass fields
            do {
                Field[] objFields = objClass.getDeclaredFields();
                for (Field field : objFields) {
                    if (!Modifier.isStatic(field.getModifiers())) {// skip statics
                        if (!field.getType().isPrimitive()) { // skip primitives
                            field.setAccessible(true);
                            Object tempObject = null;
                            try {
                                tempObject = field.get(obj);
                            } catch (IllegalArgumentException | IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            if (tempObject != null) {
                                returnVal += deepSizeOf(tempObject, previouslyVisited);
                            }
                        }
                    }
                }
                objClass = objClass.getSuperclass();
            } while (objClass != null);
        }
        return returnVal;
    }
}