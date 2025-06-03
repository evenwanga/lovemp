package com.lovemp.domain.customer.domain.port.outgoing;

import com.lovemp.domain.customer.domain.model.entity.CustomerSharing;
import com.lovemp.domain.customer.domain.model.valueobject.AuthLevel;
import com.lovemp.domain.customer.domain.model.valueobject.SharingId;
import com.lovemp.domain.customer.domain.model.valueobject.SharingStatus;
import com.lovemp.domain.customer.domain.model.valueobject.SharingType;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 顾客共享仓储接口
 */
public interface CustomerSharingRepository {
    
    /**
     * 根据ID查找顾客共享
     * 
     * @param id 共享ID
     * @return 顾客共享
     */
    Optional<CustomerSharing> findById(SharingId id);
    
    /**
     * 根据源品牌ID和目标品牌ID查找共享关系
     * 
     * @param sourceBrandId 源品牌ID
     * @param targetBrandId 目标品牌ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findBySourceBrandIdAndTargetBrandId(String sourceBrandId, String targetBrandId);
    
    /**
     * 根据源品牌ID、目标品牌ID和自然人ID查找共享关系
     * 
     * @param sourceBrandId 源品牌ID
     * @param targetBrandId 目标品牌ID
     * @param personId 自然人ID
     * @return 顾客共享
     */
    Optional<CustomerSharing> findBySourceBrandIdAndTargetBrandIdAndPersonId(String sourceBrandId, 
                                                                           String targetBrandId, 
                                                                           PersonId personId);
    
    /**
     * 根据自然人ID查找所有共享关系
     * 
     * @param personId 自然人ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findByPersonId(PersonId personId);
    
    /**
     * 根据源品牌ID查找所有共享关系
     * 
     * @param sourceBrandId 源品牌ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findBySourceBrandId(String sourceBrandId);
    
    /**
     * 根据目标品牌ID查找所有共享关系
     * 
     * @param targetBrandId 目标品牌ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findByTargetBrandId(String targetBrandId);
    
    /**
     * 根据共享类型查找共享关系
     * 
     * @param sharingType 共享类型
     * @return 顾客共享列表
     */
    List<CustomerSharing> findBySharingType(SharingType sharingType);
    
    /**
     * 根据状态查找共享关系
     * 
     * @param status 共享状态
     * @return 顾客共享列表
     */
    List<CustomerSharing> findByStatus(SharingStatus status);
    
    /**
     * 根据授权级别查找共享关系
     * 
     * @param authLevel 授权级别
     * @return 顾客共享列表
     */
    List<CustomerSharing> findByAuthLevel(AuthLevel authLevel);
    
    /**
     * 查找即将过期的共享关系
     * 
     * @param beforeDate 截止日期
     * @return 顾客共享列表
     */
    List<CustomerSharing> findExpiringSharings(LocalDate beforeDate);
    
    /**
     * 查找已过期的共享关系
     * 
     * @param currentDate 当前日期
     * @return 顾客共享列表
     */
    List<CustomerSharing> findExpiredSharings(LocalDate currentDate);
    
    /**
     * 根据品牌ID和自然人ID查找有效的共享关系
     * 
     * @param brandId 品牌ID（可以是源品牌或目标品牌）
     * @param personId 自然人ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findActiveSharingsByBrandIdAndPersonId(String brandId, PersonId personId);
    
    /**
     * 根据源品牌ID和自然人ID查找有效的共享关系
     * 
     * @param sourceBrandId 源品牌ID
     * @param personId 自然人ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findActiveSharingsBySourceBrandIdAndPersonId(String sourceBrandId, PersonId personId);
    
    /**
     * 根据目标品牌ID和自然人ID查找有效的共享关系
     * 
     * @param targetBrandId 目标品牌ID
     * @param personId 自然人ID
     * @return 顾客共享列表
     */
    List<CustomerSharing> findActiveSharingsByTargetBrandIdAndPersonId(String targetBrandId, PersonId personId);
    
    /**
     * 保存顾客共享
     * 
     * @param customerSharing 顾客共享
     */
    void save(CustomerSharing customerSharing);
    
    /**
     * 批量保存顾客共享
     * 
     * @param customerSharings 顾客共享列表
     */
    void saveAll(List<CustomerSharing> customerSharings);
    
    /**
     * 删除顾客共享
     * 
     * @param id 共享ID
     */
    void deleteById(SharingId id);
    
    /**
     * 检查共享关系是否存在
     * 
     * @param sourceBrandId 源品牌ID
     * @param targetBrandId 目标品牌ID
     * @param personId 自然人ID
     * @return true-存在，false-不存在
     */
    boolean existsBySourceBrandIdAndTargetBrandIdAndPersonId(String sourceBrandId, 
                                                            String targetBrandId, 
                                                            PersonId personId);
    
    /**
     * 统计品牌的共享关系数量
     * 
     * @param brandId 品牌ID（可以是源品牌或目标品牌）
     * @return 共享关系数量
     */
    long countByBrandId(String brandId);
    
    /**
     * 统计源品牌的共享关系数量
     * 
     * @param sourceBrandId 源品牌ID
     * @return 共享关系数量
     */
    long countBySourceBrandId(String sourceBrandId);
    
    /**
     * 统计目标品牌的共享关系数量
     * 
     * @param targetBrandId 目标品牌ID
     * @return 共享关系数量
     */
    long countByTargetBrandId(String targetBrandId);
} 