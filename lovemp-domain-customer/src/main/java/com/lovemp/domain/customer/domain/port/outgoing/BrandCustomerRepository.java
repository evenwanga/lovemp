package com.lovemp.domain.customer.domain.port.outgoing;

import com.lovemp.domain.customer.domain.model.aggregate.BrandCustomer;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerCode;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerStatus;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerType;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.util.List;
import java.util.Optional;

/**
 * 品牌顾客仓储接口
 */
public interface BrandCustomerRepository {
    
    /**
     * 根据ID查找品牌顾客
     * 
     * @param id 顾客关系ID
     * @return 品牌顾客
     */
    Optional<BrandCustomer> findById(CustomerRelationId id);
    
    /**
     * 根据顾客编码查找品牌顾客
     * 
     * @param customerCode 顾客编码
     * @return 品牌顾客
     */
    Optional<BrandCustomer> findByCustomerCode(CustomerCode customerCode);
    
    /**
     * 根据品牌ID和自然人ID查找品牌顾客
     * 
     * @param brandId 品牌ID
     * @param personId 自然人ID
     * @return 品牌顾客
     */
    Optional<BrandCustomer> findByBrandIdAndPersonId(String brandId, PersonId personId);
    
    /**
     * 根据品牌ID查找所有顾客
     * 
     * @param brandId 品牌ID
     * @return 品牌顾客列表
     */
    List<BrandCustomer> findByBrandId(String brandId);
    
    /**
     * 根据自然人ID查找所有品牌顾客关系
     * 
     * @param personId 自然人ID
     * @return 品牌顾客列表
     */
    List<BrandCustomer> findByPersonId(PersonId personId);
    
    /**
     * 根据品牌ID和顾客类型查找顾客
     * 
     * @param brandId 品牌ID
     * @param customerType 顾客类型
     * @return 品牌顾客列表
     */
    List<BrandCustomer> findByBrandIdAndCustomerType(String brandId, CustomerType customerType);
    
    /**
     * 根据品牌ID和状态查找顾客
     * 
     * @param brandId 品牌ID
     * @param status 顾客状态
     * @return 品牌顾客列表
     */
    List<BrandCustomer> findByBrandIdAndStatus(String brandId, CustomerStatus status);
    
    /**
     * 根据品牌ID查找VIP顾客
     * 
     * @param brandId 品牌ID
     * @return VIP顾客列表
     */
    List<BrandCustomer> findVipCustomersByBrandId(String brandId);
    
    /**
     * 根据品牌ID和积分范围查找顾客
     * 
     * @param brandId 品牌ID
     * @param minPoints 最小积分
     * @param maxPoints 最大积分
     * @return 品牌顾客列表
     */
    List<BrandCustomer> findByBrandIdAndPointsRange(String brandId, int minPoints, int maxPoints);
    
    /**
     * 根据品牌ID和等级查找顾客
     * 
     * @param brandId 品牌ID
     * @param level 等级
     * @return 品牌顾客列表
     */
    List<BrandCustomer> findByBrandIdAndLevel(String brandId, int level);
    
    /**
     * 获取品牌下一个顾客编码序号
     * 
     * @param brandId 品牌ID
     * @return 下一个序号
     */
    long getNextCustomerSequence(String brandId);
    
    /**
     * 保存品牌顾客
     * 
     * @param brandCustomer 品牌顾客
     */
    void save(BrandCustomer brandCustomer);
    
    /**
     * 批量保存品牌顾客
     * 
     * @param brandCustomers 品牌顾客列表
     */
    void saveAll(List<BrandCustomer> brandCustomers);
    
    /**
     * 删除品牌顾客
     * 
     * @param id 顾客关系ID
     */
    void deleteById(CustomerRelationId id);
    
    /**
     * 检查顾客编码是否存在
     * 
     * @param customerCode 顾客编码
     * @return true-存在，false-不存在
     */
    boolean existsByCustomerCode(CustomerCode customerCode);
    
    /**
     * 检查品牌和自然人的关系是否存在
     * 
     * @param brandId 品牌ID
     * @param personId 自然人ID
     * @return true-存在，false-不存在
     */
    boolean existsByBrandIdAndPersonId(String brandId, PersonId personId);
    
    /**
     * 统计品牌顾客数量
     * 
     * @param brandId 品牌ID
     * @return 顾客数量
     */
    long countByBrandId(String brandId);
    
    /**
     * 统计品牌指定类型顾客数量
     * 
     * @param brandId 品牌ID
     * @param customerType 顾客类型
     * @return 顾客数量
     */
    long countByBrandIdAndCustomerType(String brandId, CustomerType customerType);
} 