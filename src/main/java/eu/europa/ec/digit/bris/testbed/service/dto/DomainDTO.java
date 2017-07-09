package eu.europa.ec.digit.bris.testbed.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Domain entity.
 */
public class DomainDTO implements Serializable {

    private Long id;

    private Long metaDataId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMetaDataId() {
        return metaDataId;
    }

    public void setMetaDataId(Long metaDataId) {
        this.metaDataId = metaDataId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DomainDTO domainDTO = (DomainDTO) o;
        if(domainDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), domainDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DomainDTO{" +
            "id=" + getId() +
            "}";
    }
}
