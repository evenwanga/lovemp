package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.Education;
import com.lovemp.domain.person.domain.model.valueobject.EducationLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("教育经历值对象测试")
class EducationTest {

    @Test
    @DisplayName("测试创建教育经历")
    void testCreateEducation() {
        // 创建教育经历
        LocalDate startDate = LocalDate.of(2010, 9, 1);
        LocalDate endDate = LocalDate.of(2014, 7, 1);
        
        Education education = Education.of(
                "清华大学",
                "计算机科学与技术",
                EducationLevel.UNDERGRADUATE,
                startDate,
                endDate,
                true,
                "学士学位"
        );
        
        // 验证结果
        assertEquals("清华大学", education.getSchool());
        assertEquals("计算机科学与技术", education.getMajor());
        assertEquals(EducationLevel.UNDERGRADUATE, education.getLevel());
        assertEquals(startDate, education.getStartDate());
        assertEquals(endDate, education.getEndDate());
        assertTrue(education.isDegreeObtained());
        assertEquals("学士学位", education.getDegreeType());
    }
    
    @Test
    @DisplayName("测试创建未毕业的教育经历")
    void testCreateNotGraduated() {
        // 创建教育经历
        LocalDate startDate = LocalDate.of(2020, 9, 1);
        
        Education education = Education.of(
                "北京大学",
                "人工智能",
                EducationLevel.MASTER,
                startDate,
                null,
                false,
                null
        );
        
        // 验证结果
        assertEquals("北京大学", education.getSchool());
        assertEquals("人工智能", education.getMajor());
        assertEquals(EducationLevel.MASTER, education.getLevel());
        assertEquals(startDate, education.getStartDate());
        assertNull(education.getEndDate());
        assertFalse(education.isDegreeObtained());
        assertNull(education.getDegreeType());
        assertTrue(education.isOngoing());
    }
    
    @Test
    @DisplayName("测试比较学历高低")
    void testCompareEducationLevel() {
        // 创建不同学历水平的教育经历
        Education highSchool = Education.of(
                "第一中学",
                "理科",
                EducationLevel.HIGH_SCHOOL,
                LocalDate.of(2006, 9, 1),
                LocalDate.of(2009, 7, 1),
                true,
                "高中毕业证"
        );
        
        Education bachelor = Education.of(
                "清华大学",
                "计算机科学",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        
        Education master = Education.of(
                "北京大学",
                "人工智能",
                EducationLevel.MASTER,
                LocalDate.of(2014, 9, 1),
                LocalDate.of(2017, 7, 1),
                true,
                "硕士学位"
        );
        
        Education phd = Education.of(
                "麻省理工学院",
                "计算机科学",
                EducationLevel.DOCTORATE,
                LocalDate.of(2017, 9, 1),
                LocalDate.of(2022, 7, 1),
                true,
                "博士学位"
        );
        
        // 测试比较方法
        assertTrue(bachelor.isHigherThan(highSchool));
        assertTrue(master.isHigherThan(bachelor));
        assertTrue(phd.isHigherThan(master));
        
        // 测试反向比较
        assertFalse(highSchool.isHigherThan(bachelor));
        assertFalse(bachelor.isHigherThan(master));
        assertFalse(master.isHigherThan(phd));
        
        // 测试相同级别
        assertFalse(bachelor.isHigherThan(bachelor));
    }
    
    @Test
    @DisplayName("测试是否正在就读")
    void testIsOngoing() {
        // 创建已完成的教育经历
        Education completed = Education.of(
                "清华大学",
                "计算机科学",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        
        // 创建未结束日期的教育经历
        Education noEndDate = Education.of(
                "北京大学",
                "人工智能",
                EducationLevel.MASTER,
                LocalDate.of(2020, 9, 1),
                null,
                false,
                null
        );
        
        // 创建结束日期在未来的教育经历
        Education futureEndDate = Education.of(
                "北京大学",
                "人工智能",
                EducationLevel.MASTER,
                LocalDate.of(2020, 9, 1),
                LocalDate.now().plusYears(1),
                false,
                null
        );
        
        // 验证是否正在就读
        assertFalse(completed.isOngoing());
        assertTrue(noEndDate.isOngoing());
        assertTrue(futureEndDate.isOngoing());
    }
    
    @Test
    @DisplayName("测试值对象相等性")
    void testEquality() {
        // 创建两个相同的教育经历但实例不同
        Education edu1 = Education.of(
                "清华大学",
                "计算机科学",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        
        Education edu2 = Education.of(
                "清华大学",
                "计算机科学",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        
        // 验证相等性
        assertEquals(edu1, edu2);
        assertEquals(edu1.hashCode(), edu2.hashCode());
        
        // 创建不同的教育经历
        Education edu3 = Education.of(
                "北京大学", // 不同的学校
                "计算机科学",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        
        assertNotEquals(edu1, edu3);
    }
} 