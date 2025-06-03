package com.lovemp.domain.labor.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentEventId;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

/**
 * 离职完成事件
 */
@Getter
public class EmploymentTerminatedEvent extends DomainEvent {
    
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
    
    public EmploymentTerminatedEvent(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            PersonId personId,
            EnterpriseId enterpriseId,
            BrandId brandId) {
        super();
        this.laborResourceId = laborResourceId;
        this.employmentEventId = employmentEventId;
        this.personId = personId;
        this.enterpriseId = enterpriseId;
        this.brandId = brandId;
    }
}