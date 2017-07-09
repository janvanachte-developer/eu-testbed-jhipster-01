package eu.europa.ec.digit.bris.testbed.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TestSuite entity.
 */
public class TestSuiteDTO implements Serializable {

    private Long id;

    private Long specificationId;

    private Long metaDataId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Long specificationId) {
        this.specificationId = specificationId;
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

        TestSuiteDTO testSuiteDTO = (TestSuiteDTO) o;
        if(testSuiteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testSuiteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestSuiteDTO{" +
            "id=" + getId() +
            "}";
    }
}
