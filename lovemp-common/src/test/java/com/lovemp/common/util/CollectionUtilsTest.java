package com.lovemp.common.util;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CollectionUtils工具类的单元测试
 * 
 * 该测试类用于验证CollectionUtils工具类中各种集合操作方法的正确性,包括:
 * - isEmpty(): 验证集合是否为空
 * - isNotEmpty(): 验证集合是否非空
 * - toList(): 将集合转换为List
 * - toSet(): 将集合转换为Set
 * - toMap(): 将集合转换为Map
 * - groupBy(): 集合分组
 * - filter(): 集合过滤
 * - map(): 集合映射转换
 * 
 * 每个测试用例都会验证:
 * 1. 正常情况 - 集合操作的正确性
 * 2. 边界情况 - 空集合、null值的处理
 * 3. 类型转换的正确性
 * 4. 复杂对象的处理
 * 
 * @see com.lovemp.common.util.CollectionUtils
 */
class CollectionUtilsTest {

    // 测试用内部类
    static class TestUser {
        private String id;
        private String name;
        private String role;
        private boolean active;

        public TestUser(String id, String name, String role, boolean active) {
            this.id = id;
            this.name = name;
            this.role = role;
            this.active = active;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getRole() {
            return role;
        }

        public boolean isActive() {
            return active;
        }
    }

