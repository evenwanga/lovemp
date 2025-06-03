package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 生物特征值对象
 * 用于存储人员的生物识别特征数据
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Biometric implements ValueObject {
    
    /**
     * 指纹特征数据列表
     */
    private List<String> fingerprints;
    
    /**
     * 人脸特征数据
     */
    private String faceFeature;
    
    /**
     * 虹膜特征数据列表（左眼、右眼）
     */
    private List<String> irises;
    
    /**
     * 声纹特征数据
     */
    private String voicePrint;
    
    /**
     * 创建生物特征值对象
     * 
     * @param fingerprints 指纹特征列表
     * @param faceFeature 人脸特征
     * @param irises 虹膜特征列表
     * @param voicePrint 声纹特征
     * @return 生物特征值对象
     */
    public static Biometric of(List<String> fingerprints, String faceFeature, List<String> irises, String voicePrint) {
        return new Biometric(
            fingerprints != null ? new ArrayList<>(fingerprints) : new ArrayList<>(),
            faceFeature,
            irises != null ? new ArrayList<>(irises) : new ArrayList<>(),
            voicePrint
        );
    }
    
    /**
     * 创建生物特征值对象（简化版）
     * 
     * @param fingerprint 单个指纹特征
     * @param faceFeature 人脸特征
     * @param iris 单个虹膜特征
     * @param voicePrint 声纹特征
     * @return 生物特征值对象
     */
    public static Biometric of(String fingerprint, String faceFeature, String iris, String voicePrint) {
        List<String> fingerprints = new ArrayList<>();
        if (fingerprint != null) {
            fingerprints.add(fingerprint);
        }
        
        List<String> irises = new ArrayList<>();
        if (iris != null) {
            irises.add(iris);
        }
        
        return new Biometric(fingerprints, faceFeature, irises, voicePrint);
    }
    
    /**
     * 创建空的生物特征值对象
     * 
     * @return 空的生物特征值对象
     */
    public static Biometric empty() {
        return new Biometric(new ArrayList<>(), null, new ArrayList<>(), null);
    }
    
    /**
     * 判断是否为空的生物特征
     * 
     * @return 如果所有特征均为空，则返回true
     */
    public boolean isEmpty() {
        return (fingerprints == null || fingerprints.isEmpty()) && 
               faceFeature == null && 
               (irises == null || irises.isEmpty()) && 
               voicePrint == null;
    }
    
    /**
     * 添加指纹特征
     * 
     * @param fingerprint 新的指纹特征
     * @return 更新后的生物特征对象
     */
    public Biometric addFingerprint(String fingerprint) {
        if (fingerprint == null) {
            return this;
        }
        
        List<String> newFingerprints = new ArrayList<>(this.fingerprints);
        newFingerprints.add(fingerprint);
        return new Biometric(newFingerprints, this.faceFeature, this.irises, this.voicePrint);
    }
    
    /**
     * 设置指纹特征列表
     * 
     * @param fingerprints 新的指纹特征列表
     * @return 更新后的生物特征对象
     */
    public Biometric withFingerprints(List<String> fingerprints) {
        return new Biometric(
            fingerprints != null ? new ArrayList<>(fingerprints) : new ArrayList<>(),
            this.faceFeature,
            this.irises,
            this.voicePrint
        );
    }
    
    /**
     * 更新人脸特征
     * 
     * @param faceFeature 新的人脸特征
     * @return 更新后的生物特征对象
     */
    public Biometric withFaceFeature(String faceFeature) {
        return new Biometric(this.fingerprints, faceFeature, this.irises, this.voicePrint);
    }
    
    /**
     * 添加虹膜特征
     * 
     * @param iris 新的虹膜特征
     * @return 更新后的生物特征对象
     */
    public Biometric addIris(String iris) {
        if (iris == null) {
            return this;
        }
        
        List<String> newIrises = new ArrayList<>(this.irises);
        newIrises.add(iris);
        return new Biometric(this.fingerprints, this.faceFeature, newIrises, this.voicePrint);
    }
    
    /**
     * 设置虹膜特征列表
     * 
     * @param irises 新的虹膜特征列表
     * @return 更新后的生物特征对象
     */
    public Biometric withIrises(List<String> irises) {
        return new Biometric(
            this.fingerprints,
            this.faceFeature,
            irises != null ? new ArrayList<>(irises) : new ArrayList<>(),
            this.voicePrint
        );
    }
    
    /**
     * 更新声纹特征
     * 
     * @param voicePrint 新的声纹特征
     * @return 更新后的生物特征对象
     */
    public Biometric withVoicePrint(String voicePrint) {
        return new Biometric(this.fingerprints, this.faceFeature, this.irises, voicePrint);
    }
    
    /**
     * 获取指纹特征列表（不可修改）
     * 
     * @return 指纹特征列表的不可修改视图
     */
    public List<String> getFingerprints() {
        return Collections.unmodifiableList(fingerprints);
    }
    
    /**
     * 获取虹膜特征列表（不可修改）
     * 
     * @return 虹膜特征列表的不可修改视图
     */
    public List<String> getIrises() {
        return Collections.unmodifiableList(irises);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Biometric biometric = (Biometric) o;
        return Objects.equals(fingerprints, biometric.fingerprints) &&
               Objects.equals(faceFeature, biometric.faceFeature) &&
               Objects.equals(irises, biometric.irises) &&
               Objects.equals(voicePrint, biometric.voicePrint);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(fingerprints, faceFeature, irises, voicePrint);
    }
}