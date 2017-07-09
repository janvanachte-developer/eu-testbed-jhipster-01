package eu.europa.ec.digit.bris.testbed.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TestArtifact.
 */
@Entity
@Table(name = "test_artifact")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "testartifact")
public class TestArtifact implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "encofing")
    private String encofing;

    @Column(name = "uri")
    private String uri;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne
    private TestStep testStep;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TestArtifact name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public TestArtifact type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncofing() {
        return encofing;
    }

    public TestArtifact encofing(String encofing) {
        this.encofing = encofing;
        return this;
    }

    public void setEncofing(String encofing) {
        this.encofing = encofing;
    }

    public String getUri() {
        return uri;
    }

    public TestArtifact uri(String uri) {
        this.uri = uri;
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getContent() {
        return content;
    }

    public TestArtifact content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TestStep getTestStep() {
        return testStep;
    }

    public TestArtifact testStep(TestStep testStep) {
        this.testStep = testStep;
        return this;
    }

    public void setTestStep(TestStep testStep) {
        this.testStep = testStep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestArtifact testArtifact = (TestArtifact) o;
        if (testArtifact.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testArtifact.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestArtifact{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", encofing='" + getEncofing() + "'" +
            ", uri='" + getUri() + "'" +
            ", content='" + getContent() + "'" +
            "}";
    }
}
