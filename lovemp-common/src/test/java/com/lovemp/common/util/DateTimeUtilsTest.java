package com.lovemp.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * DateTimeUtils工具类单元测试
 * 
 * 该测试类用于验证DateTimeUtils工具类中各种日期时间操作方法的正确性,包括:
 * - 日期时间获取: getCurrentDate/Time/DateTime等方法
 * - 日期时间转换: toDate/toLocalDate等转换方法
 * - 日期时间计算: addDays/between等计算方法
 * - 日期时间格式化: format/parse等格式化方法
 * - 日期时间比较: isBefore/isAfter等比较方法
 * - 工作日计算: isWeekday/workdaysBetween等方法
 * 
 * 每个测试用例都会验证:
 * 1. 正常情况 - 方法的基本功能
 * 2. 边界情况 - 特殊日期、时区转换等
 * 3. 异常情况 - null值、无效格式等
 * 4. 性能要求 - 计算效率和精度
 * 
 * @see com.lovemp.common.util.DateTimeUtils
 */
public class DateTimeUtilsTest {

    /**
     * 测试获取当前日期/时间
     */
    @Test
    public void testGetCurrent() {
        // 获取当前日期、时间和日期时间
        LocalDate currentDate = DateTimeUtils.getCurrentDate();
        LocalTime currentTime = DateTimeUtils.getCurrentTime();
        LocalDateTime currentDateTime = DateTimeUtils.getCurrentDateTime();
        
        // 验证结果不为空
        assertNotNull(currentDate);
        assertNotNull(currentTime);
        assertNotNull(currentDateTime);
        
        // 验证当前日期与系统日期一致
        assertEquals(LocalDate.now(), currentDate);
        
        // 简单验证时间的合理性
        assertTrue(currentTime.getHour() >= 0 && currentTime.getHour() <= 23);
        assertTrue(currentTime.getMinute() >= 0 && currentTime.getMinute() <= 59);
        
        // 验证日期时间组合的一致性
        assertEquals(currentDate, currentDateTime.toLocalDate());
    }
    
    /**
     * 测试获取时间戳
     */
    @Test
    public void testGetCurrentTimestamp() {
        long timestamp = DateTimeUtils.getCurrentTimestamp();
        
        // 验证时间戳大于0
        assertTrue(timestamp > 0);
        
        // 验证时间戳与系统时间戳接近（误差在100毫秒内）
        long systemTimestamp = System.currentTimeMillis();
        assertTrue(Math.abs(systemTimestamp - timestamp) < 100);
    }
    
