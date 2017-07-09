package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.TestArtifactDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestArtifact and its DTO TestArtifactDTO.
 */
@Mapper(componentModel = "spring", uses = {TestStepMapper.class, })
public interface TestArtifactMapper extends EntityMapper <TestArtifactDTO, TestArtifact> {

    @Mapping(source = "testStep.id", target = "testStepId")
    TestArtifactDTO toDto(TestArtifact testArtifact); 

    @Mapping(source = "testStepId", target = "testStep")
    TestArtifact toEntity(TestArtifactDTO testArtifactDTO); 
    default TestArtifact fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestArtifact testArtifact = new TestArtifact();
        testArtifact.setId(id);
        return testArtifact;
    }
}
