package com.lovemp.domain.labor.domain.model.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("雇佣状态值对象测试")
class EmploymentStatusTest {

    @Test
    @DisplayName("应正确识别活跃状态")
    void shouldIdentifyActiveStatus() {
        assertTrue(EmploymentStatus.ACTIVE.isActive());
        
        assertFalse(EmploymentStatus.PENDING_ONBOARD.isActive());
        assertFalse(EmploymentStatus.LEAVING.isActive());
        assertFalse(EmploymentStatus.TERMINATED.isActive());
        assertFalse(EmploymentStatus.CANCELED.isActive());
    }
    
    @Test
    @DisplayName("应正确识别可入职状态")
    void shouldIdentifyCanOnboardStatus() {
        assertTrue(EmploymentStatus.PENDING_ONBOARD.canOnboard());
        
        assertFalse(EmploymentStatus.ACTIVE.canOnboard());
        assertFalse(EmploymentStatus.LEAVING.canOnboard());
        assertFalse(EmploymentStatus.TERMINATED.canOnboard());
        assertFalse(EmploymentStatus.CANCELED.canOnboard());
    }
    
    @Test
    @DisplayName("应正确识别可离职状态")
    void shouldIdentifyCanLeaveStatus() {
        assertTrue(EmploymentStatus.ACTIVE.canLeave());
        
        assertFalse(EmploymentStatus.PENDING_ONBOARD.canLeave());
        assertFalse(EmploymentStatus.LEAVING.canLeave());
        assertFalse(EmploymentStatus.TERMINATED.canLeave());
        assertFalse(EmploymentStatus.CANCELED.canLeave());
    }
    
    @Test
    @DisplayName("应正确识别可取消状态")
    void shouldIdentifyCanCancelStatus() {
        assertTrue(EmploymentStatus.PENDING_ONBOARD.canCancel());
        
        assertFalse(EmploymentStatus.ACTIVE.canCancel());
        assertFalse(EmploymentStatus.LEAVING.canCancel());
        assertFalse(EmploymentStatus.TERMINATED.canCancel());
        assertFalse(EmploymentStatus.CANCELED.canCancel());
    }
    
    @Test
    @DisplayName("应能正确获取雇佣状态描述")
    void shouldGetCorrectDescription() {
        assertEquals("待入职", EmploymentStatus.PENDING_ONBOARD.getDescription());
        assertEquals("在职", EmploymentStatus.ACTIVE.getDescription());
        assertEquals("离职中", EmploymentStatus.LEAVING.getDescription());
        assertEquals("已离职", EmploymentStatus.TERMINATED.getDescription());
        assertEquals("已取消", EmploymentStatus.CANCELED.getDescription());
    }
}