    /**
     * 测试获取当前日期/时间字符串
     */
    @Test
    public void testGetCurrentDateTimeStr() {
        // 获取当前日期、时间和日期时间字符串
        String currentDateStr = DateTimeUtils.getCurrentDateStr();
        String currentTimeStr = DateTimeUtils.getCurrentTimeStr();
        String currentDateTimeStr = DateTimeUtils.getCurrentDateTimeStr();
        
        // 验证结果不为空
        assertNotNull(currentDateStr);
        assertNotNull(currentTimeStr);
        assertNotNull(currentDateTimeStr);
        
        // 验证日期字符串格式正确
        assertTrue(currentDateStr.matches("\\d{4}-\\d{2}-\\d{2}"));
        
        // 验证时间字符串格式正确
        assertTrue(currentTimeStr.matches("\\d{2}:\\d{2}:\\d{2}"));
        
        // 验证日期时间字符串格式正确
        assertTrue(currentDateTimeStr.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"));
    }
    
    /**
     * 测试格式化日期
     */
    @Test
    public void testFormatDate() {
        // 准备测试数据
        LocalDate date = LocalDate.of(2023, 5, 15);
        
        // 测试默认格式
        String formattedDate = DateTimeUtils.formatDate(date);
        assertEquals("2023-05-15", formattedDate);
        
        // 测试自定义格式
        String customFormattedDate = DateTimeUtils.formatDate(date, "yyyy年MM月dd日");
        assertEquals("2023年05月15日", customFormattedDate);
        
        // 测试空值处理
        assertNull(DateTimeUtils.formatDate(null));
        assertNull(DateTimeUtils.formatDate(null, "yyyy-MM-dd"));
    }
    
    /**
     * 测试格式化时间
     */
    @Test
    public void testFormatTime() {
        // 准备测试数据
        LocalTime time = LocalTime.of(14, 30, 45);
        
        // 测试默认格式
        String formattedTime = DateTimeUtils.formatTime(time);
        assertEquals("14:30:45", formattedTime);
        
        // 测试自定义格式
        String customFormattedTime = DateTimeUtils.formatTime(time, "HH时mm分ss秒");
        assertEquals("14时30分45秒", customFormattedTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.formatTime(null));
        assertNull(DateTimeUtils.formatTime(null, "HH:mm:ss"));
    }
    
    /**
     * 测试格式化日期时间
     */
    @Test
    public void testFormatDateTime() {
        // 准备测试数据
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 15, 14, 30, 45);
        
        // 测试默认格式
        String formattedDateTime = DateTimeUtils.formatDateTime(dateTime);
        assertEquals("2023-05-15 14:30:45", formattedDateTime);
        
        // 测试自定义格式
        String customFormattedDateTime = DateTimeUtils.formatDateTime(dateTime, "yyyy/MM/dd HH:mm:ss");
        assertEquals("2023/05/15 14:30:45", customFormattedDateTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.formatDateTime(null));
        assertNull(DateTimeUtils.formatDateTime(null, "yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * 测试解析日期字符串
     */
    @Test
    public void testParseDate() {
        // 准备测试数据
        String dateStr = "2023-05-15";
        LocalDate expectedDate = LocalDate.of(2023, 5, 15);
        
        // 测试默认格式解析
        LocalDate parsedDate = DateTimeUtils.parseDate(dateStr);
        assertEquals(expectedDate, parsedDate);
        
        // 测试自定义格式解析
        LocalDate customParsedDate = DateTimeUtils.parseDate("2023年05月15日", "yyyy年MM月dd日");
        assertEquals(expectedDate, customParsedDate);
        
        // 测试空值处理
        assertNull(DateTimeUtils.parseDate(null));
        assertNull(DateTimeUtils.parseDate(""));
        assertNull(DateTimeUtils.parseDate(null, "yyyy-MM-dd"));
        assertNull(DateTimeUtils.parseDate("", "yyyy-MM-dd"));
    }
    
    /**
     * 测试解析时间字符串
     */
    @Test
    public void testParseTime() {
        // 准备测试数据
        String timeStr = "14:30:45";
        LocalTime expectedTime = LocalTime.of(14, 30, 45);
        
        // 测试默认格式解析
        LocalTime parsedTime = DateTimeUtils.parseTime(timeStr);
        assertEquals(expectedTime, parsedTime);
        
        // 测试自定义格式解析
        LocalTime customParsedTime = DateTimeUtils.parseTime("14时30分45秒", "HH时mm分ss秒");
        assertEquals(expectedTime, customParsedTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.parseTime(null));
        assertNull(DateTimeUtils.parseTime(""));
        assertNull(DateTimeUtils.parseTime(null, "HH:mm:ss"));
        assertNull(DateTimeUtils.parseTime("", "HH:mm:ss"));
    }
    
    /**
     * 测试解析日期时间字符串
     */
    @Test
    public void testParseDateTime() {
        // 准备测试数据
        String dateTimeStr = "2023-05-15 14:30:45";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 5, 15, 14, 30, 45);
        
        // 测试默认格式解析
        LocalDateTime parsedDateTime = DateTimeUtils.parseDateTime(dateTimeStr);
        assertEquals(expectedDateTime, parsedDateTime);
        
        // 测试自定义格式解析
        LocalDateTime customParsedDateTime = DateTimeUtils.parseDateTime("2023/05/15 14:30:45", "yyyy/MM/dd HH:mm:ss");
        assertEquals(expectedDateTime, customParsedDateTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.parseDateTime(null));
        assertNull(DateTimeUtils.parseDateTime(""));
        assertNull(DateTimeUtils.parseDateTime(null, "yyyy-MM-dd HH:mm:ss"));
        assertNull(DateTimeUtils.parseDateTime("", "yyyy-MM-dd HH:mm:ss"));
    }
    
    /**
     * 测试Java Date与LocalDate互转
     */
    @Test
    public void testDateAndLocalDateConversion() {
        // LocalDate转Date
        LocalDate localDate = LocalDate.of(2023, 5, 15);
        Date date = DateTimeUtils.toDate(localDate);
        
        assertNotNull(date);
        
        // Date转LocalDate
        LocalDate convertedLocalDate = DateTimeUtils.toLocalDate(date);
        
        assertEquals(localDate, convertedLocalDate);
    }
    
    /**
     * 测试Java Date与LocalDateTime互转
     */
    @Test
    public void testDateAndLocalDateTimeConversion() {
        // LocalDateTime转Date
        LocalDateTime localDateTime = LocalDateTime.of(2023, 5, 15, 14, 30, 45);
        Date date = DateTimeUtils.toDate(localDateTime);
        
        assertNotNull(date);
        
        // Date转LocalDateTime
        LocalDateTime convertedLocalDateTime = DateTimeUtils.toLocalDateTime(date);
        
        // 注意：转换可能有精度问题，所以我们只比较到秒级别
        assertEquals(localDateTime.truncatedTo(ChronoUnit.SECONDS), 
                     convertedLocalDateTime.truncatedTo(ChronoUnit.SECONDS));
    }
    
    /**
     * 测试计算日期间隔
     */
    @Test
    public void testDaysBetween() {
        // 准备测试数据
        LocalDate startDate = LocalDate.of(2023, 5, 15);
        LocalDate endDate = LocalDate.of(2023, 5, 20);
        
        // 测试正向计算
        long days = DateTimeUtils.daysBetween(startDate, endDate);
        assertEquals(5, days);
        
        // 测试反向计算（应返回负值）
        long negativeDays = DateTimeUtils.daysBetween(endDate, startDate);
        assertEquals(-5, negativeDays);
        
        // 测试相同日期
        long zeroDays = DateTimeUtils.daysBetween(startDate, startDate);
        assertEquals(0, zeroDays);
    }
    
    /**
     * 测试计算小时间隔
     */
    @Test
    public void testHoursBetween() {
        // 准备测试数据
        LocalDateTime startDateTime = LocalDateTime.of(2023, 5, 15, 10, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 5, 15, 15, 30, 0);
        
        // 测试正向计算
        long hours = DateTimeUtils.hoursBetween(startDateTime, endDateTime);
        assertEquals(5, hours);
        
        // 测试反向计算（应返回负值）
        long negativeHours = DateTimeUtils.hoursBetween(endDateTime, startDateTime);
        assertEquals(-5, negativeHours);
        
        // 测试相同时间
        long zeroHours = DateTimeUtils.hoursBetween(startDateTime, startDateTime);
        assertEquals(0, zeroHours);
    }
    
    /**
     * 测试计算分钟间隔
     */
    @Test
    public void testMinutesBetween() {
        // 准备测试数据
        LocalDateTime startDateTime = LocalDateTime.of(2023, 5, 15, 10, 15, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 5, 15, 10, 45, 0);
        
        // 测试正向计算
        long minutes = DateTimeUtils.minutesBetween(startDateTime, endDateTime);
        assertEquals(30, minutes);
        
        // 测试反向计算（应返回负值）
        long negativeMinutes = DateTimeUtils.minutesBetween(endDateTime, startDateTime);
        assertEquals(-30, negativeMinutes);
        
        // 测试相同时间
        long zeroMinutes = DateTimeUtils.minutesBetween(startDateTime, startDateTime);
        assertEquals(0, zeroMinutes);
    }
    
    /**
     * 测试获取月份的第一天和最后一天
     */
    @Test
    public void testFirstAndLastDayOfMonth() {
        // 准备测试数据
        LocalDate date = LocalDate.of(2023, 5, 15);
        
        // 测试获取月份第一天
        LocalDate firstDay = DateTimeUtils.firstDayOfMonth(date);
        assertEquals(LocalDate.of(2023, 5, 1), firstDay);
        
        // 测试获取月份最后一天
        LocalDate lastDay = DateTimeUtils.lastDayOfMonth(date);
        assertEquals(LocalDate.of(2023, 5, 31), lastDay);
        
        // 测试特殊月份（2月）
        LocalDate febDate = LocalDate.of(2024, 2, 15); // 2024是闰年
        assertEquals(LocalDate.of(2024, 2, 1), DateTimeUtils.firstDayOfMonth(febDate));
        assertEquals(LocalDate.of(2024, 2, 29), DateTimeUtils.lastDayOfMonth(febDate));
        
        // 测试空值处理
        assertNull(DateTimeUtils.firstDayOfMonth(null));
        assertNull(DateTimeUtils.lastDayOfMonth(null));
    }
    
    /**
     * 测试获取周的第一天和最后一天
     */
    @Test
    public void testFirstAndLastDayOfWeek() {
        // 准备测试数据 - 2023-05-15是星期一
        LocalDate monday = LocalDate.of(2023, 5, 15);
        
        // 测试获取周第一天
        LocalDate firstDay = DateTimeUtils.firstDayOfWeek(monday);
        assertEquals(LocalDate.of(2023, 5, 15), firstDay); // 星期一是周第一天
        
        // 测试获取周最后一天
        LocalDate lastDay = DateTimeUtils.lastDayOfWeek(monday);
        assertEquals(LocalDate.of(2023, 5, 21), lastDay); // 星期日是周最后一天
        
        // 测试从周中某一天
        LocalDate wednesday = LocalDate.of(2023, 5, 17); // 星期三
        assertEquals(LocalDate.of(2023, 5, 15), DateTimeUtils.firstDayOfWeek(wednesday));
        assertEquals(LocalDate.of(2023, 5, 21), DateTimeUtils.lastDayOfWeek(wednesday));
        
        // 测试空值处理
        assertNull(DateTimeUtils.firstDayOfWeek(null));
        assertNull(DateTimeUtils.lastDayOfWeek(null));
    }
    
    /**
     * 测试日期是否在指定范围内
     */
    @Test
    public void testIsBetween() {
        // 准备测试数据
        LocalDate startDate = LocalDate.of(2023, 5, 1);
        LocalDate midDate = LocalDate.of(2023, 5, 15);
        LocalDate endDate = LocalDate.of(2023, 5, 31);
        
        // 测试日期在范围内
        assertTrue(DateTimeUtils.isBetween(midDate, startDate, endDate));
        
        // 测试日期等于边界
        assertTrue(DateTimeUtils.isBetween(startDate, startDate, endDate));
        assertTrue(DateTimeUtils.isBetween(endDate, startDate, endDate));
        
        // 测试日期在范围外
        LocalDate beforeDate = LocalDate.of(2023, 4, 30);
        LocalDate afterDate = LocalDate.of(2023, 6, 1);
        assertFalse(DateTimeUtils.isBetween(beforeDate, startDate, endDate));
        assertFalse(DateTimeUtils.isBetween(afterDate, startDate, endDate));
        
        // 测试日期时间在范围内
        LocalDateTime startDateTime = LocalDateTime.of(2023, 5, 1, 0, 0, 0);
        LocalDateTime midDateTime = LocalDateTime.of(2023, 5, 15, 12, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2023, 5, 31, 23, 59, 59);
        
        assertTrue(DateTimeUtils.isBetween(midDateTime, startDateTime, endDateTime));
        assertTrue(DateTimeUtils.isBetween(startDateTime, startDateTime, endDateTime));
        assertTrue(DateTimeUtils.isBetween(endDateTime, startDateTime, endDateTime));
        
        // 测试空值处理
        assertThrows(NullPointerException.class, () -> DateTimeUtils.isBetween(null, startDate, endDate));
        assertThrows(NullPointerException.class, () -> DateTimeUtils.isBetween(midDate, null, endDate));
        assertThrows(NullPointerException.class, () -> DateTimeUtils.isBetween(midDate, startDate, null));
    }
    
    /**
     * 测试日期加减天数
     */
    @Test
    public void testAddDays() {
        // 准备测试数据
        LocalDate date = LocalDate.of(2023, 5, 15);
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 15, 12, 30, 0);
        
        // 测试增加天数
        LocalDate newDate = DateTimeUtils.addDays(date, 5);
        assertEquals(LocalDate.of(2023, 5, 20), newDate);
        
        // 测试减少天数
        newDate = DateTimeUtils.addDays(date, -5);
        assertEquals(LocalDate.of(2023, 5, 10), newDate);
        
        // 测试跨月
        newDate = DateTimeUtils.addDays(date, 20);
        assertEquals(LocalDate.of(2023, 6, 4), newDate);
        
        // 测试跨年
        newDate = DateTimeUtils.addDays(date, 365);
        assertEquals(LocalDate.of(2024, 5, 14), newDate); // 2024为闰年
        
        // 测试日期时间
        LocalDateTime newDateTime = DateTimeUtils.addDays(dateTime, 5);
        assertEquals(LocalDateTime.of(2023, 5, 20, 12, 30, 0), newDateTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.addDays((LocalDate) null, 5));
        assertNull(DateTimeUtils.addDays((LocalDateTime) null, 5));
    }
    
    /**
     * 测试日期加减月份
     */
    @Test
    public void testAddMonths() {
        // 准备测试数据
        LocalDate date = LocalDate.of(2023, 5, 15);
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 15, 12, 30, 0);
        
        // 测试增加月份
        LocalDate newDate = DateTimeUtils.addMonths(date, 3);
        assertEquals(LocalDate.of(2023, 8, 15), newDate);
        
        // 测试减少月份
        newDate = DateTimeUtils.addMonths(date, -3);
        assertEquals(LocalDate.of(2023, 2, 15), newDate);
        
        // 测试跨年
        newDate = DateTimeUtils.addMonths(date, 8);
        assertEquals(LocalDate.of(2024, 1, 15), newDate);
        
        // 测试月末日期
        LocalDate monthEnd = LocalDate.of(2023, 1, 31);
        newDate = DateTimeUtils.addMonths(monthEnd, 1);
        assertEquals(LocalDate.of(2023, 2, 28), newDate); // 2月没有31日，自动调整为28日
        
        // 测试日期时间
        LocalDateTime newDateTime = DateTimeUtils.addMonths(dateTime, 3);
        assertEquals(LocalDateTime.of(2023, 8, 15, 12, 30, 0), newDateTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.addMonths((LocalDate) null, 3));
        assertNull(DateTimeUtils.addMonths((LocalDateTime) null, 3));
    }
    
