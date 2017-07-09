package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.TestStepDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestStep and its DTO TestStepDTO.
 */
@Mapper(componentModel = "spring", uses = {TestCaseMapper.class, })
public interface TestStepMapper extends EntityMapper <TestStepDTO, TestStep> {

    @Mapping(source = "testCase.id", target = "testCaseId")
    TestStepDTO toDto(TestStep testStep); 

    @Mapping(source = "testCaseId", target = "testCase")
    @Mapping(target = "testArtifacts", ignore = true)
    TestStep toEntity(TestStepDTO testStepDTO); 
    default TestStep fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestStep testStep = new TestStep();
        testStep.setId(id);
        return testStep;
    }
}
