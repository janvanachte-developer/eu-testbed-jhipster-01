package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.SpecificationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Specification and its DTO SpecificationDTO.
 */
@Mapper(componentModel = "spring", uses = {DomainMapper.class, MetaDataMapper.class, })
public interface SpecificationMapper extends EntityMapper <SpecificationDTO, Specification> {

    @Mapping(source = "domain.id", target = "domainId")

    @Mapping(source = "metaData.id", target = "metaDataId")
    SpecificationDTO toDto(Specification specification); 

    @Mapping(source = "domainId", target = "domain")

    @Mapping(source = "metaDataId", target = "metaData")
    @Mapping(target = "testSuites", ignore = true)
    Specification toEntity(SpecificationDTO specificationDTO); 
    default Specification fromId(Long id) {
        if (id == null) {
            return null;
        }
        Specification specification = new Specification();
        specification.setId(id);
        return specification;
    }
}
