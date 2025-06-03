package com.lovemp.domain.person.domain;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.person.domain.event.PersonCreatedEvent;
import com.lovemp.domain.person.domain.event.PersonStatusChangedEvent;
import com.lovemp.domain.person.domain.event.PersonUpdatedEvent;
import com.lovemp.domain.person.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("自然人聚合根测试")
class PersonTest {

    // ... 保留原有测试方法 ...
    
    @Test
    @DisplayName("测试更新生僻字姓名")
    void testUpdateNameWithRareCharacter() {
        // 准备测试数据
        Person person = createTestPerson();
        String originalName = person.getName();
        
        // 执行测试 - 更新为带有生僻字的姓名
        String processedName = "赵(木占)";
        String rareCharacterName = "赵𬊤";
        String pinyin = "ZHAO ZHAN";
        person.updateNameWithRareCharacter(processedName, rareCharacterName, pinyin);
        
        // 验证结果
        assertEquals(processedName, person.getName()); // name字段应存储处理后的姓名
        assertTrue(person.isRareCharacter()); // 生僻字标志应为true
        assertNotNull(person.getOriginalName()); // 原始姓名对象不应为空
        
        // 验证原始姓名对象的字段
        assertEquals(rareCharacterName, person.getOriginalName().getOriginalName());
        assertEquals(processedName, person.getOriginalName().getProcessedName());
        assertEquals(pinyin, person.getOriginalName().getPinyin());
        
        // 验证getDisplayName和getFullName方法
        assertEquals(processedName, person.getDisplayName()); // 显示用姓名应为处理后的姓名
        assertEquals(rareCharacterName, person.getFullName()); // 完整姓名应为原始姓名（带生僻字）
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertTrue(domainEvents.get(1) instanceof PersonUpdatedEvent);
        
        PersonUpdatedEvent event = (PersonUpdatedEvent) domainEvents.get(1);
        assertEquals(person.getId(), event.getPersonId());
        assertEquals("name", event.getUpdatedFields());
    }
    
    @Test
    @DisplayName("测试更新生僻字姓名后再更新为普通姓名")
    void testUpdateNormalNameAfterRareCharacter() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 先更新为带有生僻字的姓名
        person.updateNameWithRareCharacter("赵(木占)", "赵𬊤", "ZHAO ZHAN");
        assertTrue(person.isRareCharacter());
        assertNotNull(person.getOriginalName());
        
        // 再更新为普通姓名
        person.updateName("李四");
        
        // 验证结果
        assertEquals("李四", person.getName());
        assertFalse(person.isRareCharacter()); // 生僻字标志应为false
        assertNull(person.getOriginalName()); // 原始姓名对象应为null
        
        // 验证getDisplayName和getFullName方法
        assertEquals("李四", person.getDisplayName()); // 显示用姓名应为普通姓名
        assertEquals("李四", person.getFullName()); // 完整姓名也应为普通姓名
    }
    
    @Test
    @DisplayName("测试生僻字姓名的参数验证")
    void testRareCharacterNameValidation() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 测试处理后姓名为空的情况
        assertThrows(IllegalArgumentException.class, () -> {
            person.updateNameWithRareCharacter("", "赵𬊤", "ZHAO ZHAN");
        });
        
        // 测试原始姓名为空的情况
        assertThrows(IllegalArgumentException.class, () -> {
            person.updateNameWithRareCharacter("赵(木占)", "", "ZHAO ZHAN");
        });
    }
    
    /**
     * 创建用于测试的自然人对象
     */
    private Person createTestPerson() {
        PersonId personId = PersonId.of(UUID.randomUUID().toString());
        String name = "张三";
        Gender gender = Gender.MALE;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        
        IdentityDocument idCard = IdentityDocument.ofIdCard(
                "110101199001011234", 
                "北京市公安局",
                LocalDate.now(),
                LocalDate.now().plusYears(20)
        );
        
        ContactInfo contactInfo = ContactInfo.basic("13800138000", null);
        
        return Person.create(personId, name, gender, birthDate, idCard, contactInfo);
    }
}     @Test
