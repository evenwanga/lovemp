package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.Identifier;

/**
 * 自然人标识符值对象
 */
public class PersonId extends Identifier<String> {
    
    private PersonId(String id) {
        super(id);
    }
    
    /**
     * 创建自然人标识符
     * 
     * @param id 标识符字符串
     * @return 自然人标识符对象
     */
    public static PersonId of(String id) {
        return new PersonId(id);
    }
}