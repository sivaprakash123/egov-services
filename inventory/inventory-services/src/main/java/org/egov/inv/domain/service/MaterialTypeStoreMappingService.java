package org.egov.inv.domain.service;

import org.egov.common.Constants;
import org.egov.common.DomainService;
import org.egov.common.MdmsRepository;
import org.egov.common.Pagination;
import org.egov.common.exception.ErrorCode;
import org.egov.common.exception.InvalidDataException;
import org.egov.inv.model.*;
import org.egov.inv.persistence.entity.MaterialTypeStoreMappingEntity;
import org.egov.inv.persistence.repository.MaterialTypeStoreJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class MaterialTypeStoreMappingService extends DomainService {

    @Value("${inv.materialtypestoremapping.save.topic}")
    private String saveTopic;

    @Value("${inv.materialtypestoremapping.save.key}")
    private String saveTopicKey;

    @Autowired
    private MdmsRepository mdmsRepository;

    @Autowired
    private MaterialTypeStoreJdbcRepository typeStoreMappingJdbcRepository;

    @Autowired
    private StoreService storeService;

    public MaterialTypeStoreResponse create(MaterialTypeStoreRequest materialTypeStoreRequest, String tenantId) {
        List<MaterialTypeStoreMapping> materialTypeStores = materialTypeStoreRequest.getMaterialTypeStores();

       // validate(materialTypeStores, tenantId, Constants.ACTION_CREATE);
        for (MaterialTypeStoreMapping materialTypeStoreMapping : materialTypeStores) {
            setTenant(tenantId, materialTypeStoreMapping);
            materialTypeStoreMapping.setId(typeStoreMappingJdbcRepository.getSequence("seq_materialtypestoremapping"));
        }

        kafkaQue.send(saveTopic, saveTopicKey, materialTypeStoreRequest);
        MaterialTypeStoreResponse materialTypeStoreResponse = new MaterialTypeStoreResponse();
        materialTypeStoreResponse.setResponseInfo(null);
        materialTypeStoreResponse.setMaterialTypeStores(materialTypeStoreRequest.getMaterialTypeStores());

        return materialTypeStoreResponse;
    }

    private void setTenant(String tenantId, MaterialTypeStoreMapping materialTypeStoreMapping) {
        if (isEmpty(materialTypeStoreMapping.getTenantId())) {
            materialTypeStoreMapping.setTenantId(tenantId);
        }
    }

    public MaterialTypeStoreResponse update(MaterialTypeStoreRequest materialTypeStoreRequest, String tenantId) {
        List<MaterialTypeStoreMapping> materialTypeStores = materialTypeStoreRequest.getMaterialTypeStores();
       // validate(materialTypeStores, tenantId, Constants.ACTION_UPDATE);

        for (MaterialTypeStoreMapping materialTypeStoreMapping : materialTypeStores) {
            setTenant(tenantId, materialTypeStoreMapping);
        }
        kafkaQue.send(saveTopic, saveTopicKey, materialTypeStoreRequest);
        MaterialTypeStoreResponse materialTypeStoreResponse = new MaterialTypeStoreResponse();
        materialTypeStoreResponse.setResponseInfo(null);
        materialTypeStoreResponse.setMaterialTypeStores(materialTypeStoreRequest.getMaterialTypeStores());

        return materialTypeStoreResponse;
    }

    public MaterialTypeStoreResponse search(MaterialTypeStoreMappingSearch materialTypeStoreMappingSearch) {
        MaterialTypeStoreResponse materialTypeStoreResponse = new MaterialTypeStoreResponse();
        Pagination<MaterialTypeStoreMapping> storeMappingPagination = typeStoreMappingJdbcRepository.search(materialTypeStoreMappingSearch);
        if (storeMappingPagination.getPagedData().size() > 0) {
            materialTypeStoreResponse.setMaterialTypeStores(null);
            materialTypeStoreResponse.setMaterialTypeStores(storeMappingPagination.getPagedData());
            return materialTypeStoreResponse;
        } else {
            materialTypeStoreResponse.setMaterialTypeStores(null);
            materialTypeStoreResponse.setMaterialTypeStores(Collections.EMPTY_LIST);
            return materialTypeStoreResponse;
        }
    }


    private void validate(List<MaterialTypeStoreMapping> materialTypeStoreMappings, String tenantId, String method) {
        InvalidDataException errors = new InvalidDataException();
        try {
            switch (method) {

                case Constants.ACTION_CREATE: {
                    if (materialTypeStoreMappings == null) {
                        throw new InvalidDataException("materialstore", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }
                break;

                case Constants.ACTION_UPDATE: {
                    if (materialTypeStoreMappings == null) {
                        throw new InvalidDataException("materialstore", ErrorCode.NOT_NULL.getCode(), null);
                    }
                }

                break;
            }
            for (MaterialTypeStoreMapping materialTypeStoreMapping : materialTypeStoreMappings) {
                int i = 0;

                if (!typeStoreMappingJdbcRepository.uniqueCheck("materialtype", "store", new MaterialTypeStoreMappingEntity().toEntity(materialTypeStoreMapping))) {
                    errors.addDataError(ErrorCode.COMBINATION_EXISTS.getCode(), "Material type and ", "store", String.valueOf(i));
                }

                if (null != materialTypeStoreMapping && null != materialTypeStoreMapping.getMaterialType().getCode()) {
                    MaterialType materialType = (MaterialType) mdmsRepository.fetchObject(tenantId, "inventory", "MaterialType", "code", materialTypeStoreMapping.getMaterialType().getCode(), MaterialType.class);
                    materialTypeStoreMapping.setMaterialType(materialType);
                }

                if (null != materialTypeStoreMapping.getStore() && !isEmpty(materialTypeStoreMapping.getStore().getCode())) {
                    StoreGetRequest storeGetRequest = StoreGetRequest.builder()
                            .code(Collections.singletonList(materialTypeStoreMapping.getStore().getCode()))
                            .tenantId(tenantId)
                            .build();

                    StoreResponse storeResponse = storeService.search(storeGetRequest);
                    if (storeResponse.getStores().size() == 0) {
                        errors.addDataError(ErrorCode.STORE_NOT_EXIST.getCode(), materialTypeStoreMapping.getStore().getCode());
                    }
                }

                i++;
            }
        } catch (IllegalArgumentException e) {

        }
        if (errors.getValidationErrors().size() > 0) {
            throw errors;
        }
    }


}
