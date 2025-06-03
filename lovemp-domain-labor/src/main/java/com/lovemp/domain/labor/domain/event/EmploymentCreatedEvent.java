package com.lovemp.domain.labor.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentEventId;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentType;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 雇佣关系创建事件
 */
@Getter
public class EmploymentCreatedEvent extends DomainEvent {
    
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
     * 雇佣类型
     */
    private final EmploymentType employmentType;
    
    /**
     * 雇佣开始日期
     */
    private final LocalDate startDate;
    
    public EmploymentCreatedEvent(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            PersonId personId,
            EnterpriseId enterpriseId,
            BrandId brandId,
            EmploymentType employmentType,
            LocalDate startDate) {
        super();
        this.laborResourceId = laborResourceId;
        this.employmentEventId = employmentEventId;
        this.personId = personId;
        this.enterpriseId = enterpriseId;
        this.brandId = brandId;
        this.employmentType = employmentType;
        this.startDate = startDate;
    }
}