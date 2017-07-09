package eu.europa.ec.digit.bris.testbed.service.mapper;

import eu.europa.ec.digit.bris.testbed.domain.*;
import eu.europa.ec.digit.bris.testbed.service.dto.ActorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Actor and its DTO ActorDTO.
 */
@Mapper(componentModel = "spring", uses = {TestSuiteMapper.class, })
public interface ActorMapper extends EntityMapper <ActorDTO, Actor> {

    @Mapping(source = "testSuite.id", target = "testSuiteId")
    ActorDTO toDto(Actor actor); 

    @Mapping(source = "testSuiteId", target = "testSuite")
    Actor toEntity(ActorDTO actorDTO); 
    default Actor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Actor actor = new Actor();
        actor.setId(id);
        return actor;
    }
}
