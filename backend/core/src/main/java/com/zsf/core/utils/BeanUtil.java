package com.zsf.core.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yyq
 */
public class BeanUtil {

    private BeanUtil(){

    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) {
        if (map == null){
            return null;
        }

        T obj = null;
        try {
            obj = beanClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }

            field.setAccessible(true);

            try {
                field.set(obj, map.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return obj;
    }

    public static Map<String, Object> objectToMap(Object obj) {
        if(obj == null){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);

            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        return map;
    }

    public static <T> List<T> deepCopy(List<T> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            List<T> dest = (List<T>) in.readObject();
            return dest;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAllNull(Object object) {

        Field[] fields = object.getClass().getDeclaredFields();

        return Arrays.stream(fields).allMatch((field) -> {
            field.setAccessible(true);
            try {
                return Objects.isNull(field.get(object));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        });

    }
}
