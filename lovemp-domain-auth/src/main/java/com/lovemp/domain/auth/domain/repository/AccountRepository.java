package com.lovemp.domain.auth.domain.repository;

import com.lovemp.domain.auth.domain.model.aggregate.Account;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.util.List;
import java.util.Optional;

/**
 * 账号仓储接口
 * 
 * <p>定义账号聚合根的持久化操作</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public interface AccountRepository {
    
    /**
     * 根据ID查找账号
     * 
     * @param accountId 账号ID
     * @return 账号聚合根，如果不存在则返回空
     */
    Optional<Account> findById(AccountId accountId);
    
    /**
     * 根据用户名查找账号
     * 
     * @param username 用户名
     * @return 账号聚合根，如果不存在则返回空
     */
    Optional<Account> findByUsername(String username);
    
    /**
     * 根据邮箱查找账号
     * 
     * @param email 邮箱
     * @return 账号聚合根，如果不存在则返回空
     */
    Optional<Account> findByEmail(String email);
    
    /**
     * 根据自然人ID查找账号
     * 
     * @param personId 自然人ID
     * @return 账号聚合根，如果不存在则返回空
     */
    Optional<Account> findByPersonId(PersonId personId);
    
    /**
     * 查找所有账号
     * 
     * @return 账号列表
     */
    List<Account> findAll();
    
    /**
     * 保存账号
     * 
     * @param account 账号聚合根
     */
    void save(Account account);
    
    /**
     * 删除账号
     * 
     * @param accountId 账号ID
     */
    void deleteById(AccountId accountId);
    
    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByEmail(String email);
} 