package org.openmrs.sync.core.service;

import org.openmrs.sync.core.entity.MockedEntity;
import org.openmrs.sync.core.model.MockedModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AbstractEntityServiceTest {

    @Mock
    private MockedOpenMrsRepository repository;

    @Mock
    private Function<MockedEntity, MockedModel> etyToModelMapper;

    @Mock
    private Function<MockedModel, MockedEntity> modelToEtyMapper;

    private MockedEntityService mockedEntityService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        mockedEntityService = new MockedEntityService(repository, etyToModelMapper, modelToEtyMapper);
    }

    @Test
    public void getModels() {
        // Given
        MockedEntity mockedEntity1 = new MockedEntity(1, "uuid1");
        MockedEntity mockedEntity2 = new MockedEntity(2, "uuid2");
        MockedModel mockedModel1 = new MockedModel("uuid1");
        MockedModel mockedModel2 = new MockedModel("uuid2");
        when(repository.findAll()).thenReturn(Arrays.asList(mockedEntity1, mockedEntity2));
        when(etyToModelMapper.apply(mockedEntity1)).thenReturn(mockedModel1);
        when(etyToModelMapper.apply(mockedEntity2)).thenReturn(mockedModel2);

        // When
        List<MockedModel> result = mockedEntityService.getModels();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(model -> model.equals(mockedModel1)));
        assertTrue(result.stream().anyMatch(model -> model.equals(mockedModel2)));
    }

    @Test
    public void save_entity_exists() {
        // Given
        MockedModel mockedModel = new MockedModel("uuid");
        MockedEntity mockedEntity= new MockedEntity(null, "uuid");
        MockedEntity mockedEntityInDb = new MockedEntity(1, "uuid");
        when(repository.findByUuid("uuid")).thenReturn(mockedEntityInDb);
        when(repository.save(mockedEntityInDb)).thenReturn(mockedEntity);
        when(modelToEtyMapper.apply(mockedModel)).thenReturn(mockedEntity);
        when(etyToModelMapper.apply(mockedEntityInDb)).thenReturn(mockedModel);

        // When
        MockedModel result = mockedEntityService.save(mockedModel);

        // Then
        assertEquals(mockedModel, result);
        verify(repository).save(mockedEntityInDb);
    }

    @Test
    public void save_entity_does_not_exist() {
        // Given
        MockedModel mockedModel = new MockedModel("uuid");
        MockedEntity mockedEntity= new MockedEntity(null, "uuid");
        when(repository.findByUuid("uuid")).thenReturn(null);
        when(repository.save(mockedEntity)).thenReturn(mockedEntity);
        when(modelToEtyMapper.apply(mockedModel)).thenReturn(mockedEntity);
        when(etyToModelMapper.apply(mockedEntity)).thenReturn(mockedModel);

        // When
        MockedModel result = mockedEntityService.save(mockedModel);

        // Then
        assertEquals(mockedModel, result);
        verify(repository).save(mockedEntity);
    }
}