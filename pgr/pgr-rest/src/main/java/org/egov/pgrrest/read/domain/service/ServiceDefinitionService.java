package org.egov.pgrrest.read.domain.service;

import org.egov.pgrrest.common.model.ServiceDefinition;
import org.egov.pgrrest.read.domain.model.ServiceDefinitionSearchCriteria;
import org.egov.pgrrest.read.persistence.repository.ServiceDefinitionRepository;
import org.egov.pgrrest.read.web.contract.ServiceDefinitionRequest;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class ServiceDefinitionService {

    private ServiceDefinitionRepository repository;

    public ServiceDefinitionService(ServiceDefinitionRepository repository) {
        this.repository = repository;
    }

    public ServiceDefinition find(ServiceDefinitionSearchCriteria searchCriteria) {
        return repository.find(searchCriteria);
    }

    public ServiceDefinition create(ServiceDefinitionRequest request) {
        throw new NotImplementedException();
    }
}
