package org.openmrs.sync.component.service.light.impl;

import org.openmrs.sync.component.entity.light.ConceptDatatypeLight;
import org.openmrs.sync.component.repository.OpenmrsRepository;
import org.openmrs.sync.component.service.light.AbstractLightService;
import org.springframework.stereotype.Service;

@Service
public class ConceptDatatypeLightService extends AbstractLightService<ConceptDatatypeLight> {

    public ConceptDatatypeLightService(final OpenmrsRepository<ConceptDatatypeLight> repository) {
        super(repository);
    }

    @Override
    protected ConceptDatatypeLight createPlaceholderEntity(final String uuid) {
        ConceptDatatypeLight conceptDatatype = new ConceptDatatypeLight();
        conceptDatatype.setDateCreated(DEFAULT_DATE);
        conceptDatatype.setCreator(DEFAULT_USER_ID);
        conceptDatatype.setName(DEFAULT_STRING);
        return conceptDatatype;
    }
}
