package com.lovemp.common.util;

import java.lang.reflect.*;
import java.util.*;

/**
 * 反射工具类
 * 
 * 提供简化Java反射操作的工具方法，帮助开发者更方便地使用反射机制，减少冗余代码。
 * 
 * 主要功能包括：
 * 1. 获取类信息：获取类的所有字段、方法（包括继承的私有成员）
 * 2. 字段操作：获取/设置字段值，支持按名称操作或直接通过Field对象
 * 3. 方法调用：通过方法名或Method对象调用方法，自动处理可访问性
 * 4. 实例创建：创建类的实例，自动处理私有构造函数
 * 5. 类型判断：判断是否为基本类型、集合类型、Map类型等
 * 6. 泛型操作：获取字段的泛型类型信息
 * 
 * 适用场景：
 * - 需要绕过可访问性限制进行操作时（如访问/设置私有字段）
 * - 动态操作类、对象的字段和方法时（如ORM框架实现）
 * - 根据配置或运行时条件调用方法时
 * - 框架开发时需要进行的底层反射操作
 * - 泛型类型信息的提取
 * 
 * 使用示例：
 * // 获取并设置私有字段
 * Field field = ReflectionUtils.getField(user.getClass(), "password");
 * ReflectionUtils.setFieldValue(user, field, "newPassword");
 * 
 * // 调用方法
 * ReflectionUtils.invokeMethod(service, "doInternalProcess", param1, param2);
 * 
 * 注意：反射操作会带来一定的性能开销，并可能绕过类的封装，请谨慎使用。
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
        // 工具类不允许实例化
    }

    /**
     * 获取类的所有字段，包括继承的私有字段
     *
     * @param clazz 目标类
     * @return 字段列表
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        
        // 如果类为null，直接返回空列表
        if (clazz == null) {
            return fields;
        }
        
        // 获取当前类的所有字段
        Collections.addAll(fields, clazz.getDeclaredFields());
        
        // 递归获取父类的字段
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            fields.addAll(getAllFields(superClass));
        }
        
        return fields;
    }

    /**
     * 获取类的所有方法，包括继承的私有方法
     *
     * @param clazz 目标类
     * @return 方法列表
     */
    public static List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        
        // 如果类为null，直接返回空列表
        if (clazz == null) {
            return methods;
        }
        
        // 获取当前类的所有方法
        Collections.addAll(methods, clazz.getDeclaredMethods());
        
        // 递归获取父类的方法
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            methods.addAll(getAllMethods(superClass));
        }
        
        return methods;
    }

    /**
     * 根据字段名获取字段
     *
     * @param clazz     目标类
     * @param fieldName 字段名
     * @return 字段对象，找不到返回null
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        if (clazz == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }
        
        try {
            // 尝试获取当前类的字段
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 如果当前类没有该字段，尝试从父类获取
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                return getField(superClass, fieldName);
            }
            
            // 如果父类也没有找到，返回null
            return null;
        }
    }

    /**
     * 根据方法名和参数类型获取方法
     *
     * @param clazz          目标类
     * @param methodName     方法名
     * @param parameterTypes 参数类型
     * @return 方法对象，找不到返回null
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null || StringUtils.isEmpty(methodName)) {
            return null;
        }
        
        try {
            // 尝试获取当前类的方法
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            // 如果当前类没有该方法，尝试从父类获取
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                return getMethod(superClass, methodName, parameterTypes);
            }
            
            // 如果父类也没有找到，返回null
            return null;
        }
    }

    /**
     * 获取字段的值
     *
     * @param obj   目标对象
     * @param field 字段
     * @return 字段值
     */
    public static Object getFieldValue(Object obj, Field field) {
        if (obj == null || field == null) {
            return null;
        }
        
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("获取字段值失败", e);
        }
    }

    /**
     * 根据字段名获取字段的值
     *
     * @param obj       目标对象
     * @param fieldName 字段名
     * @return 字段值
     */
    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }
        
        Field field = getField(obj.getClass(), fieldName);
        if (field == null) {
            return null;
        }
        
        return getFieldValue(obj, field);
    }

    /**
     * 设置字段的值
     *
     * @param obj   目标对象
     * @param field 字段
     * @param value 字段值
     */
    public static void setFieldValue(Object obj, Field field, Object value) {
        if (obj == null || field == null) {
            return;
        }
        
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("设置字段值失败", e);
        }
    }

    /**
     * 根据字段名设置字段的值
     *
     * @param obj       目标对象
     * @param fieldName 字段名
     * @param value     字段值
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) {
        if (obj == null || StringUtils.isEmpty(fieldName)) {
            return;
        }
        
        Field field = getField(obj.getClass(), fieldName);
        if (field == null) {
            return;
        }
        
        setFieldValue(obj, field, value);
    }

    /**
     * 调用方法
     *
     * @param obj    目标对象
     * @param method 方法
     * @param args   参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        if (obj == null || method == null) {
            return null;
        }
        
        try {
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("调用方法失败", e);
        }
    }

    /**
     * 根据方法名调用方法
     *
     * @param obj        目标对象
     * @param methodName 方法名
     * @param args       参数
     * @return 方法返回值
     */
    public static Object invokeMethod(Object obj, String methodName, Object... args) {
        if (obj == null || StringUtils.isEmpty(methodName)) {
            return null;
        }
        
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i] != null ? args[i].getClass() : null;
        }
        
        Method method = getMethod(obj.getClass(), methodName, parameterTypes);
        if (method == null) {
            return null;
        }
        
        return invokeMethod(obj, method, args);
    }

    /**
     * 创建实例
     *
     * @param clazz 目标类
     * @param <T>   类型
     * @return 创建的实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // 设置构造函数为可访问
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("创建实例失败", e);
        }
    }

    /**
     * 判断类是否为基本类型或其包装类
     *
     * @param clazz 待检查的类
     * @return 是否为基本类型或其包装类
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        
        return clazz.isPrimitive() 
                || clazz.equals(Boolean.class)
                || clazz.equals(Byte.class)
                || clazz.equals(Character.class)
                || clazz.equals(Short.class)
                || clazz.equals(Integer.class)
                || clazz.equals(Long.class)
                || clazz.equals(Float.class)
                || clazz.equals(Double.class);
    }

    /**
     * 判断类是否为集合类型
     *
     * @param clazz 待检查的类
     * @return 是否为集合类型
     */
    public static boolean isCollection(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断类是否为Map类型
     *
     * @param clazz 待检查的类
     * @return 是否为Map类型
     */
    public static boolean isMap(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        
        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * 获取泛型类型
     *
     * @param field 字段
     * @return 泛型类型数组
     */
    public static Class<?>[] getGenericTypes(Field field) {
        if (field == null) {
            return new Class<?>[0];
        }
        
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return new Class<?>[0];
        }
        
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        
        Class<?>[] classes = new Class<?>[typeArguments.length];
        for (int i = 0; i < typeArguments.length; i++) {
            Type typeArgument = typeArguments[i];
            if (typeArgument instanceof Class) {
                classes[i] = (Class<?>) typeArgument;
            } else {
                classes[i] = Object.class;
            }
        }
        
        return classes;
    }
} 