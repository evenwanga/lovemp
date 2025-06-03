package com.lovemp.common.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 集合工具类
 * 
 * 提供各种集合类型（List、Set、Map等）的常用操作方法，简化集合处理，提高代码可读性和开发效率。
 * 
 * 主要功能包括：
 * 1. 集合判空：检查集合是否为空或非空
 * 2. 集合过滤：按条件筛选集合元素
 * 3. 元素查找：查找满足条件的第一个元素
 * 4. 集合转换：集合到Map的转换，元素类型转换
 * 5. 集合分组：按照指定属性对集合元素进行分组
 * 6. 集合合并：多个集合的合并操作
 * 7. 集合运算：交集、差集等集合运算
 * 8. 安全操作：提供安全的集合添加、获取方法
 * 
 * 适用场景：
 * - 需要对集合进行判空检查
 * - 需要从集合中筛选满足条件的元素
 * - 需要将集合转换为Map进行快速查找
 * - 需要对集合元素进行分组统计
 * - 需要对多个集合进行合并或比较
 * - 需要安全地操作可能为null的集合
 * - 需要对集合元素进行批量转换
 * 
 * 使用示例：
 * // 判断集合是否为空
 * if (CollectionUtils.isNotEmpty(userList)) {
 *     // 处理非空集合
 * }
 * 
 * // 过滤满足条件的元素
 * List<User> adultUsers = CollectionUtils.filter(userList, user -> user.getAge() >= 18);
 * 
 * // 按属性分组
 * Map<String, List<User>> usersByDept = CollectionUtils.groupBy(userList, User::getDepartment);
 * 
 * // 集合元素转换
 * List<String> userNames = CollectionUtils.map(userList, User::getName);
 * 
 * 注意：
 * 1. 方法设计考虑了null安全，大多数方法在输入为null时会返回空集合而非抛出异常
 * 2. 大部分方法利用Java 8 Stream API实现，提供函数式处理能力
 * 3. 转换和过滤方法会创建新集合，不会修改原集合
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // 工具类不允许实例化
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 待检查集合
     * @return 为空返回true，否则返回false
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否不为空
     *
     * @param collection 待检查集合
     * @return 不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     *
     * @param map 待检查Map
     * @return 为空返回true，否则返回false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 待检查Map
     * @return 不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 过滤集合
     *
     * @param collection 源集合
     * @param predicate 过滤条件
     * @param <T> 元素类型
     * @return 过滤后的新集合
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * 获取集合第一个元素
     *
     * @param collection 源集合
     * @param <T> 元素类型
     * @return 第一个元素，如果集合为空则返回null
     */
    public static <T> T firstOrNull(Collection<T> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.iterator().next();
    }

    /**
     * 获取集合第一个满足条件的元素
     *
     * @param collection 源集合
     * @param predicate 条件
     * @param <T> 元素类型
     * @return 第一个满足条件的元素，如果没有则返回null
     */
    public static <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return null;
        }
        return collection.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * 将集合转换为Map
     *
     * @param collection 源集合
     * @param keyMapper key提取函数
     * @param <T> 集合元素类型
     * @param <K> Map键类型
     * @return 转换后的Map
     */
    public static <T, K> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, Function.identity(), (a, b) -> a));
    }

    /**
     * 将集合转换为Map
     *
     * @param collection 源集合
     * @param keyMapper key提取函数
     * @param valueMapper value提取函数
     * @param <T> 集合元素类型
     * @param <K> Map键类型
     * @param <V> Map值类型
     * @return 转换后的Map
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> collection, 
                                           Function<T, K> keyMapper,
                                           Function<T, V> valueMapper) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper, (a, b) -> a));
    }

    /**
     * 将集合按照指定属性分组
     *
     * @param collection 源集合
     * @param classifier 分组依据
     * @param <T> 集合元素类型
     * @param <K> 分组键类型
     * @return 分组后的Map
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> classifier) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.groupingBy(classifier));
    }

    /**
     * 转换集合元素
     *
     * @param collection 源集合
     * @param mapper 映射函数
     * @param <T> 源集合元素类型
     * @param <R> 目标集合元素类型
     * @return 转换后的新集合
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * 安全地向集合中添加元素
     *
     * @param collection 目标集合
     * @param element 待添加元素
     * @param <T> 元素类型
     * @return 是否添加成功
     */
    public static <T> boolean safeAdd(Collection<T> collection, T element) {
        if (collection == null || element == null) {
            return false;
        }
        return collection.add(element);
    }

    /**
     * 合并多个集合为一个新集合
     *
     * @param collections 待合并的集合
     * @param <T> 元素类型
     * @return 合并后的新集合
     */
    @SafeVarargs
    public static <T> List<T> merge(Collection<T>... collections) {
        List<T> result = new ArrayList<>();
        if (collections == null) {
            return result;
        }
        
        for (Collection<T> collection : collections) {
            if (isNotEmpty(collection)) {
                result.addAll(collection);
            }
        }
        return result;
    }

    /**
     * 计算两个集合的交集
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T> 元素类型
     * @return 交集
     */
    public static <T> List<T> intersection(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1) || isEmpty(collection2)) {
            return new ArrayList<>();
        }
        
        List<T> result = new ArrayList<>(collection1);
        result.retainAll(collection2);
        return result;
    }

    /**
     * 计算两个集合的差集（collection1中有但collection2中没有的元素）
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @param <T> 元素类型
     * @return 差集
     */
    public static <T> List<T> difference(Collection<T> collection1, Collection<T> collection2) {
        if (isEmpty(collection1)) {
            return new ArrayList<>();
        }
        if (isEmpty(collection2)) {
            return new ArrayList<>(collection1);
        }
        
        List<T> result = new ArrayList<>(collection1);
        result.removeAll(collection2);
        return result;
    }
} 