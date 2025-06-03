package com.lovemp.domain.labor.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentEventId;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 发起离职事件
 */
@Getter
public class EmploymentLeavingInitiatedEvent extends DomainEvent {
    
    /**
     * 劳动力资源ID
     */
    private final LaborResourceId laborResourceId;
    
    /**
     * 雇佣事件ID
     */
    private final EmploymentEventId employmentEventId;
    
    /**
     * 自然人ID
     */
    private final PersonId personId;
    
    /**
     * 企业ID
     */
    private final EnterpriseId enterpriseId;
    
    /**
     * 品牌ID
     */
    private final BrandId brandId;
    
    /**
     * 离职日期
     */
    private final LocalDate leavingDate;
    
    public EmploymentLeavingInitiatedEvent(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            PersonId personId,
            EnterpriseId enterpriseId,
            BrandId brandId,
            LocalDate leavingDate) {
        super();
        this.laborResourceId = laborResourceId;
        this.employmentEventId = employmentEventId;
        this.personId = personId;
        this.enterpriseId = enterpriseId;
        this.brandId = brandId;
        this.leavingDate = leavingDate;
    }
}