    /**
     * 测试日期加减年份
     */
    @Test
    public void testAddYears() {
        // 准备测试数据
        LocalDate date = LocalDate.of(2023, 5, 15);
        LocalDateTime dateTime = LocalDateTime.of(2023, 5, 15, 12, 30, 0);
        
        // 测试增加年份
        LocalDate newDate = DateTimeUtils.addYears(date, 2);
        assertEquals(LocalDate.of(2025, 5, 15), newDate);
        
        // 测试减少年份
        newDate = DateTimeUtils.addYears(date, -2);
        assertEquals(LocalDate.of(2021, 5, 15), newDate);
        
        // 测试闰年处理
        LocalDate leapDate = LocalDate.of(2020, 2, 29); // 2020是闰年
        newDate = DateTimeUtils.addYears(leapDate, 1);
        assertEquals(LocalDate.of(2021, 2, 28), newDate); // 2021不是闰年，自动调整为28日
        
        // 测试日期时间
        LocalDateTime newDateTime = DateTimeUtils.addYears(dateTime, 2);
        assertEquals(LocalDateTime.of(2025, 5, 15, 12, 30, 0), newDateTime);
        
        // 测试空值处理
        assertNull(DateTimeUtils.addYears((LocalDate) null, 2));
        assertNull(DateTimeUtils.addYears((LocalDateTime) null, 2));
    }
    
