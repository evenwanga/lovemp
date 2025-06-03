package com.lovemp.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 * 
 * 基于Jackson实现JSON序列化与反序列化的工具类，提供简便易用的JSON处理方法，支持各种复杂对象和集合类型。
 * 
 * 主要功能包括：
 * 1. 对象序列化：将Java对象转换为JSON字符串
 * 2. 对象反序列化：将JSON字符串转换为Java对象
 * 3. 集合类型处理：支持List、Map等集合类型的序列化和反序列化
 * 4. 复杂泛型处理：通过TypeReference支持复杂泛型类型的反序列化
 * 5. 对象间转换：通过JSON作为中间步骤实现不同对象间的属性复制
 * 6. 树模型操作：通过JsonNode进行灵活的JSON节点访问和操作
 * 7. 安全处理：提供异常安全的序列化方法，防止业务中断
 * 
 * 适用场景：
 * - 前后端数据交换：API请求和响应的序列化与反序列化
 * - 对象持久化：将对象序列化后存储到数据库或文件系统
 * - 缓存数据处理：Redis等缓存系统中的对象序列化和反序列化
 * - 消息队列传输：在消息队列中传输的消息体序列化
 * - 配置文件处理：读取和写入JSON格式的配置文件
 * - 复杂对象转换：在不同对象模型之间进行转换
 * 
 * 使用示例：
 * // 对象转JSON
 * String json = JsonUtils.toJson(user);
 * 
 * // JSON转对象
 * User user = JsonUtils.fromJson(json, User.class);
 * 
 * // JSON转List
 * List<User> users = JsonUtils.fromJsonToList(json, User.class);
 * 
 * // 复杂泛型反序列化
 * Map<String, List<User>> userMap = JsonUtils.fromJson(json, 
 *     new TypeReference<Map<String, List<User>>>() {});
 * 
 * 注意：
 * 1. 内部使用线程安全的ObjectMapper实例，可在高并发环境中安全使用
 * 2. 默认配置支持Java 8日期时间类型，且忽略未知属性和空值
 * 3. 提供了安全方法，发生异常时返回默认值而不是抛出异常
 */
public final class JsonUtils {

    private JsonUtils() {
        // 工具类不允许实例化
    }

    /**
     * 全局ObjectMapper实例，线程安全
     * 配置了Java 8日期时间模块支持和常用序列化选项
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            // 注册Java 8日期时间模块
            .registerModule(new JavaTimeModule())
            // 禁用日期时间作为时间戳输出
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // 忽略未知属性
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 空值不序列化
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 对象转格式化的JSON字符串（用于日志打印等）
     *
     * @param object 对象
     * @return 格式化的JSON字符串
     */
    public static String toPrettyJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON格式化序列化失败", e);
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 目标类型
     * @param <T>   目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }

    /**
     * JSON字符串转复杂对象
     * 用于处理泛型嵌套等复杂类型
     *
     * @param json          JSON字符串
     * @param typeReference 类型引用
     * @param <T>           目标类型泛型
     * @return 转换后的对象
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(json) || typeReference == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化到复杂类型失败", e);
        }
    }

    /**
     * JSON字符串转List
     *
     * @param json  JSON字符串
     * @param clazz 列表元素类型
     * @param <T>   目标类型泛型
     * @return 转换后的列表
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json) || clazz == null) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(
                    json,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化到List失败", e);
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param json      JSON字符串
     * @param keyClass   Map键类型
     * @param valueClass Map值类型
     * @param <K>       键类型泛型
     * @param <V>       值类型泛型
     * @return 转换后的Map
     */
    public static <K, V> Map<K, V> fromJsonToMap(String json, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(json) || keyClass == null || valueClass == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(
                    json,
                    OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass)
            );
        } catch (IOException e) {
            throw new RuntimeException("JSON反序列化到Map失败", e);
        }
    }

    /**
     * 对象转换为另一个对象
     * 用于不同对象间的属性复制，只要属性名相同即可
     *
     * @param source    源对象
     * @param targetClass 目标类型
     * @param <T>       目标类型泛型
     * @return 目标对象实例
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null || targetClass == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.convertValue(source, targetClass);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("对象转换失败", e);
        }
    }

    /**
     * 获取Jackson的ObjectMapper实例
     * 如果需要自定义配置，可以使用此方法获取实例后进行配置
     *
     * @return ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 解析JSON字符串为JsonNode
     *
     * @param json JSON字符串
     * @return JsonNode对象
     */
    public static JsonNode parseJsonNode(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("解析JSON为JsonNode失败", e);
        }
    }

    /**
     * 从JsonNode中获取指定路径的值
     *
     * @param jsonNode JsonNode对象
     * @param path     路径表达式，例如"user.address.city"
     * @return 指定路径的值，如果路径不存在返回null
     */
    public static String getValueFromPath(JsonNode jsonNode, String path) {
        if (jsonNode == null || StringUtils.isEmpty(path)) {
            return null;
        }

        String[] pathSegments = path.split("\\.");
        JsonNode currentNode = jsonNode;

        for (String segment : pathSegments) {
            currentNode = currentNode.path(segment);
            if (currentNode.isMissingNode() || currentNode.isNull()) {
                return null;
            }
        }

        return currentNode.asText();
    }

    /**
     * 安全地将对象转换为JSON字符串，出错时返回默认值而不抛出异常
     *
     * @param object       要转换的对象
     * @param defaultValue 转换失败时的默认值
     * @return JSON字符串或默认值
     */
    public static String toJsonSafe(Object object, String defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return defaultValue;
        }
    }
} 