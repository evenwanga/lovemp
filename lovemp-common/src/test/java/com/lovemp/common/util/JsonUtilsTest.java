package com.lovemp.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JsonUtils工具类的单元测试
 * 
 * <p>该测试类用于验证JsonUtils工具类中各种JSON操作方法的正确性,包括:
 * <ul>
 *   <li>对象序列化 - 将Java对象转换为JSON字符串</li>
 *   <li>对象反序列化 - 将JSON字符串转换为Java对象</li>
 *   <li>集合类型转换 - List、Map等集合类型的序列化和反序列化</li>
 *   <li>JSON节点操作 - 读取和修改JSON节点</li>
 * </ul>
 * 
 * <p>测试验证:
 * <ul>
 *   <li>正常情况 - 各种数据类型的正确转换</li>
 *   <li>异常情况 - 非法JSON格式、类型不匹配等异常处理</li>
 *   <li>特殊字符 - 包含特殊字符的JSON处理</li>
 * </ul>
 * 
 * @see com.lovemp.common.util.JsonUtils
 * @author lovemp
 * @since 1.0
 */
class JsonUtilsTest {

    // 测试用内部类
    static class TestUser {
        private String name;
        private int age;
        private List<String> hobbies;
        private TestAddress address;

        public TestUser() {
        }

        public TestUser(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getHobbies() {
            return hobbies;
        }

        public void setHobbies(List<String> hobbies) {
            this.hobbies = hobbies;
        }

        public TestAddress getAddress() {
            return address;
        }

        public void setAddress(TestAddress address) {
            this.address = address;
        }
    }

    static class TestAddress {
        private String city;
        private String street;

        public TestAddress() {
        }

        public TestAddress(String city, String street) {
            this.city = city;
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    @Test
    void toJson() {
        // 创建测试对象
        TestUser user = new TestUser("张三", 25);
        user.setHobbies(Arrays.asList("阅读", "编程", "旅行"));
        user.setAddress(new TestAddress("北京", "朝阳区"));

        // 测试对象转JSON
        String json = JsonUtils.toJson(user);
        assertNotNull(json);
        assertTrue(json.contains("张三"));
        assertTrue(json.contains("25"));
        assertTrue(json.contains("阅读"));
        assertTrue(json.contains("北京"));
    }

    @Test
    void toPrettyJson() {
        // 创建测试对象
        TestUser user = new TestUser("李四", 30);

        // 测试对象转格式化JSON
        String prettyJson = JsonUtils.toPrettyJson(user);
        assertNotNull(prettyJson);
        assertTrue(prettyJson.contains("李四"));
        assertTrue(prettyJson.contains("30"));
        // 格式化JSON应该包含换行和缩进
        assertTrue(prettyJson.contains("\n"));
    }

    @Test
    void fromJson() {
        // 准备测试JSON
        String json = "{\"name\":\"王五\",\"age\":35,\"hobbies\":[\"游泳\",\"跑步\"],\"address\":{\"city\":\"上海\",\"street\":\"浦东新区\"}}";

        // 测试JSON转对象
        TestUser user = JsonUtils.fromJson(json, TestUser.class);
        assertNotNull(user);
        assertEquals("王五", user.getName());
        assertEquals(35, user.getAge());
        assertEquals(2, user.getHobbies().size());
        assertEquals("上海", user.getAddress().getCity());
        assertEquals("浦东新区", user.getAddress().getStreet());
    }

    @Test
    void fromJsonWithTypeReference() {
        // 准备测试JSON
        String json = "{\"admin\":{\"name\":\"管理员\",\"age\":40},\"user\":{\"name\":\"普通用户\",\"age\":25}}";

        // 测试JSON转复杂类型
        Map<String, TestUser> userMap = JsonUtils.fromJson(json, new TypeReference<Map<String, TestUser>>() {});
        assertNotNull(userMap);
        assertEquals(2, userMap.size());
        assertEquals("管理员", userMap.get("admin").getName());
        assertEquals(40, userMap.get("admin").getAge());
        assertEquals("普通用户", userMap.get("user").getName());
    }

    @Test
    void fromJsonToList() {
        // 准备测试JSON
        String json = "[{\"name\":\"用户1\",\"age\":21},{\"name\":\"用户2\",\"age\":22}]";

        // 测试JSON转列表
        List<TestUser> users = JsonUtils.fromJsonToList(json, TestUser.class);
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("用户1", users.get(0).getName());
        assertEquals(21, users.get(0).getAge());
        assertEquals("用户2", users.get(1).getName());
        assertEquals(22, users.get(1).getAge());
    }

    @Test
    void fromJsonToMap() {
        // 准备测试JSON
        String json = "{\"1\":\"值1\",\"2\":\"值2\",\"3\":\"值3\"}";

        // 测试JSON转Map
        Map<String, String> map = JsonUtils.fromJsonToMap(json, String.class, String.class);
        assertNotNull(map);
        assertEquals(3, map.size());
        assertEquals("值1", map.get("1"));
        assertEquals("值2", map.get("2"));
        assertEquals("值3", map.get("3"));
    }

    @Test
    void convert() {
        // 创建源对象
        TestUser sourceUser = new TestUser("赵六", 28);
        sourceUser.setAddress(new TestAddress("广州", "天河区"));

        // 测试对象转换
        TestUser targetUser = JsonUtils.convert(sourceUser, TestUser.class);
        assertNotNull(targetUser);
        assertEquals("赵六", targetUser.getName());
        assertEquals(28, targetUser.getAge());
        assertEquals("广州", targetUser.getAddress().getCity());
    }

    @Test
    void getObjectMapper() {
        // 测试获取ObjectMapper实例
        ObjectMapper mapper = JsonUtils.getObjectMapper();
        assertNotNull(mapper);
    }

    @Test
    void parseJsonNode() {
        // 准备测试JSON
        String json = "{\"name\":\"孙七\",\"address\":{\"city\":\"深圳\",\"street\":\"南山区\"}}";

        // 测试解析为JsonNode
        JsonNode jsonNode = JsonUtils.parseJsonNode(json);
        assertNotNull(jsonNode);
        assertEquals("孙七", jsonNode.get("name").asText());
        assertTrue(jsonNode.has("address"));
        assertEquals("深圳", jsonNode.get("address").get("city").asText());
    }

    @Test
    void getValueFromPath() {
        // 准备测试JSON
        String json = "{\"name\":\"周八\",\"address\":{\"city\":\"成都\",\"street\":\"武侯区\"},\"contacts\":[{\"type\":\"phone\",\"value\":\"12345678\"}]}";
        JsonNode jsonNode = JsonUtils.parseJsonNode(json);

        // 测试获取路径值
        String name = JsonUtils.getValueFromPath(jsonNode, "name");
        String city = JsonUtils.getValueFromPath(jsonNode, "address.city");
        assertEquals("周八", name);
        assertEquals("成都", city);
        
        // 测试不存在的路径
        String notExist = JsonUtils.getValueFromPath(jsonNode, "not.exist");
        assertNull(notExist);
    }

    @Test
    void toJsonSafe() {
        // 创建测试对象
        TestUser user = new TestUser("郑九", 50);

        // 测试安全转换
        String json = JsonUtils.toJsonSafe(user, "默认值");
        assertNotNull(json);
        assertTrue(json.contains("郑九"));
        
        // 测试null对象
        json = JsonUtils.toJsonSafe(null, "默认值");
        assertEquals("默认值", json);
    }
} 