package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerCode 值对象测试")
class CustomerCodeTest {

    @Test
    @DisplayName("应该能够创建有效的顾客编码")
    void should_create_valid_customer_code() {
        // Arrange
        String validCode = "C202312001";
        
        // Act
        CustomerCode customerCode = CustomerCode.of(validCode);
        
        // Assert
        assertThat(customerCode).isNotNull();
        assertThat(customerCode.getValue()).isEqualTo(validCode);
    }

    @Test
    @DisplayName("应该能够生成顾客编码")
    void should_generate_customer_code() {
        // Arrange
        String brandCode = "BR001";
        long sequence = 1L;
        
        // Act
        CustomerCode customerCode = CustomerCode.generate(brandCode, sequence);
        
        // Assert
        assertThat(customerCode).isNotNull();
        assertThat(customerCode.getValue()).isNotNull().isNotEmpty();
        assertThat(customerCode.getValue()).startsWith(brandCode);
        assertThat(customerCode.getValue()).isEqualTo("BR00100000001"); // brandCode + 8位数字
    }

    @Test
    @DisplayName("生成的顾客编码应该是唯一的")
    void should_generate_unique_customer_codes() {
        // Arrange
        String brandCode = "BR001";
        
        // Act
        CustomerCode code1 = CustomerCode.generate(brandCode, 1L);
        CustomerCode code2 = CustomerCode.generate(brandCode, 2L);
        
        // Assert
        assertThat(code1.getValue()).isNotEqualTo(code2.getValue());
    }

    @Test
    @DisplayName("创建顾客编码时空值应该抛出异常")
    void should_throw_exception_when_creating_with_null_value() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerCode.of(null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客编码不能为空");
    }

    @Test
    @DisplayName("创建顾客编码时空字符串应该抛出异常")
    void should_throw_exception_when_creating_with_empty_value() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerCode.of(""))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客编码不能为空");
    }

    @Test
    @DisplayName("创建顾客编码时过长值应该抛出异常")
    void should_throw_exception_when_creating_with_too_long_value() {
        // Arrange
        String tooLongCode = "C".repeat(31); // 超过30个字符
        
        // Act & Assert
        assertThatThrownBy(() -> CustomerCode.of(tooLongCode))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客编码长度不能超过30个字符");
    }

    @Test
    @DisplayName("生成顾客编码时品牌编码不能为空")
    void should_throw_exception_when_generating_with_null_brand_code() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerCode.generate(null, 1L))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌编码不能为空");
    }

    @Test
    @DisplayName("生成顾客编码时序号不能为0")
    void should_throw_exception_when_generating_with_zero_sequence() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerCode.generate("BR001", 0L))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("序号必须大于0");
    }

    @Test
    @DisplayName("生成顾客编码时序号不能为负数")
    void should_throw_exception_when_generating_with_negative_sequence() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerCode.generate("BR001", -1L))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("序号必须大于0");
    }

    @Test
    @DisplayName("应该能够验证顾客编码格式")
    void should_validate_customer_code_format() {
        // Act & Assert
        assertThat(CustomerCode.isValid("C202312001")).isTrue();
        assertThat(CustomerCode.isValid("BR00100000123")).isTrue();
        assertThat(CustomerCode.isValid("VALID_CODE")).isTrue();
        
        assertThat(CustomerCode.isValid("")).isFalse();
        assertThat(CustomerCode.isValid(null)).isFalse();
        assertThat(CustomerCode.isValid("A".repeat(31))).isFalse(); // 超过30个字符
    }

    @Test
    @DisplayName("相同值的顾客编码应该相等")
    void should_be_equal_when_same_value() {
        // Arrange
        String codeValue = "C202312001";
        CustomerCode code1 = CustomerCode.of(codeValue);
        CustomerCode code2 = CustomerCode.of(codeValue);
        
        // Assert
        assertThat(code1).isEqualTo(code2);
        assertThat(code1.hashCode()).isEqualTo(code2.hashCode());
    }

    @Test
    @DisplayName("不同值的顾客编码应该不相等")
    void should_not_be_equal_when_different_value() {
        // Arrange
        CustomerCode code1 = CustomerCode.of("C202312001");
        CustomerCode code2 = CustomerCode.of("C202312002");
        
        // Assert
        assertThat(code1).isNotEqualTo(code2);
        assertThat(code1.hashCode()).isNotEqualTo(code2.hashCode());
    }

    @Test
    @DisplayName("toString方法应该返回编码值")
    void should_return_value_in_toString() {
        // Arrange
        String codeValue = "C202312001";
        CustomerCode customerCode = CustomerCode.of(codeValue);
        
        // Act
        String result = customerCode.toString();
        
        // Assert
        assertThat(result).isEqualTo(codeValue);
    }
} 