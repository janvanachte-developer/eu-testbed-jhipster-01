package eu.europa.ec.digit.bris.testbed.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TestStep.
 */
@Entity
@Table(name = "test_step")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "teststep")
public class TestStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TestCase testCase;

    @OneToMany(mappedBy = "testStep")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TestArtifact> testArtifacts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public TestStep testCase(TestCase testCase) {
        this.testCase = testCase;
        return this;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public Set<TestArtifact> getTestArtifacts() {
        return testArtifacts;
    }

    public TestStep testArtifacts(Set<TestArtifact> testArtifacts) {
        this.testArtifacts = testArtifacts;
        return this;
    }

    public TestStep addTestArtifacts(TestArtifact testArtifact) {
        this.testArtifacts.add(testArtifact);
        testArtifact.setTestStep(this);
        return this;
    }

    public TestStep removeTestArtifacts(TestArtifact testArtifact) {
        this.testArtifacts.remove(testArtifact);
        testArtifact.setTestStep(null);
        return this;
    }

    public void setTestArtifacts(Set<TestArtifact> testArtifacts) {
        this.testArtifacts = testArtifacts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestStep testStep = (TestStep) o;
        if (testStep.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testStep.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestStep{" +
            "id=" + getId() +
            "}";
    }
}
