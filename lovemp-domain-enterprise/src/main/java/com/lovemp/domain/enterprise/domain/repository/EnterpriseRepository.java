package com.lovemp.domain.enterprise.domain.repository;

import com.lovemp.common.domain.Page;
import com.lovemp.common.domain.Repository;
import com.lovemp.domain.enterprise.domain.model.aggregate.Enterprise;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseType;

import java.util.List;
import java.util.Optional;

/**
 * 企业存储库接口
 */
public interface EnterpriseRepository extends Repository<Enterprise, EnterpriseId> {
    
    /**
     * 保存企业
     * 
     * @param enterprise 企业实体
     * @return 保存后的企业
     */
    Enterprise save(Enterprise enterprise);
    
    /**
     * 根据ID查找企业
     * 
     * @param id 企业ID
     * @return 企业实体
     */
    Optional<Enterprise> findById(EnterpriseId id);
    
    /**
     * 根据统一社会信用代码查找企业
     * 
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @return 企业实体
     */
    Optional<Enterprise> findByUnifiedSocialCreditCode(String unifiedSocialCreditCode);
    
    /**
     * 根据企业名称模糊查询
     * 
     * @param name 企业名称
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @return 分页企业列表
     */
    Page<Enterprise> findByNameLike(String name, int pageIndex, int pageSize);
    
    /**
     * 查询指定企业类型的企业
     * 
     * @param enterpriseType 企业类型
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @return 分页企业列表
     */
    Page<Enterprise> findByEnterpriseType(EnterpriseType enterpriseType, int pageIndex, int pageSize);
    
    /**
     * 查询指定区域内的企业
     * 
     * @param province 省份
     * @param city 城市
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @return 分页企业列表
     */
    Page<Enterprise> findByRegion(String province, String city, int pageIndex, int pageSize);
    
    /**
     * 获取最近注册的企业
     * 
     * @param limit 数量
     * @return 企业列表
     */
    List<Enterprise> findRecentRegistered(int limit);
    
    /**
     * 查询法定代表人是指定人的企业
     * 
     * @param legalRepresentativeName 法定代表人姓名
     * @param idNumber 法定代表人身份证号
     * @return 企业列表
     */
    List<Enterprise> findByLegalRepresentative(String legalRepresentativeName, String idNumber);
    
    /**
     * 根据ID删除企业
     * 
     * @param id 企业ID
     * @return 是否删除成功
     */
    boolean delete(EnterpriseId id);
    
    /**
     * 统计总企业数
     * 
     * @return 企业数量
     */
    long count();
} 