package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.MetaDataDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MetaData and its DTO MetaDataDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetaDataMapper extends EntityMapper <MetaDataDTO, MetaData> {
    
    
    default MetaData fromId(Long id) {
        if (id == null) {
            return null;
        }
        MetaData metaData = new MetaData();
        metaData.setId(id);
        return metaData;
    }
}
