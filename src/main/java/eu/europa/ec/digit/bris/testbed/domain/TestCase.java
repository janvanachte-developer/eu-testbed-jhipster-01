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
 * A TestCase.
 */
@Entity
@Table(name = "test_case")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "testcase")
public class TestCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TestSuite testSuite;

    @OneToOne
    @JoinColumn(unique = true)
    private MetaData metaData;

    @OneToMany(mappedBy = "testCase")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TestStep> steps = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TestSuite getTestSuite() {
        return testSuite;
    }

    public TestCase testSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
        return this;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public TestCase metaData(MetaData metaData) {
        this.metaData = metaData;
        return this;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Set<TestStep> getSteps() {
        return steps;
    }

    public TestCase steps(Set<TestStep> testSteps) {
        this.steps = testSteps;
        return this;
    }

    public TestCase addSteps(TestStep testStep) {
        this.steps.add(testStep);
        testStep.setTestCase(this);
        return this;
    }

    public TestCase removeSteps(TestStep testStep) {
        this.steps.remove(testStep);
        testStep.setTestCase(null);
        return this;
    }

    public void setSteps(Set<TestStep> testSteps) {
        this.steps = testSteps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestCase testCase = (TestCase) o;
        if (testCase.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testCase.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestCase{" +
            "id=" + getId() +
            "}";
    }
}
