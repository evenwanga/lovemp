package com.lovemp.domain.labor.domain.model.valueobject;

import com.lovemp.common.domain.Identifier;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * 劳动力资源ID - 值对象
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class LaborResourceId extends Identifier<String> {
    
    private LaborResourceId(String value) {
        super(value);
    }
    
    /**
     * 创建新的劳动力资源ID
     *
     * @return 新的劳动力资源ID
     */
    public static LaborResourceId generate() {
        return new LaborResourceId(UUID.randomUUID().toString());
    }
    
    /**
     * 根据现有ID创建劳动力资源ID
     *
     * @param id ID值
     * @return 劳动力资源ID
     */
    public static LaborResourceId of(String id) {
        return new LaborResourceId(id);
    }
    
    @Override
    public String toString() {
        return getValue();
    }
}