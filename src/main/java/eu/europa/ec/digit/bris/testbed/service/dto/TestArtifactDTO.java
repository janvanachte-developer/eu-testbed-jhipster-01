package eu.europa.ec.digit.bris.testbed.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TestArtifact entity.
 */
public class TestArtifactDTO implements Serializable {

    private Long id;

    private String name;

    private String type;

    private String encofing;

    private String uri;

    @Lob
    private String content;

    private Long testStepId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncofing() {
        return encofing;
    }

    public void setEncofing(String encofing) {
        this.encofing = encofing;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTestStepId() {
        return testStepId;
    }

    public void setTestStepId(Long testStepId) {
        this.testStepId = testStepId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestArtifactDTO testArtifactDTO = (TestArtifactDTO) o;
        if(testArtifactDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testArtifactDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestArtifactDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", encofing='" + getEncofing() + "'" +
            ", uri='" + getUri() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
