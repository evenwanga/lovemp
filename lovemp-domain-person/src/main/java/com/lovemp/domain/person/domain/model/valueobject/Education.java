package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 教育经历值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Education implements ValueObject {
    
    /**
     * 学校名称
     */
    private String school;
    
    /**
     * 专业
     */
    private String major;
    
    /**
     * 学历等级
     */
    private EducationLevel level;
    
    /**
     * 入学日期
     */
    private LocalDate startDate;
    
    /**
     * 毕业日期
     */
    private LocalDate endDate;
    
    /**
     * 是否获得学位
     */
    private boolean degreeObtained;
    
    /**
     * 学位类型（如学士、硕士、博士等）
     */
    private String degreeType;
    
    /**
     * 创建教育经历值对象
     * 
     * @param school 学校名称
     * @param major 专业
     * @param level 学历等级
     * @param startDate 入学日期
     * @param endDate 毕业日期
     * @param degreeObtained 是否获得学位
     * @param degreeType 学位类型
     * @return 教育经历值对象
     */
    public static Education of(String school, String major, EducationLevel level,
                              LocalDate startDate, LocalDate endDate,
                              boolean degreeObtained, String degreeType) {
        return new Education(school, major, level, startDate, endDate, degreeObtained, degreeType);
    }
    
    /**
     * 判断是否为最高学历
     * 
     * @param otherEducation 其他教育经历
     * @return 如果当前教育经历的学历等级高于其他教育经历，则返回true
     */
    public boolean isHigherThan(Education otherEducation) {
        if (otherEducation == null) {
            return true;
        }
        return this.level.ordinal() > otherEducation.level.ordinal();
    }
    
    /**
     * 判断是否正在就读
     * 
     * @return 如果当前正在就读则返回true
     */
    public boolean isOngoing() {
        return endDate == null || endDate.isAfter(DateTimeUtils.getCurrentDate());
    }
    
    /**
     * 判断是否在读
     * 
     * @return 如果结束日期为空或结束日期在当前日期之后，则认为在读
     */
    public boolean isInProgress() {
        return endDate == null || endDate.isAfter(DateTimeUtils.getCurrentDate());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Education education = (Education) o;
        return degreeObtained == education.degreeObtained &&
               Objects.equals(school, education.school) &&
               Objects.equals(major, education.major) &&
               level == education.level &&
               Objects.equals(startDate, education.startDate) &&
               Objects.equals(endDate, education.endDate) &&
               Objects.equals(degreeType, education.degreeType);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(school, major, level, startDate, endDate, degreeObtained, degreeType);
    }
}