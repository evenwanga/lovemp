package com.lovemp.domain.labor.domain.model.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("雇佣类型值对象测试")
class EmploymentTypeTest {

    @Test
    @DisplayName("应正确识别正式雇佣关系")
    void shouldIdentifyFormalEmployment() {
        assertTrue(EmploymentType.FULL_TIME.isFormalEmployment());
        assertTrue(EmploymentType.PART_TIME.isFormalEmployment());
        
        assertFalse(EmploymentType.TEMPORARY.isFormalEmployment());
        assertFalse(EmploymentType.CONTRACT.isFormalEmployment());
        assertFalse(EmploymentType.INTERN.isFormalEmployment());
        assertFalse(EmploymentType.CONTRACTOR.isFormalEmployment());
        assertFalse(EmploymentType.OUTSOURCE.isFormalEmployment());
    }
    
    @Test
    @DisplayName("应正确识别灵活用工")
    void shouldIdentifyFlexibleEmployment() {
        assertTrue(EmploymentType.TEMPORARY.isFlexibleEmployment());
        assertTrue(EmploymentType.CONTRACT.isFlexibleEmployment());
        assertTrue(EmploymentType.CONTRACTOR.isFlexibleEmployment());
        
        assertFalse(EmploymentType.FULL_TIME.isFlexibleEmployment());
        assertFalse(EmploymentType.PART_TIME.isFlexibleEmployment());
        assertFalse(EmploymentType.INTERN.isFlexibleEmployment());
        assertFalse(EmploymentType.OUTSOURCE.isFlexibleEmployment());
    }
    
    @Test
    @DisplayName("应能正确获取雇佣类型描述")
    void shouldGetCorrectDescription() {
        assertEquals("全职雇员", EmploymentType.FULL_TIME.getDescription());
        assertEquals("兼职雇员", EmploymentType.PART_TIME.getDescription());
        assertEquals("临时工", EmploymentType.TEMPORARY.getDescription());
        assertEquals("合同工", EmploymentType.CONTRACT.getDescription());
        assertEquals("实习生", EmploymentType.INTERN.getDescription());
        assertEquals("个体工商户", EmploymentType.CONTRACTOR.getDescription());
        assertEquals("外包", EmploymentType.OUTSOURCE.getDescription());
    }
}