    @Test
    void isEmpty() {
        // 测试集合为空判断
        assertTrue(CollectionUtils.isEmpty((Collection<?>) null));
        assertTrue(CollectionUtils.isEmpty(new ArrayList<>()));
        
        List<String> list = Arrays.asList("a", "b", "c");
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    void isNotEmpty() {
        // 测试集合不为空判断
        assertFalse(CollectionUtils.isNotEmpty((Collection<?>) null));
        assertFalse(CollectionUtils.isNotEmpty(new ArrayList<>()));
        
        List<String> list = Arrays.asList("a", "b", "c");
        assertTrue(CollectionUtils.isNotEmpty(list));
    }

    @Test
    void isEmptyMap() {
        // 测试Map为空判断
        assertTrue(CollectionUtils.isEmpty((Map<?, ?>) null));
        assertTrue(CollectionUtils.isEmpty(new HashMap<>()));
        
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertFalse(CollectionUtils.isEmpty(map));
    }

    @Test
    void isNotEmptyMap() {
        // 测试Map不为空判断
        assertFalse(CollectionUtils.isNotEmpty((Map<?, ?>) null));
        assertFalse(CollectionUtils.isNotEmpty(new HashMap<>()));
        
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertTrue(CollectionUtils.isNotEmpty(map));
    }

    @Test
    void filter() {
        // 创建测试数据
        List<TestUser> users = Arrays.asList(
            new TestUser("1", "张三", "admin", true),
            new TestUser("2", "李四", "user", true),
            new TestUser("3", "王五", "admin", false),
            new TestUser("4", "赵六", "user", false)
        );
        
        // 测试过滤集合
        List<TestUser> activeUsers = CollectionUtils.filter(users, TestUser::isActive);
        assertEquals(2, activeUsers.size());
        assertEquals("张三", activeUsers.get(0).getName());
        assertEquals("李四", activeUsers.get(1).getName());
        
        // 测试组合过滤条件
        List<TestUser> activeAdmins = CollectionUtils.filter(users, 
                user -> user.isActive() && "admin".equals(user.getRole()));
        assertEquals(1, activeAdmins.size());
        assertEquals("张三", activeAdmins.get(0).getName());
        
        // 测试空集合过滤
        List<TestUser> emptyResult = CollectionUtils.filter(null, TestUser::isActive);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void firstOrNull() {
        // 创建测试数据
        List<String> list = Arrays.asList("a", "b", "c");
        
        // 测试获取第一个元素
        String first = CollectionUtils.firstOrNull(list);
        assertEquals("a", first);
        
        // 测试空集合
        assertNull(CollectionUtils.firstOrNull(new ArrayList<>()));
        assertNull(CollectionUtils.firstOrNull(null));
    }

    @Test
    void findFirst() {
        // 创建测试数据
        List<TestUser> users = Arrays.asList(
            new TestUser("1", "张三", "admin", true),
            new TestUser("2", "李四", "user", true),
            new TestUser("3", "王五", "admin", false)
        );
        
        // 测试查找第一个符合条件的元素
        TestUser admin = CollectionUtils.findFirst(users, 
                user -> "admin".equals(user.getRole()));
        assertNotNull(admin);
        assertEquals("张三", admin.getName());
        
        // 测试未找到符合条件的元素
        TestUser notFound = CollectionUtils.findFirst(users, 
                user -> "guest".equals(user.getRole()));
        assertNull(notFound);
        
        // 测试空集合
        assertNull(CollectionUtils.findFirst(null, user -> true));
    }

    @Test
    void toMap() {
        // 创建测试数据
        List<TestUser> users = Arrays.asList(
            new TestUser("1", "张三", "admin", true),
            new TestUser("2", "李四", "user", true),
            new TestUser("3", "王五", "admin", false)
        );
        
        // 测试转换为Map（使用ID作为key）
        Map<String, TestUser> userMap = CollectionUtils.toMap(users, TestUser::getId);
        assertEquals(3, userMap.size());
        assertEquals("张三", userMap.get("1").getName());
        assertEquals("李四", userMap.get("2").getName());
        assertEquals("王五", userMap.get("3").getName());
        
        // 测试转换为Map（使用ID作为key，名称作为value）
        Map<String, String> nameMap = CollectionUtils.toMap(
                users, TestUser::getId, TestUser::getName);
        assertEquals(3, nameMap.size());
        assertEquals("张三", nameMap.get("1"));
        assertEquals("李四", nameMap.get("2"));
        assertEquals("王五", nameMap.get("3"));
        
        // 测试空集合
        Map<String, TestUser> emptyMap = CollectionUtils.toMap(null, TestUser::getId);
        assertTrue(emptyMap.isEmpty());
    }

    @Test
    void groupBy() {
        // 创建测试数据
        List<TestUser> users = Arrays.asList(
            new TestUser("1", "张三", "admin", true),
            new TestUser("2", "李四", "user", true),
            new TestUser("3", "王五", "admin", false),
            new TestUser("4", "赵六", "user", false)
        );
        
        // 测试按角色分组
        Map<String, List<TestUser>> usersByRole = CollectionUtils.groupBy(users, TestUser::getRole);
        assertEquals(2, usersByRole.size());
        assertEquals(2, usersByRole.get("admin").size());
        assertEquals(2, usersByRole.get("user").size());
        assertEquals("张三", usersByRole.get("admin").get(0).getName());
        assertEquals("王五", usersByRole.get("admin").get(1).getName());
        
        // 测试按活跃状态分组
        Map<Boolean, List<TestUser>> usersByActive = CollectionUtils.groupBy(users, TestUser::isActive);
        assertEquals(2, usersByActive.size());
        assertEquals(2, usersByActive.get(true).size());
        assertEquals(2, usersByActive.get(false).size());
        
        // 测试空集合
        Map<String, List<TestUser>> emptyMap = CollectionUtils.groupBy(null, TestUser::getRole);
        assertTrue(emptyMap.isEmpty());
    }

    @Test
    void map() {
        // 创建测试数据
        List<TestUser> users = Arrays.asList(
            new TestUser("1", "张三", "admin", true),
            new TestUser("2", "李四", "user", true)
        );
        
        // 测试映射集合元素
        List<String> names = CollectionUtils.map(users, TestUser::getName);
        assertEquals(2, names.size());
        assertEquals("张三", names.get(0));
        assertEquals("李四", names.get(1));
        
        // 测试复杂映射
        List<String> fullInfos = CollectionUtils.map(users, 
                user -> user.getName() + "(" + user.getRole() + ")");
        assertEquals(2, fullInfos.size());
        assertEquals("张三(admin)", fullInfos.get(0));
        assertEquals("李四(user)", fullInfos.get(1));
        
        // 测试空集合
        List<String> emptyResult = CollectionUtils.map(null, TestUser::getName);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void safeAdd() {
        // 创建测试集合
        List<String> list = new ArrayList<>();
        
        // 测试安全添加元素
        assertTrue(CollectionUtils.safeAdd(list, "a"));
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));
        
        // 测试添加null元素
        assertFalse(CollectionUtils.safeAdd(list, null));
        assertEquals(1, list.size());
        
        // 测试向null集合添加元素
        assertFalse(CollectionUtils.safeAdd(null, "a"));
    }

    @Test
    void merge() {
        // 创建测试集合
        List<String> list1 = Arrays.asList("a", "b");
        List<String> list2 = Arrays.asList("c", "d");
        List<String> list3 = Arrays.asList("e", "f");
        
        // 测试合并集合
        List<String> merged = CollectionUtils.merge(list1, list2, list3);
        assertEquals(6, merged.size());
        assertEquals("a", merged.get(0));
        assertEquals("b", merged.get(1));
        assertEquals("c", merged.get(2));
        assertEquals("d", merged.get(3));
        assertEquals("e", merged.get(4));
        assertEquals("f", merged.get(5));
        
        // 测试包含null集合的合并
        List<String> mergedWithNull = CollectionUtils.merge(list1, null, list3);
        assertEquals(4, mergedWithNull.size());
        
        // 测试null参数
        List<String> emptyResult = CollectionUtils.merge((Collection<String>[]) null);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void intersection() {
        // 创建测试集合
        List<String> list1 = Arrays.asList("a", "b", "c", "d");
        List<String> list2 = Arrays.asList("c", "d", "e", "f");
        
        // 测试计算交集
        List<String> intersection = CollectionUtils.intersection(list1, list2);
        assertEquals(2, intersection.size());
        assertTrue(intersection.contains("c"));
        assertTrue(intersection.contains("d"));
        
        // 测试空集合参数
        assertTrue(CollectionUtils.intersection(null, list2).isEmpty());
        assertTrue(CollectionUtils.intersection(list1, null).isEmpty());
        assertTrue(CollectionUtils.intersection(null, null).isEmpty());
    }

    @Test
    void difference() {
        // 创建测试集合
        List<String> list1 = Arrays.asList("a", "b", "c", "d");
        List<String> list2 = Arrays.asList("c", "d", "e", "f");
        
        // 测试计算差集 (list1 - list2)
        List<String> difference = CollectionUtils.difference(list1, list2);
        assertEquals(2, difference.size());
        assertTrue(difference.contains("a"));
        assertTrue(difference.contains("b"));
        
        // 测试空集合参数
        assertTrue(CollectionUtils.difference(null, list2).isEmpty());
        assertEquals(list1.size(), CollectionUtils.difference(list1, null).size());
    }
} 