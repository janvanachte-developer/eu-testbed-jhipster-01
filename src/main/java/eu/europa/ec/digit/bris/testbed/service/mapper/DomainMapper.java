package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.DomainDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Domain and its DTO DomainDTO.
 */
@Mapper(componentModel = "spring", uses = {MetaDataMapper.class, })
public interface DomainMapper extends EntityMapper <DomainDTO, Domain> {

    @Mapping(source = "metaData.id", target = "metaDataId")
    DomainDTO toDto(Domain domain); 

    @Mapping(source = "metaDataId", target = "metaData")
    @Mapping(target = "specifications", ignore = true)
    Domain toEntity(DomainDTO domainDTO); 
    default Domain fromId(Long id) {
        if (id == null) {
            return null;
        }
        Domain domain = new Domain();
        domain.setId(id);
        return domain;
    }
}
