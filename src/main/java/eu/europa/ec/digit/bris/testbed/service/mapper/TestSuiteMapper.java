package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.TestSuiteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TestSuite and its DTO TestSuiteDTO.
 */
@Mapper(componentModel = "spring", uses = {SpecificationMapper.class, MetaDataMapper.class, })
public interface TestSuiteMapper extends EntityMapper <TestSuiteDTO, TestSuite> {

    @Mapping(source = "specification.id", target = "specificationId")

    @Mapping(source = "metaData.id", target = "metaDataId")
    TestSuiteDTO toDto(TestSuite testSuite); 

    @Mapping(source = "specificationId", target = "specification")

    @Mapping(source = "metaDataId", target = "metaData")
    @Mapping(target = "actors", ignore = true)
    @Mapping(target = "testCases", ignore = true)
    TestSuite toEntity(TestSuiteDTO testSuiteDTO); 
    default TestSuite fromId(Long id) {
        if (id == null) {
            return null;
        }
        TestSuite testSuite = new TestSuite();
        testSuite.setId(id);
        return testSuite;
    }
}
