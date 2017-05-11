package org.egov.pgrrest.common.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "attribute_definition")
public class AttributeDefinition extends AbstractPersistable<AttributeDefinitionKey> {
    @EmbeddedId
    private AttributeDefinitionKey id;

    @Column(name = "variable")
    private char variable;

    @Column(name = "datatype")
    private String dataType;

    @Column(name = "required")
    private char required;

    @Column(name = "datatypedescription")
    private String dataTypeDescription;

    @Column(name = "ordernum")
    private int order;

    @Column(name = "description")
    private String description;
}

