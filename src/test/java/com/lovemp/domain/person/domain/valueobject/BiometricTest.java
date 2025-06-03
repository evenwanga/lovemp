package com.lovemp.domain.person.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("生物特征值对象测试")
class BiometricTest {
    
    @Test
    @DisplayName("测试创建空的生物特征对象")
    void testCreateEmpty() {
        // 创建空的生物特征对象
        Biometric biometric = Biometric.empty();
        
        // 验证结果
        assertTrue(biometric.isEmpty());
        assertEquals(0, biometric.getFingerprints().size());
        assertEquals(0, biometric.getIrises().size());
        assertNull(biometric.getFaceFeature());
        assertNull(biometric.getVoicePrint());
    }
    
    @Test
    @DisplayName("测试创建基本的生物特征对象")
    void testCreateBasic() {
        // 创建基本的生物特征对象
        Biometric biometric = Biometric.of(
                "指纹数据1",
                "人脸特征数据",
                "虹膜数据1",
                "声纹数据"
        );
        
        // 验证结果
        assertFalse(biometric.isEmpty());
        assertEquals(1, biometric.getFingerprints().size());
        assertEquals("指纹数据1", biometric.getFingerprints().get(0));
        assertEquals(1, biometric.getIrises().size());
        assertEquals("虹膜数据1", biometric.getIrises().get(0));
        assertEquals("人脸特征数据", biometric.getFaceFeature());
        assertEquals("声纹数据", biometric.getVoicePrint());
    }
    
    @Test
    @DisplayName("测试创建带有多个指纹和虹膜特征的生物特征对象")
    void testCreateWithMultipleFingerprints() {
        // 准备测试数据
        List<String> fingerprints = Arrays.asList("左手拇指", "左手食指", "右手拇指");
        List<String> irises = Arrays.asList("左眼虹膜", "右眼虹膜");
        
        // 创建生物特征对象
        Biometric biometric = Biometric.of(
                fingerprints,
                "人脸特征数据",
                irises,
                "声纹数据"
        );
        
        // 验证结果
        assertFalse(biometric.isEmpty());
        assertEquals(3, biometric.getFingerprints().size());
        assertEquals("左手拇指", biometric.getFingerprints().get(0));
        assertEquals("左手食指", biometric.getFingerprints().get(1));
        assertEquals("右手拇指", biometric.getFingerprints().get(2));
        
        assertEquals(2, biometric.getIrises().size());
        assertEquals("左眼虹膜", biometric.getIrises().get(0));
        assertEquals("右眼虹膜", biometric.getIrises().get(1));
        
        assertEquals("人脸特征数据", biometric.getFaceFeature());
        assertEquals("声纹数据", biometric.getVoicePrint());
    }
    
    @Test
    @DisplayName("测试添加指纹特征")
    void testAddFingerprint() {
        // 创建空的生物特征对象
        Biometric biometric = Biometric.empty();
        
        // 添加第一个指纹
        Biometric updatedBiometric = biometric.addFingerprint("左手拇指");
        
        // 验证结果
        assertEquals(1, updatedBiometric.getFingerprints().size());
        assertEquals("左手拇指", updatedBiometric.getFingerprints().get(0));
        
        // 添加第二个指纹
        Biometric finalBiometric = updatedBiometric.addFingerprint("右手拇指");
        
        // 验证结果
        assertEquals(2, finalBiometric.getFingerprints().size());
        assertEquals("左手拇指", finalBiometric.getFingerprints().get(0));
        assertEquals("右手拇指", finalBiometric.getFingerprints().get(1));
        
        // 验证不可变性
        assertEquals(1, updatedBiometric.getFingerprints().size());
        assertEquals(0, biometric.getFingerprints().size());
    }
    
    @Test
    @DisplayName("测试添加虹膜特征")
    void testAddIris() {
        // 创建空的生物特征对象
        Biometric biometric = Biometric.empty();
        
        // 添加第一个虹膜特征
        Biometric updatedBiometric = biometric.addIris("左眼虹膜");
        
        // 验证结果
        assertEquals(1, updatedBiometric.getIrises().size());
        assertEquals("左眼虹膜", updatedBiometric.getIrises().get(0));
        
        // 添加第二个虹膜特征
        Biometric finalBiometric = updatedBiometric.addIris("右眼虹膜");
        
        // 验证结果
        assertEquals(2, finalBiometric.getIrises().size());
        assertEquals("左眼虹膜", finalBiometric.getIrises().get(0));
        assertEquals("右眼虹膜", finalBiometric.getIrises().get(1));
        
        // 验证不可变性
        assertEquals(1, updatedBiometric.getIrises().size());
        assertEquals(0, biometric.getIrises().size());
    }
    
