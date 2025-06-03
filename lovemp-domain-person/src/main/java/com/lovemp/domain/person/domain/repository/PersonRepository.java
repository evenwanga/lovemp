package com.lovemp.domain.person.domain.repository;

import com.lovemp.common.domain.Repository;
import com.lovemp.common.domain.Page;
import com.lovemp.domain.person.domain.model.aggregate.Person;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import com.lovemp.domain.person.domain.model.valueobject.IdentityDocument;
import com.lovemp.domain.person.domain.model.valueobject.PersonStatus;

import java.util.Optional;

/**
 * 自然人仓储接口
 */
public interface PersonRepository extends Repository<Person, PersonId> {
    
    /**
     * 根据证件信息查找自然人
     * 
     * @param document 证件信息
     * @return 自然人可选结果
     */
    Optional<Person> findByIdentityDocument(IdentityDocument document);
    
    /**
     * 根据证件号码查找自然人
     * 
     * @param documentNumber 证件号码
     * @return 自然人可选结果
     */
    Optional<Person> findByDocumentNumber(String documentNumber);
    
    /**
     * 根据姓名模糊查询自然人
     * 
     * @param name 姓名
     * @param pageIndex 页码（从0开始）
     * @param pageSize 每页大小
     * @return 自然人分页结果
     */
    Page<Person> findByNameLike(String name, int pageIndex, int pageSize);
    
    /**
     * 根据手机号码查找自然人
     * 
     * @param mobile 手机号码
     * @return 自然人可选结果
     */
    Optional<Person> findByMobile(String mobile);
    
    /**
     * 根据状态查询自然人列表
     * 
     * @param status 状态
     * @param pageIndex 页码（从0开始）
     * @param pageSize 每页大小
     * @return 自然人分页结果
     */
    Page<Person> findByStatus(PersonStatus status, int pageIndex, int pageSize);
    
    /**
     * 统计特定状态的自然人数量
     * 
     * @param status 状态
     * @return 数量
     */
    long countByStatus(PersonStatus status);
    
    /**
     * 保存自然人聚合根
     * 
     * @param person 自然人聚合根
     * @return 保存后的自然人聚合根
     */
    Person save(Person person);
}