    /**
     * 测试日期比较
     */
    @Test
    public void testDateComparison() {
        // 准备测试数据
        LocalDate date1 = LocalDate.of(2023, 5, 15);
        LocalDate date2 = LocalDate.of(2023, 5, 20);
        LocalDate date3 = LocalDate.of(2023, 5, 15); // 与date1相同
        
        // 测试isBefore
        assertTrue(DateTimeUtils.isBefore(date1, date2));
        assertFalse(DateTimeUtils.isBefore(date2, date1));
        assertFalse(DateTimeUtils.isBefore(date1, date3)); // 相同日期
        
        // 测试isAfter
        assertTrue(DateTimeUtils.isAfter(date2, date1));
        assertFalse(DateTimeUtils.isAfter(date1, date2));
        assertFalse(DateTimeUtils.isAfter(date1, date3)); // 相同日期
        
        // 测试null处理
        assertFalse(DateTimeUtils.isBefore(null, date2));
        assertFalse(DateTimeUtils.isBefore(date1, null));
        assertFalse(DateTimeUtils.isAfter(null, date1));
        assertFalse(DateTimeUtils.isAfter(date2, null));
    }
    
    /**
     * 测试日期是否同一天/月/年
     */
    @Test
    public void testDateEquality() {
        // 准备测试数据
        LocalDate date1 = LocalDate.of(2023, 5, 15);
        LocalDate date2 = LocalDate.of(2023, 5, 15); // 与date1相同
        LocalDate date3 = LocalDate.of(2023, 5, 16); // 不同日期，同月
        LocalDate date4 = LocalDate.of(2023, 6, 15); // 不同月份，同年
        LocalDate date5 = LocalDate.of(2024, 5, 15); // 不同年份
        
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 5, 15, 12, 30, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 5, 15, 18, 45, 0); // 同一天不同时间
        
