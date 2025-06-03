package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 姓名值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Name implements ValueObject {
    
    /**
     * 原始姓名（可能包含生僻字）
     */
    private String originalName;
    
    /**
     * 处理后的姓名（用于显示、排序等）
     */
    private String processedName;
    
    /**
     * 拼音
     */
    private String pinyin;
    
    /**
     * 创建姓名值对象
     * 
     * @param originalName 原始姓名
     * @return 姓名值对象（处理后的姓名和拼音为null）
     */
    public static Name of(String originalName) {
        return new Name(originalName, null, null);
    }
    
    /**
     * 创建完整姓名值对象
     * 
     * @param originalName 原始姓名
     * @param processedName 处理后的姓名
     * @param pinyin 拼音
     * @return 姓名值对象
     */
    public static Name of(String originalName, String processedName, String pinyin) {
        return new Name(originalName, processedName, pinyin);
    }
    
    /**
     * 更新处理后的姓名
     * 
     * @param processedName 处理后的姓名
     * @return 更新后的姓名值对象
     */
    public Name withProcessedName(String processedName) {
        return new Name(this.originalName, processedName, this.pinyin);
    }
    
    /**
     * 更新拼音
     * 
     * @param pinyin 拼音
     * @return 更新后的姓名值对象
     */
    public Name withPinyin(String pinyin) {
        return new Name(this.originalName, this.processedName, pinyin);
    }
    
    /**
     * 获取显示用姓名
     * 如果处理后的姓名存在则返回处理后的姓名，否则返回原始姓名
     * 
     * @return 显示用姓名
     */
    public String getDisplayName() {
        return processedName != null ? processedName : originalName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(originalName, name.originalName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(originalName);
    }
    
    @Override
    public String toString() {
        return originalName;
    }
}