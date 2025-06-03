package com.lovemp.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Bean工具类
 * 
 * 提供Java Bean对象间属性复制、转换和映射的高性能工具方法，支持复杂场景下的对象操作。
 * 
 * 主要功能包括：
 * 1. 基础属性复制：简单的浅拷贝，支持同名属性间直接复制
 * 2. 基于Getter/Setter的复制：通过调用方法而非直接字段访问进行复制
 * 3. 嵌套属性复制：支持复制多层嵌套的属性，如"user.address.city"
 * 4. 条件筛选复制：支持包含和排除特定属性的复制
 * 5. 类型转换：在复制过程中进行类型转换，处理不同类型间的兼容问题
 * 6. 自定义转换器：支持使用自定义转换器处理特殊属性
 * 7. 集合复制：批量处理列表对象的属性复制
 * 8. 对象深拷贝：创建对象的完整副本，包括内部引用对象
 * 9. 函数式映射：使用函数式接口实现更灵活的属性映射
 * 
 * 适用场景：
 * - DO、DTO、VO、BO等对象间的相互转换
 * - 领域模型与数据模型间的映射
 * - API请求/响应对象与内部业务对象的转换
 * - 数据导入导出时的对象转换
 * - 缓存数据与实体对象的相互转换
 * - 处理复杂嵌套属性的数据结构转换
 * 
 * 使用示例：
 * // 基本属性复制
 * BeanUtils.copyProperties(userDO, userDTO);
 * 
 * // 选择性属性复制
 * BeanUtils.copySpecificProperties(source, target, "id", "name", "age");
 * 
 * // 类型转换复制
 * UserDTO dto = BeanUtils.copyProperties(userDO, UserDTO.class);
 * 
 * // 自定义转换器
 * BeanUtils.copyWithConverter(source, target, (s, t) -> {
 *     t.setFullName(s.getFirstName() + " " + s.getLastName());
 * });
 * 
 * 注意：
 * 1. 提供多种缓存机制提高反射性能
 * 2. 对基本类型提供默认值处理，避免空指针异常
 * 3. 支持复杂类型转换，如字符串与数字、日期等类型的互转
 */
public final class BeanUtils {

    private BeanUtils() {
        // 工具类不允许实例化
    }

    // 类属性缓存，提高反射性能
    private static final Map<Class<?>, Map<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<Field>> FIELDS_CACHE = new ConcurrentHashMap<>();

    /**
     * 复制属性（浅拷贝）
     * 从源对象复制同名属性到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源对象类型
     * @param <T>    目标对象类型
     */
    public static <S, T> void copyProperties(S source, T target) {
        copyProperties(source, target, (String[]) null);
    }

    /**
     * 复制属性（浅拷贝）
     * 从源对象复制同名属性到目标对象，可以指定要排除的属性
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 要忽略的属性名
     * @param <S>              源对象类型
     * @param <T>              目标对象类型
     */
    public static <S, T> void copyProperties(S source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            return;
        }

        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : Collections.emptyList();

        // 获取源对象的所有字段
        List<Field> sourceFields = getAllFields(source.getClass());
        // 获取目标对象的所有字段
        List<Field> targetFields = getAllFields(target.getClass());