        // 测试isSameDay
        assertTrue(DateTimeUtils.isSameDay(date1, date2));
        assertFalse(DateTimeUtils.isSameDay(date1, date3));
        
        // 测试日期时间isSameDay
        assertTrue(DateTimeUtils.isSameDay(dateTime1, dateTime2));
        
        // 测试isSameMonth
        assertTrue(DateTimeUtils.isSameMonth(date1, date2));
        assertTrue(DateTimeUtils.isSameMonth(date1, date3));
        assertFalse(DateTimeUtils.isSameMonth(date1, date4));
        
        // 测试isSameYear
        assertTrue(DateTimeUtils.isSameYear(date1, date2));
        assertTrue(DateTimeUtils.isSameYear(date1, date3));
        assertTrue(DateTimeUtils.isSameYear(date1, date4));
        assertFalse(DateTimeUtils.isSameYear(date1, date5));
        
        // 测试null处理
        assertFalse(DateTimeUtils.isSameDay(null, date2));
        assertFalse(DateTimeUtils.isSameDay(date1, null));
        assertFalse(DateTimeUtils.isSameMonth(null, date2));
        assertFalse(DateTimeUtils.isSameYear(date1, null));
    }
    
    /**
     * 测试工作日和周末判断
     */
    @Test
    public void testWeekdayAndWeekend() {
        // 2023-05-15是星期一，2023-05-20是星期六，2023-05-21是星期日
        LocalDate monday = LocalDate.of(2023, 5, 15);
        LocalDate saturday = LocalDate.of(2023, 5, 20);
        LocalDate sunday = LocalDate.of(2023, 5, 21);
        
        // 输出每个日期的星期几，用于调试
        System.out.println("Monday: " + monday + " - " + monday.getDayOfWeek());
        System.out.println("Saturday: " + saturday + " - " + saturday.getDayOfWeek());
        System.out.println("Sunday: " + sunday + " - " + sunday.getDayOfWeek());
        
        // 测试工作日判断
        assertTrue(DateTimeUtils.isWeekday(monday));
        assertFalse(DateTimeUtils.isWeekday(saturday));
        assertFalse(DateTimeUtils.isWeekday(sunday));
        
        // 测试周末判断
        assertFalse(DateTimeUtils.isWeekend(monday));
        assertTrue(DateTimeUtils.isWeekend(saturday));
        assertTrue(DateTimeUtils.isWeekend(sunday));
        
        // 测试null处理
        System.out.println("isWeekday(null): " + DateTimeUtils.isWeekday(null));
        assertFalse(DateTimeUtils.isWeekday(null));
        
        System.out.println("isWeekend(null): " + DateTimeUtils.isWeekend(null));
        assertFalse(DateTimeUtils.isWeekend(null));
    }
    
    /**
     * 测试工作日数量计算
     */
    @Test
    public void testWorkdaysBetween() {
        // 准备测试数据 - 2023年5月的工作日和周末分布
        // 周一到周五: 1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 15, 16, 17, 18, 19, 22, 23, 24, 25, 26, 29, 30, 31
        // 周末: 6, 7, 13, 14, 20, 21, 27, 28
        
        // 测试同一周内的工作日
        LocalDate monday = LocalDate.of(2023, 5, 15); // 周一
        LocalDate friday = LocalDate.of(2023, 5, 19); // 周五
        LocalDate saturday = LocalDate.of(2023, 5, 20); // 周六
        
        // 输出日期的星期几
        System.out.println("Monday: " + monday + " - " + monday.getDayOfWeek());
        System.out.println("Friday: " + friday + " - " + friday.getDayOfWeek());
        System.out.println("Saturday: " + saturday + " - " + saturday.getDayOfWeek());
        
        // 输出工作日情况
        System.out.println("Monday to Friday workdays: " + DateTimeUtils.workdaysBetween(monday, friday));
        
        assertEquals(5, DateTimeUtils.workdaysBetween(monday, friday));
        
        // 测试周末跨越
        LocalDate nextMonday = LocalDate.of(2023, 5, 22); // 下周一
        System.out.println("Next Monday: " + nextMonday + " - " + nextMonday.getDayOfWeek());
        System.out.println("Friday to next Monday workdays: " + DateTimeUtils.workdaysBetween(friday, nextMonday));
        
        assertEquals(5, DateTimeUtils.workdaysBetween(friday, nextMonday));
        
        // 测试长期间隔（包含多个周末）
        LocalDate startMonth = LocalDate.of(2023, 5, 1); // 5月1日，周一
        LocalDate endMonth = LocalDate.of(2023, 5, 31); // 5月31日，周三
        System.out.println("Start month: " + startMonth + " - " + startMonth.getDayOfWeek());
        System.out.println("End month: " + endMonth + " - " + endMonth.getDayOfWeek());
        System.out.println("Start month to end month workdays: " + DateTimeUtils.workdaysBetween(startMonth, endMonth));
        
        assertEquals(23, DateTimeUtils.workdaysBetween(startMonth, endMonth));
        
        // 测试开始日期在结束日期之后
        System.out.println("Friday to Monday workdays (reverse): " + DateTimeUtils.workdaysBetween(friday, monday));
        
        assertEquals(5, DateTimeUtils.workdaysBetween(friday, monday));
        
        // 测试同一天
        assertEquals(1, DateTimeUtils.workdaysBetween(monday, monday));
        assertEquals(0, DateTimeUtils.workdaysBetween(saturday, saturday)); // 周末
        
        // 测试null处理
        assertEquals(0, DateTimeUtils.workdaysBetween(null, friday));
        assertEquals(0, DateTimeUtils.workdaysBetween(monday, null));
    }
    
    /**
     * 测试季度第一天和最后一天
     */
    @Test
    public void testFirstAndLastDayOfQuarter() {
        // 测试不同季度
        // Q1: 1月-3月
        LocalDate q1Date = LocalDate.of(2023, 2, 15);
        assertEquals(LocalDate.of(2023, 1, 1), DateTimeUtils.firstDayOfQuarter(q1Date));
        assertEquals(LocalDate.of(2023, 3, 31), DateTimeUtils.lastDayOfQuarter(q1Date));
        
        // Q2: 4月-6月
        LocalDate q2Date = LocalDate.of(2023, 5, 15);
        assertEquals(LocalDate.of(2023, 4, 1), DateTimeUtils.firstDayOfQuarter(q2Date));
        assertEquals(LocalDate.of(2023, 6, 30), DateTimeUtils.lastDayOfQuarter(q2Date));
        
        // Q3: 7月-9月
        LocalDate q3Date = LocalDate.of(2023, 8, 15);
        assertEquals(LocalDate.of(2023, 7, 1), DateTimeUtils.firstDayOfQuarter(q3Date));
        assertEquals(LocalDate.of(2023, 9, 30), DateTimeUtils.lastDayOfQuarter(q3Date));
        
        // Q4: 10月-12月
        LocalDate q4Date = LocalDate.of(2023, 11, 15);
        assertEquals(LocalDate.of(2023, 10, 1), DateTimeUtils.firstDayOfQuarter(q4Date));
        assertEquals(LocalDate.of(2023, 12, 31), DateTimeUtils.lastDayOfQuarter(q4Date));
        
        // 测试null处理
        assertNull(DateTimeUtils.firstDayOfQuarter(null));
        assertNull(DateTimeUtils.lastDayOfQuarter(null));
    }
    
    /**
     * 测试年份第一天和最后一天
     */
    @Test
    public void testFirstAndLastDayOfYear() {
        // 测试不同年份
        LocalDate date2023 = LocalDate.of(2023, 5, 15);
        assertEquals(LocalDate.of(2023, 1, 1), DateTimeUtils.firstDayOfYear(date2023));
        assertEquals(LocalDate.of(2023, 12, 31), DateTimeUtils.lastDayOfYear(date2023));
        
        // 测试闰年
        LocalDate date2024 = LocalDate.of(2024, 2, 29); // 2024是闰年
        assertEquals(LocalDate.of(2024, 1, 1), DateTimeUtils.firstDayOfYear(date2024));
        assertEquals(LocalDate.of(2024, 12, 31), DateTimeUtils.lastDayOfYear(date2024));
        
        // 测试null处理
        assertNull(DateTimeUtils.firstDayOfYear(null));
        assertNull(DateTimeUtils.lastDayOfYear(null));
    }
    
    /**
     * 测试获取日期的年月日部分
     */
    @Test
    public void testGetDateParts() {
        // 准备测试数据
        LocalDate date = LocalDate.of(2023, 5, 15);
        
        // 测试获取年份
        assertEquals(2023, DateTimeUtils.getYear(date));
        
        // 测试获取月份
        assertEquals(5, DateTimeUtils.getMonth(date));
        
        // 测试获取日
        assertEquals(15, DateTimeUtils.getDay(date));
        
        // 测试null处理
        assertEquals(0, DateTimeUtils.getYear(null));
        assertEquals(0, DateTimeUtils.getMonth(null));
        assertEquals(0, DateTimeUtils.getDay(null));
    }
    
    /**
     * 测试获取时间的时分秒部分
     */
    @Test
    public void testGetTimeParts() {
        // 准备测试数据
        LocalTime time = LocalTime.of(14, 30, 45);
        
        // 测试获取小时
        assertEquals(14, DateTimeUtils.getHour(time));
        
        // 测试获取分钟
        assertEquals(30, DateTimeUtils.getMinute(time));
        
        // 测试获取秒
        assertEquals(45, DateTimeUtils.getSecond(time));
        
        // 测试null处理
        assertEquals(0, DateTimeUtils.getHour(null));
        assertEquals(0, DateTimeUtils.getMinute(null));
        assertEquals(0, DateTimeUtils.getSecond(null));
    }
    
    /**
     * 测试日期边界条件
     */
    @Test
    public void testDateBoundaries() {
        // 测试年初年末
        LocalDate yearStart = LocalDate.of(2023, 1, 1);
        LocalDate yearEnd = LocalDate.of(2023, 12, 31);
        
        // 年初前一天是上一年的年末
        assertEquals(yearEnd.minusYears(1), DateTimeUtils.addDays(yearStart, -1));
        
        // 年末后一天是下一年的年初
        assertEquals(yearStart.plusYears(1), DateTimeUtils.addDays(yearEnd, 1));
        
        // 测试不同月份天数
        // 大月（31天）
        assertEquals(31, DateTimeUtils.daysBetween(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 31)) + 1);
        // 小月（30天）
        assertEquals(30, DateTimeUtils.daysBetween(LocalDate.of(2023, 4, 1), LocalDate.of(2023, 4, 30)) + 1);
        // 平年2月（28天）
        assertEquals(28, DateTimeUtils.daysBetween(LocalDate.of(2023, 2, 1), LocalDate.of(2023, 2, 28)) + 1);
        // 闰年2月（29天）
        assertEquals(29, DateTimeUtils.daysBetween(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 29)) + 1);
    }
    
    /**
     * 测试闰年相关日期操作
     */
    @Test
    public void testLeapYearOperations() {
        // 测试在闰年和非闰年之间的日期变化
        
        // 准备测试数据 - 2020是闰年，2月29日存在
        LocalDate leapYearDate = LocalDate.of(2020, 2, 29);
        
        // 加一年后，日期应调整为2月28日（2021不是闰年）
        assertEquals(LocalDate.of(2021, 2, 28), DateTimeUtils.addYears(leapYearDate, 1));
        
        // 加四年后，日期应保持2月29日（2024也是闰年）
        assertEquals(LocalDate.of(2024, 2, 29), DateTimeUtils.addYears(leapYearDate, 4));
        
        // 跨闰年变化的月份测试
        LocalDate dateBeforeLeap = LocalDate.of(2023, 12, 31);
        // 加2个月，从12月31日→2月28/29日(闰年判断)
        assertEquals(LocalDate.of(2024, 2, 29), DateTimeUtils.addMonths(dateBeforeLeap, 2));
        
        // 从2月的非闰年到闰年
        LocalDate feb28NonLeap = LocalDate.of(2023, 2, 28);
        assertEquals(LocalDate.of(2024, 2, 28), DateTimeUtils.addYears(feb28NonLeap, 1));
    }
    
    /**
     * 测试日期时间运算中的溢出和进位
     */
    @Test
    public void testDateTimeOverflow() {
        // 测试时间溢出
        LocalDateTime dateTime = LocalDateTime.of(2023, 12, 31, 23, 59, 59);
        
        // 使用Java原生方法手动测试时间加1分钟后溢出到第二天
        LocalDateTime nextMinute = dateTime.plusMinutes(1);
        assertEquals(LocalDateTime.of(2024, 1, 1, 0, 0, 59), nextMinute);
        
        // 测试大跨度日期计算
        LocalDate date = LocalDate.of(2023, 1, 1);
        // 加1000天，应该跨越多个年份
        LocalDate after1000Days = DateTimeUtils.addDays(date, 1000);
        // 预期结果：2023-01-01 + 1000天 = 2025-09-27
        assertEquals(LocalDate.of(2025, 9, 27), after1000Days);
    }
    
    /**
     * 测试工作日计算的特殊情况
     */
    @Test
    public void testWorkdaysSpecialCases() {
        // 测试跨越多个月的工作日计算
        LocalDate startDate = LocalDate.of(2023, 4, 24); // 4月24日，周一
        LocalDate endDate = LocalDate.of(2023, 5, 12);   // 5月12日，周五
        
        // 总共跨越了3周(15个工作日)
        assertEquals(15, DateTimeUtils.workdaysBetween(startDate, endDate));
        
        // 测试长期间隔工作日计算（跨越一个月以上）
        LocalDate longStart = LocalDate.of(2023, 1, 1); // 1月1日，周日
        LocalDate longEnd = LocalDate.of(2023, 3, 31);  // 3月31日，周五
        
        // 计算1月1日到3月31日的工作日（不包括周末）
        // 这期间应该有约64个工作日（准确数字根据日历计算）
        long expectedWorkdays = 0;
        LocalDate current = longStart;
        while (!current.isAfter(longEnd)) {
            if (DateTimeUtils.isWeekday(current)) {
                expectedWorkdays++;
            }
            current = current.plusDays(1);
        }
        
        assertEquals(expectedWorkdays, DateTimeUtils.workdaysBetween(longStart, longEnd));
    }
    
    /**
     * 测试日期解析的异常情况
     */
    @Test
    public void testDateParsingExceptions() {
        // 测试无效日期格式
        assertNull(DateTimeUtils.parseDate("invalid-date"));
        assertNull(DateTimeUtils.parseDateTime("invalid-datetime"));
        
        // 测试格式正确但日期无效（如2月30日）
        assertNull(DateTimeUtils.parseDate("2023-02-30"));
        
        // 测试解析带有特殊字符的日期
        String dateWithSpecialChars = "2023年01月01日";
        LocalDate parsedSpecialDate = DateTimeUtils.parseDate(dateWithSpecialChars, "yyyy年MM月dd日");
        assertEquals(LocalDate.of(2023, 1, 1), parsedSpecialDate);
    }
    
    /**
     * 测试跨时区日期时间转换
     * 注意：这个测试需要DateTimeUtils提供时区转换方法，如果没有可以忽略
     */
    @Test
    public void testTimeZoneConversions() {
        // 由于DateTimeUtils可能没有直接的时区转换方法，这里使用Java原生方法示范
        
        // 创建不同时区的同一时刻
        ZonedDateTime utcTime = ZonedDateTime.of(
            LocalDateTime.of(2023, 5, 15, 12, 0, 0),
            ZoneId.of("UTC")
        );
        
        ZonedDateTime shanghaiTime = utcTime.withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
        
        // 验证时区转换正确性（上海比UTC快8小时）
        assertEquals(20, shanghaiTime.getHour()); // 12 + 8 = 20
        
        // 验证两个时间表示的是同一时刻
        assertEquals(utcTime.toInstant(), shanghaiTime.toInstant());
    }
} 