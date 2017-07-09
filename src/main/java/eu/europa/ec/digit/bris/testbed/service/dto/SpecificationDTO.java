package eu.europa.ec.digit.bris.testbed.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Specification entity.
 */
public class SpecificationDTO implements Serializable {

    private Long id;

    private Long domainId;

    private Long metaDataId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
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

        SpecificationDTO specificationDTO = (SpecificationDTO) o;
        if(specificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), specificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpecificationDTO{" +
            "id=" + getId() +
            "}";
    }
}