    @Test
    @DisplayName("测试更新人脸特征")
    void testUpdateFaceFeature() {
        // 创建空的生物特征对象
        Biometric biometric = Biometric.empty();
        
        // 更新人脸特征
        Biometric updatedBiometric = biometric.withFaceFeature("人脸特征数据");
        
        // 验证结果
        assertNull(biometric.getFaceFeature());
        assertEquals("人脸特征数据", updatedBiometric.getFaceFeature());
        
        // 再次更新人脸特征
        Biometric finalBiometric = updatedBiometric.withFaceFeature("新的人脸特征数据");
        
        // 验证结果
        assertEquals("人脸特征数据", updatedBiometric.getFaceFeature());
        assertEquals("新的人脸特征数据", finalBiometric.getFaceFeature());
    }
    
    @Test
    @DisplayName("测试批量设置指纹和虹膜特征")
    void testBatchSetFingerprints() {
        // 创建初始生物特征对象
        Biometric biometric = Biometric.of(
                "初始指纹",
                "初始人脸特征",
                "初始虹膜特征",
                null
        );
        
        // 准备批量替换的特征数据
        List<String> newFingerprints = Arrays.asList("新指纹1", "新指纹2", "新指纹3");
        List<String> newIrises = Arrays.asList("新虹膜1", "新虹膜2");
        
        // 批量更新指纹特征
        Biometric withNewFingerprints = biometric.withFingerprints(newFingerprints);
        
        // 验证结果
        assertEquals(3, withNewFingerprints.getFingerprints().size());
        assertEquals("新指纹1", withNewFingerprints.getFingerprints().get(0));
        assertEquals("新指纹2", withNewFingerprints.getFingerprints().get(1));
        assertEquals("新指纹3", withNewFingerprints.getFingerprints().get(2));
        // 其他属性应保持不变
        assertEquals("初始人脸特征", withNewFingerprints.getFaceFeature());
        assertEquals(1, withNewFingerprints.getIrises().size());
        
        // 批量更新虹膜特征
        Biometric withBothUpdated = withNewFingerprints.withIrises(newIrises);
        
        // 验证结果
        assertEquals(3, withBothUpdated.getFingerprints().size());
        assertEquals(2, withBothUpdated.getIrises().size());
        assertEquals("新虹膜1", withBothUpdated.getIrises().get(0));
        assertEquals("新虹膜2", withBothUpdated.getIrises().get(1));
    }
    
    @Test
    @DisplayName("测试返回集合的不可变性")
    void testReturnedCollectionImmutability() {
        // 准备测试数据并创建生物特征对象
        Biometric biometric = Biometric.of(
                "指纹数据1",
                "人脸特征数据",
                "虹膜数据1",
                "声纹数据"
        );
        
        // 验证尝试修改返回的集合会抛出异常
        List<String> fingerprintsList = biometric.getFingerprints();
        List<String> irisesList = biometric.getIrises();
        
        // 测试add方法
        assertThrows(UnsupportedOperationException.class, () -> {
            fingerprintsList.add("新指纹");
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            irisesList.add("新虹膜");
        });
        
        // 测试remove方法
        assertThrows(UnsupportedOperationException.class, () -> {
            fingerprintsList.remove(0);
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            irisesList.remove(0);
        });
        
        // 测试clear方法
        assertThrows(UnsupportedOperationException.class, () -> {
            fingerprintsList.clear();
        });
        
        assertThrows(UnsupportedOperationException.class, () -> {
            irisesList.clear();
        });
    }
    
    @Test
    @DisplayName("测试原始集合修改不影响Biometric内部集合")
    void testOriginalCollectionModificationIsolation() {
        // 准备测试数据
        List<String> fingerprints = new ArrayList<>();
        fingerprints.add("指纹1");
        fingerprints.add("指纹2");
        
        List<String> irises = new ArrayList<>();
        irises.add("虹膜1");
        irises.add("虹膜2");
        
        // 创建生物特征对象
        Biometric biometric = Biometric.of(
                fingerprints,
                "人脸特征",
                irises,
                "声纹特征"
        );
        
        // 修改原始集合不应影响对象内的集合
        fingerprints.add("新指纹");
        irises.add("新虹膜");
        
        assertEquals(2, biometric.getFingerprints().size());
        assertEquals(2, biometric.getIrises().size());
        
        // 验证原始集合变更不影响Biometric内的集合内容
        assertEquals("指纹1", biometric.getFingerprints().get(0));
        assertEquals("指纹2", biometric.getFingerprints().get(1));
        assertEquals("虹膜1", biometric.getIrises().get(0));
        assertEquals("虹膜2", biometric.getIrises().get(1));
    }
} 