        // 复制属性
        for (Field sourceField : sourceFields) {
            // 忽略静态字段和被忽略的属性
            if (Modifier.isStatic(sourceField.getModifiers()) || ignoreList.contains(sourceField.getName())) {
                continue;
            }

            // 查找目标对象中同名字段
            Field targetField = targetFields.stream()
                    .filter(field -> field.getName().equals(sourceField.getName()))
                    .findFirst()
                    .orElse(null);

            if (targetField != null && !Modifier.isStatic(targetField.getModifiers())) {
                try {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    // 获取源字段的值并设置到目标字段
                    Object value = sourceField.get(source);
                    targetField.set(target, value);
                } catch (IllegalAccessException e) {
                    // 忽略无法访问的字段
                }
            }
        }
    }

    /**
     * 基于getter/setter方法复制属性
     * 从源对象通过getter方法获取值，通过setter方法设置到目标对象
     *
     * @param source 源对象
     * @param target 目标对象
     * @param <S>    源对象类型
     * @param <T>    目标对象类型
     */
    public static <S, T> void copyPropertiesByGetterSetter(S source, T target) {
        copyPropertiesByGetterSetter(source, target, (String[]) null);
    }

    /**
     * 基于getter/setter方法复制属性，可以指定要排除的属性
     *
     * @param source           源对象
     * @param target           目标对象
     * @param ignoreProperties 要忽略的属性名
     * @param <S>              源对象类型
     * @param <T>              目标对象类型
     */
    public static <S, T> void copyPropertiesByGetterSetter(S source, T target, String... ignoreProperties) {
        if (source == null || target == null) {
            return;
        }

        List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : Collections.emptyList();
        
        // 获取源对象的所有getter方法
        Map<String, Method> sourceGetters = getGetterMethods(source.getClass());
        // 获取目标对象的所有setter方法
        Map<String, Method> targetSetters = getSetterMethods(target.getClass());
        
        // 对每个getter方法，查找对应的setter方法并复制值
        for (Map.Entry<String, Method> entry : sourceGetters.entrySet()) {
            String propertyName = entry.getKey();
            
            // 忽略被排除的属性
            if (ignoreList.contains(propertyName)) {
                continue;
            }
            
            Method getter = entry.getValue();
            Method setter = targetSetters.get(propertyName);
            
            // 如果目标对象有对应的setter方法
            if (setter != null) {
                try {
                    // 获取getter方法的返回类型和setter方法的参数类型
                    Class<?> paramType = setter.getParameterTypes()[0];
                    
                    // 调用getter获取值
                    Object value = getter.invoke(source);
                    
                    // 类型兼容检查
                    if (value != null) {
                        // 如果类型不兼容，尝试进行转换
                        if (!paramType.isAssignableFrom(value.getClass())) {
                            value = convertValueIfPossible(value, paramType);
                        }
                    } else {
                        // 如果值为null且目标参数是基本类型，则需要设置默认值
                        if (paramType.isPrimitive()) {
                            value = getDefaultValueForPrimitive(paramType);
                        }
                    }
                    
                    // 调用setter设置值
                    setter.invoke(target, value);
                } catch (Exception e) {
                    // 记录异常信息但继续处理其他属性
                    System.err.println("复制属性 " + propertyName + " 时出错: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 获取基本类型的默认值
     * 
     * @param primitiveType 基本类型的Class
     * @return 对应的默认值
     */
    private static Object getDefaultValueForPrimitive(Class<?> primitiveType) {
        if (primitiveType == int.class) {
            return 0;
        } else if (primitiveType == long.class) {
            return 0L;
        } else if (primitiveType == double.class) {
            return 0.0;
        } else if (primitiveType == float.class) {
            return 0.0f;
        } else if (primitiveType == boolean.class) {
            return false;
        } else if (primitiveType == byte.class) {
            return (byte) 0;
        } else if (primitiveType == short.class) {
            return (short) 0;
        } else if (primitiveType == char.class) {
            return '\u0000';
        }
        return null;
    }

    /**
     * 复制嵌套属性
     * 支持形如"address.city"这样的嵌套属性路径
     *
     * @param source      源对象
     * @param target      目标对象
     * @param nestedPaths 嵌套属性路径
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     */
    public static <S, T> void copyNestedProperties(S source, T target, String... nestedPaths) {
        if (source == null || target == null || nestedPaths == null || nestedPaths.length == 0) {
            return;
        }
        
        for (String path : nestedPaths) {
            copyNestedProperty(source, target, path);
        }
    }

    /**
     * 复制单个嵌套属性
     *
     * @param source 源对象
     * @param target 目标对象
     * @param path   嵌套属性路径，如"address.city"
     */
    private static void copyNestedProperty(Object source, Object target, String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        
        String[] pathParts = path.split("\\.");
        if (pathParts.length == 1) {
            // 非嵌套属性，直接复制
            try {
                Field sourceField = getField(source.getClass(), path);
                Field targetField = getField(target.getClass(), path);
                
                if (sourceField != null && targetField != null) {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);
                    
                    Object value = sourceField.get(source);
                    targetField.set(target, value);
                }
            } catch (Exception e) {
                // 忽略反射异常
            }
            return;
        }
        
        // 处理嵌套属性
        Object sourceNestedObj = source;
        Object targetNestedObj = target;
        
        for (int i = 0; i < pathParts.length - 1; i++) {
            String part = pathParts[i];
            
            try {
                // 获取源对象的嵌套对象
                Field sourceField = getField(sourceNestedObj.getClass(), part);
                if (sourceField == null) {
                    return;
                }
                
                sourceField.setAccessible(true);
                sourceNestedObj = sourceField.get(sourceNestedObj);
                
                if (sourceNestedObj == null) {
                    return;
                }
                
                // 获取目标对象的嵌套对象
                Field targetField = getField(targetNestedObj.getClass(), part);
                if (targetField == null) {
                    return;
                }
                
                targetField.setAccessible(true);
                Object targetFieldValue = targetField.get(targetNestedObj);
                
                // 如果目标嵌套对象为null，则创建新实例
                if (targetFieldValue == null) {
                    targetFieldValue = targetField.getType().getDeclaredConstructor().newInstance();
                    targetField.set(targetNestedObj, targetFieldValue);
                }
                
                targetNestedObj = targetFieldValue;
            } catch (Exception e) {
                // 忽略反射异常
                return;
            }
        }
        
        // 复制最后一级属性
        String lastPart = pathParts[pathParts.length - 1];
        try {
            Field sourceField = getField(sourceNestedObj.getClass(), lastPart);
            Field targetField = getField(targetNestedObj.getClass(), lastPart);
            
            if (sourceField != null && targetField != null) {
                sourceField.setAccessible(true);
                targetField.setAccessible(true);
                
                Object value = sourceField.get(sourceNestedObj);
                targetField.set(targetNestedObj, value);
            }
        } catch (Exception e) {
            // 忽略反射异常
        }
    }

    /**
     * 带类型转换的属性复制
     * 
     * @param source     源对象
     * @param target     目标对象
     * @param converters 类型转换器映射
     * @param <S>        源对象类型
     * @param <T>        目标对象类型
     */
    public static <S, T> void copyPropertiesWithTypeConversion(S source, T target, 
                                                              Map<Class<?>, Function<Object, Object>> converters) {
        if (source == null || target == null) {
            return;
        }
        
        // 获取源对象的所有字段
        List<Field> sourceFields = getAllFields(source.getClass());
        // 获取目标对象的所有字段
        List<Field> targetFields = getAllFields(target.getClass());
        
        // 复制属性
        for (Field sourceField : sourceFields) {
            // 忽略静态字段
            if (Modifier.isStatic(sourceField.getModifiers())) {
                continue;
            }
            
            // 查找目标对象中同名字段
            Field targetField = targetFields.stream()
                    .filter(field -> field.getName().equals(sourceField.getName()))
                    .findFirst()
                    .orElse(null);
            
            if (targetField != null && !Modifier.isStatic(targetField.getModifiers())) {
                try {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);
                    
                    // 获取源字段的值
                    Object value = sourceField.get(source);
                    
                    if (value != null) {
                        // 检查是否需要类型转换
                        Class<?> sourceType = value.getClass();
                        Class<?> targetType = targetField.getType();
                        
                        if (!targetType.isAssignableFrom(sourceType) && converters != null) {
                            // 查找适用的转换器
                            Function<Object, Object> converter = converters.get(targetType);
                            if (converter != null) {
                                value = converter.apply(value);
                            } else {
                                // 尝试简单类型转换
                                value = convertValueIfPossible(value, targetType);
                            }
                        }
                    }
                    
                    // 设置目标字段的值
                    targetField.set(target, value);
                } catch (IllegalAccessException e) {
                    // 忽略无法访问的字段
                }
            }
        }
    }

    /**
     * 简单类型转换
     * 支持基本数值类型、字符串、日期等常见类型间的转换
     * 
     * @param value     源值
     * @param targetType 目标类型
     * @return 转换后的值，如果无法转换则返回原值
     */
    private static Object convertValueIfPossible(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        
        Class<?> sourceType = value.getClass();
        
        // 字符串转换为基本类型
        if (sourceType == String.class) {
            String strValue = (String) value;
            
            if (targetType == Integer.class || targetType == int.class) {
                try {
                    return Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    return 0;
                }
            } else if (targetType == Long.class || targetType == long.class) {
                try {
                    return Long.parseLong(strValue);
                } catch (NumberFormatException e) {
                    return 0L;
                }
            } else if (targetType == Double.class || targetType == double.class) {
                try {
                    return Double.parseDouble(strValue);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            } else if (targetType == Float.class || targetType == float.class) {
                try {
                    return Float.parseFloat(strValue);
                } catch (NumberFormatException e) {
                    return 0.0f;
                }
            } else if (targetType == Boolean.class || targetType == boolean.class) {
                return Boolean.parseBoolean(strValue);
            } else if (targetType == java.math.BigDecimal.class) {
                try {
                    return new java.math.BigDecimal(strValue);
                } catch (NumberFormatException e) {
                    return java.math.BigDecimal.ZERO;
                }
            } else if (targetType == java.math.BigInteger.class) {
                try {
                    return new java.math.BigInteger(strValue);
                } catch (NumberFormatException e) {
                    return java.math.BigInteger.ZERO;
                }
            }
        }
        
        // 数值类型互相转换
        if (Number.class.isAssignableFrom(sourceType)) {
            Number numValue = (Number) value;
            
            if (targetType == Integer.class || targetType == int.class) {
                return numValue.intValue();
            } else if (targetType == Long.class || targetType == long.class) {
                return numValue.longValue();
            } else if (targetType == Double.class || targetType == double.class) {
                return numValue.doubleValue();
            } else if (targetType == Float.class || targetType == float.class) {
                return numValue.floatValue();
            } else if (targetType == String.class) {
                return numValue.toString();
            } else if (targetType == java.math.BigDecimal.class) {
                // 添加对数值类型转BigDecimal的支持
                if (sourceType == Double.class || sourceType == double.class) {
                    return new java.math.BigDecimal(numValue.toString());
                } else if (sourceType == Long.class || sourceType == long.class) {
                    return java.math.BigDecimal.valueOf(numValue.longValue());
                } else if (sourceType == Integer.class || sourceType == int.class) {
                    return java.math.BigDecimal.valueOf(numValue.intValue());
                } else if (sourceType == Float.class || sourceType == float.class) {
                    // 直接使用toString避免浮点精度问题
                    return new java.math.BigDecimal(numValue.toString());
                }
                return new java.math.BigDecimal(numValue.toString());
            } else if (targetType == java.math.BigInteger.class) {
                if (sourceType == Long.class || sourceType == long.class) {
                    return java.math.BigInteger.valueOf(numValue.longValue());
                } else {
                    return new java.math.BigInteger(numValue.toString());
                }
            }
        }
        
        // 其他类型转字符串
        if (targetType == String.class) {
            return value.toString();
        }
        
        // 无法转换，返回原值
        return value;
    }

    /**
     * 复制指定属性
     * 从源对象复制指定属性到目标对象
     *
     * @param source            源对象
     * @param target            目标对象
     * @param includeProperties 需要复制的属性名
     * @param <S>               源对象类型
     * @param <T>               目标对象类型
     */
    public static <S, T> void copySpecificProperties(S source, T target, String... includeProperties) {
        if (source == null || target == null || includeProperties == null || includeProperties.length == 0) {
            return;
        }

        List<String> includeList = Arrays.asList(includeProperties);

        // 获取源对象的所有字段
        List<Field> sourceFields = getAllFields(source.getClass());
        // 获取目标对象的所有字段
        List<Field> targetFields = getAllFields(target.getClass());

        // 复制指定属性
        for (Field sourceField : sourceFields) {
            // 只复制指定的属性
            if (!includeList.contains(sourceField.getName())) {
                continue;
            }

            // 查找目标对象中同名字段
            Field targetField = targetFields.stream()
                    .filter(field -> field.getName().equals(sourceField.getName()))
                    .findFirst()
                    .orElse(null);

            if (targetField != null) {
                try {
                    sourceField.setAccessible(true);
                    targetField.setAccessible(true);

                    // 获取源字段的值并设置到目标字段
                    Object value = sourceField.get(source);
                    targetField.set(target, value);
                } catch (IllegalAccessException e) {
                    // 忽略无法访问的字段
                }
            }
        }
    }

    /**
     * 创建目标对象并复制属性
     * 创建目标类的实例，并从源对象复制属性
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 目标对象实例
     */
    public static <S, T> T copyProperties(S source, Class<T> targetClass) {
        if (source == null || targetClass == null) {
            return null;
        }

        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("创建对象并复制属性失败", e);
        }
    }

    /**
     * 批量复制属性
     * 从源对象列表复制属性到目标类的新实例列表
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标类
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 目标对象列表
     */
    public static <S, T> List<T> copyPropertiesList(List<S> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList) || targetClass == null) {
            return new ArrayList<>();
        }

        return sourceList.stream()
                .map(source -> copyProperties(source, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * 使用转换器复制对象
     * 通过指定的转换器函数复制源对象到目标对象
     *
     * @param source    源对象
     * @param target    目标对象
     * @param converter 转换器函数，用于自定义转换逻辑
     * @param <S>       源对象类型
     * @param <T>       目标对象类型
     */
    public static <S, T> void copyWithConverter(S source, T target, BiConsumer<S, T> converter) {
        if (source == null || target == null) {
            return;
        }

        // 使用支持类型转换的方法进行基本属性复制
        copyPropertiesByGetterSetter(source, target);
        
        // 如果提供了转换器，应用自定义转换逻辑
        if (converter != null) {
            converter.accept(source, target);
        }
    }

    /**
     * 使用转换器创建并复制对象
     * 创建目标类的实例，并通过指定的转换器函数复制源对象
     *
     * @param source      源对象
     * @param targetClass 目标类
     * @param converter   转换器函数，用于自定义转换逻辑
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 目标对象实例
     */
    public static <S, T> T copyWithConverter(S source, Class<T> targetClass, BiConsumer<S, T> converter) {
        if (source == null || targetClass == null) {
            return null;
        }

        try {
            // 创建目标类的实例
            T target = targetClass.getDeclaredConstructor().newInstance();
            
            // 使用支持类型转换的方法进行基本属性复制
            copyPropertiesByGetterSetter(source, target);
            
            // 如果提供了转换器，应用自定义转换逻辑
            if (converter != null) {
                converter.accept(source, target);
            }
            
            return target;
        } catch (Exception e) {
            throw new RuntimeException("创建对象并使用转换器复制失败: " + e.getMessage(), e);
        }
    }

    /**
     * 使用转换器批量复制对象
     * 从源对象列表使用转换器复制到目标类的新实例列表
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标类
     * @param converter   转换器函数，用于自定义转换逻辑
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 目标对象列表
     */
    public static <S, T> List<T> copyListWithConverter(List<S> sourceList, Class<T> targetClass, BiConsumer<S, T> converter) {
        if (CollectionUtils.isEmpty(sourceList) || targetClass == null || converter == null) {
            return new ArrayList<>();
        }

        return sourceList.stream()
                .map(source -> copyWithConverter(source, targetClass, converter))
                .collect(Collectors.toList());
    }

    /**
     * 通用对象转换器
     * 使用提供的映射函数进行对象转换
     *
     * @param source      源对象
     * @param constructor 目标对象构造器
     * @param mappings    属性映射函数数组
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 转换后的目标对象
     */
    @SafeVarargs
    public static <S, T> T map(S source, Supplier<T> constructor, Function<S, BiConsumer<S, T>>... mappings) {
        if (source == null || constructor == null || mappings == null) {
            return null;
        }

        T target = constructor.get();
        for (Function<S, BiConsumer<S, T>> mapping : mappings) {
            BiConsumer<S, T> consumer = mapping.apply(source);
            if (consumer != null) {
                consumer.accept(source, target);
            }
        }

        return target;
    }

    /**
     * 通用列表对象转换器
     * 使用提供的映射函数批量进行对象转换
     *
     * @param sourceList  源对象列表
     * @param constructor 目标对象构造器
     * @param mappings    属性映射函数数组
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 转换后的目标对象列表
     */
    @SafeVarargs
    public static <S, T> List<T> mapList(List<S> sourceList, Supplier<T> constructor, Function<S, BiConsumer<S, T>>... mappings) {
        if (CollectionUtils.isEmpty(sourceList) || constructor == null || mappings == null) {
            return Collections.emptyList();
        }

        return sourceList.stream()
                .map(source -> map(source, constructor, mappings))
                .collect(Collectors.toList());
    }

    /**
     * 对象深拷贝（通过JSON序列化和反序列化实现）
     *
     * @param source 源对象
     * @param <T>    对象类型
     * @return 深拷贝后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T source) {
        if (source == null) {
            return null;
        }

        String json = JsonUtils.toJson(source);
        return (T) JsonUtils.fromJson(json, source.getClass());
    }

    // 工具方法

    /**
     * 获取类的所有字段，使用缓存提高性能
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        return FIELDS_CACHE.computeIfAbsent(clazz, key -> {
            List<Field> fields = new ArrayList<>();
            Class<?> currentClass = key;
            
            while (currentClass != null && currentClass != Object.class) {
                Collections.addAll(fields, currentClass.getDeclaredFields());
                currentClass = currentClass.getSuperclass();
            }
            
            return fields;
        });
    }

    /**
     * 获取字段对象
     */
    private static Field getField(Class<?> clazz, String fieldName) {
        List<Field> fields = getAllFields(clazz);
        return fields.stream()
                .filter(field -> field.getName().equals(fieldName))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取类的所有getter方法，使用缓存提高性能
     */
    private static Map<String, Method> getGetterMethods(Class<?> clazz) {
        return GETTER_CACHE.computeIfAbsent(clazz, key -> {
            Map<String, Method> getters = new HashMap<>();
            Class<?> currentClass = key;
            
            while (currentClass != null && currentClass != Object.class) {
                for (Method method : currentClass.getDeclaredMethods()) {
                    if (Modifier.isPublic(method.getModifiers()) && 
                            method.getParameterCount() == 0 && 
                            method.getReturnType() != void.class) {
                        
                        String methodName = method.getName();
                        String propertyName = null;
                        
                        if (methodName.startsWith("get") && methodName.length() > 3) {
                            propertyName = StringUtils.uncapitalize(methodName.substring(3));
                        } else if (methodName.startsWith("is") && methodName.length() > 2 && 
                                (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)) {
                            propertyName = StringUtils.uncapitalize(methodName.substring(2));
                        }
                        
                        if (propertyName != null) {
                            getters.putIfAbsent(propertyName, method);
                        }
                    }
                }
                
                currentClass = currentClass.getSuperclass();
            }
            
            return getters;
        });
    }

    /**
     * 获取类的所有setter方法，使用缓存提高性能
     */
    private static Map<String, Method> getSetterMethods(Class<?> clazz) {
        return SETTER_CACHE.computeIfAbsent(clazz, key -> {
            Map<String, Method> setters = new HashMap<>();
            Class<?> currentClass = key;
            
            while (currentClass != null && currentClass != Object.class) {
                for (Method method : currentClass.getDeclaredMethods()) {
                    if (Modifier.isPublic(method.getModifiers()) && 
                            method.getParameterCount() == 1 && 
                            method.getReturnType() == void.class) {
                        
                        String methodName = method.getName();
                        
                        if (methodName.startsWith("set") && methodName.length() > 3) {
                            String propertyName = StringUtils.uncapitalize(methodName.substring(3));
                            setters.putIfAbsent(propertyName, method);
                        }
                    }
                }
                
                currentClass = currentClass.getSuperclass();
            }
            
            return setters;
        });
    }
} 