package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.TestCaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestCase and its DTO TestCaseDTO.
 */
@Mapper(componentModel = "spring", uses = {TestSuiteMapper.class, MetaDataMapper.class, })
public interface TestCaseMapper extends EntityMapper <TestCaseDTO, TestCase> {

    @Mapping(source = "testSuite.id", target = "testSuiteId")

    @Mapping(source = "metaData.id", target = "metaDataId")
    TestCaseDTO toDto(TestCase testCase); 

    @Mapping(source = "testSuiteId", target = "testSuite")

    @Mapping(source = "metaDataId", target = "metaData")
    @Mapping(target = "steps", ignore = true)
    TestCase toEntity(TestCaseDTO testCaseDTO); 
    default TestCase fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestCase testCase = new TestCase();
        testCase.setId(id);
        return testCase;
    }
}
