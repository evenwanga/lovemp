package com.lovemp.domain.labor.domain.model.valueobject;

import com.lovemp.common.domain.Identifier;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

/**
 * 雇佣事件ID - 值对象
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class EmploymentEventId extends Identifier<String> {
    
    private EmploymentEventId(String value) {
        super(value);
    }
    
    /**
     * 创建新的雇佣事件ID
     *
     * @return 新的雇佣事件ID
     */
    public static EmploymentEventId generate() {
        return new EmploymentEventId(UUID.randomUUID().toString());
    }
    
    /**
     * 根据现有ID创建雇佣事件ID
     *
     * @param id ID值
     * @return 雇佣事件ID
     */
    public static EmploymentEventId of(String id) {
        return new EmploymentEventId(id);
    }
    
    @Override
    public String toString() {
        return getValue();
    }
}