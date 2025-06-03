package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.Getter;

/**
 * 企业资源账号类型枚举
 */
@Getter
public enum ResourceAccountType implements ValueObject {
    
    /**
     * 微信公众号
     */
    WECHAT_OFFICIAL_ACCOUNT("微信公众号", "WOA"),
    
    /**
     * 微信小程序
     */
    WECHAT_MINI_PROGRAM("微信小程序", "WMP"),
    
    /**
     * 微信企业号
     */
    WECHAT_ENTERPRISE_ACCOUNT("微信企业号", "WEA"),
    
    /**
     * 支付宝商家号
     */
    ALIPAY_MERCHANT_ACCOUNT("支付宝商家号", "AMA"),
    
    /**
     * 支付宝小程序
     */
    ALIPAY_MINI_PROGRAM("支付宝小程序", "AMP"),
    
    /**
     * 字节跳动小程序
     */
    BYTEDANCE_MINI_PROGRAM("字节跳动小程序", "BMP"),
    
    /**
     * 微博企业账号
     */
    WEIBO_ENTERPRISE_ACCOUNT("微博企业账号", "WBA"),
    
    /**
     * 抖音企业号
     */
    DOUYIN_ENTERPRISE_ACCOUNT("抖音企业号", "DEA"),
    
    /**
     * 快手企业号
     */
    KUAISHOU_ENTERPRISE_ACCOUNT("快手企业号", "KEA"),
    
    /**
     * 小红书企业号
     */
    XIAOHONGSHU_ENTERPRISE_ACCOUNT("小红书企业号", "XEA"),
    
    /**
     * 企业邮箱
     */
    ENTERPRISE_EMAIL("企业邮箱", "EEM"),
    
    /**
     * 企业网站
     */
    ENTERPRISE_WEBSITE("企业网站", "EWS"),
    
    /**
     * APP开发者账号
     */
    APP_DEVELOPER_ACCOUNT("APP开发者账号", "ADA"),
    
    /**
     * 商标注册账号
     */
    TRADEMARK_REGISTRATION_ACCOUNT("商标注册账号", "TRA"),
    
    /**
     * 专利注册账号
     */
    PATENT_REGISTRATION_ACCOUNT("专利注册账号", "PRA"),
    
    /**
     * 域名注册账号
     */
    DOMAIN_REGISTRATION_ACCOUNT("域名注册账号", "DRA"),
    
    /**
     * 云服务账号
     */
    CLOUD_SERVICE_ACCOUNT("云服务账号", "CSA"),
    
    /**
     * iOS开发者账号
     */
    IOS_DEVELOPER_ACCOUNT("iOS开发者账号", "IDA"),
    
    /**
     * iOS证书 - 开发证书
     */
    IOS_DEVELOPMENT_CERTIFICATE("iOS开发证书", "IDC"),
    
    /**
     * iOS证书 - 发布证书
     */
    IOS_DISTRIBUTION_CERTIFICATE("iOS发布证书", "IDSC"),
    
    /**
     * iOS证书 - 推送证书
     */
    IOS_PUSH_CERTIFICATE("iOS推送证书", "IPC"),
    
    /**
     * Android开发者账号
     */
    ANDROID_DEVELOPER_ACCOUNT("Android开发者账号", "ANDA"),
    
    /**
     * 其他平台账号
     */
    OTHER("其他平台账号", "OTHER");
    
    /**
     * 账号类型名称
     */
    private final String name;
    
    /**
     * 账号类型代码
     */
    private final String code;
    
    ResourceAccountType(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    /**
     * 根据代码获取资源账号类型
     * 
     * @param code 账号类型代码
     * @return 资源账号类型枚举
     */
    public static ResourceAccountType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("账号类型代码不能为空");
        }
        
        for (ResourceAccountType type : ResourceAccountType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的账号类型代码: " + code);
    }
    
    /**
     * 根据名称获取资源账号类型
     * 
     * @param name 账号类型名称
     * @return 资源账号类型枚举
     */
    public static ResourceAccountType fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("账号类型名称不能为空");
        }
        
        for (ResourceAccountType type : ResourceAccountType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的账号类型名称: " + name);
    }
    
    /**
     * 是否为微信相关账号
     * 
     * @return 如果是微信相关账号返回true
     */
    public boolean isWechatRelated() {
        return this == WECHAT_OFFICIAL_ACCOUNT || this == WECHAT_MINI_PROGRAM || 
               this == WECHAT_ENTERPRISE_ACCOUNT;
    }
    
    /**
     * 是否为支付宝相关账号
     * 
     * @return 如果是支付宝相关账号返回true
     */
    public boolean isAlipayRelated() {
        return this == ALIPAY_MERCHANT_ACCOUNT || this == ALIPAY_MINI_PROGRAM;
    }
    
    /**
     * 是否为社交媒体账号
     * 
     * @return 如果是社交媒体账号返回true
     */
    public boolean isSocialMediaAccount() {
        return this == WEIBO_ENTERPRISE_ACCOUNT || this == DOUYIN_ENTERPRISE_ACCOUNT ||
               this == KUAISHOU_ENTERPRISE_ACCOUNT || this == XIAOHONGSHU_ENTERPRISE_ACCOUNT;
    }
    
    /**
     * 是否为小程序账号
     * 
     * @return 如果是小程序账号返回true
     */
    public boolean isMiniProgramAccount() {
        return this == WECHAT_MINI_PROGRAM || this == ALIPAY_MINI_PROGRAM || 
               this == BYTEDANCE_MINI_PROGRAM;
    }
    
    /**
     * 是否为知识产权相关账号
     * 
     * @return 如果是知识产权相关账号返回true
     */
    public boolean isIntellectualPropertyAccount() {
        return this == TRADEMARK_REGISTRATION_ACCOUNT || this == PATENT_REGISTRATION_ACCOUNT ||
               this == DOMAIN_REGISTRATION_ACCOUNT;
    }
    
    /**
     * 是否为iOS相关账号或证书
     * 
     * @return 如果是iOS相关账号或证书返回true
     */
    public boolean isIOSRelated() {
        return this == IOS_DEVELOPER_ACCOUNT || this == IOS_DEVELOPMENT_CERTIFICATE || 
               this == IOS_DISTRIBUTION_CERTIFICATE || this == IOS_PUSH_CERTIFICATE;
    }
    
    /**
     * 是否为iOS证书（不包括开发者账号）
     * 
     * @return 如果是iOS证书返回true
     */
    public boolean isIOSCertificate() {
        return this == IOS_DEVELOPMENT_CERTIFICATE || this == IOS_DISTRIBUTION_CERTIFICATE || 
               this == IOS_PUSH_CERTIFICATE;
    }
    
    /**
     * 是否为移动应用开发相关账号
     * 
     * @return 如果是移动应用开发相关账号返回true
     */
    public boolean isMobileAppDeveloperAccount() {
        return this == APP_DEVELOPER_ACCOUNT || this == IOS_DEVELOPER_ACCOUNT || 
               this == ANDROID_DEVELOPER_ACCOUNT;
    }
    
    /**
     * 返回资源账号类型的名称
     * 
     * @return 资源账号类型的名称
     */
    @Override
    public String toString() {
        return this.name;
    }
} 