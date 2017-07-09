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
 * A TestSuite.
 */
@Entity
@Table(name = "test_suite")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "testsuite")
public class TestSuite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Specification specification;

    @OneToOne
    @JoinColumn(unique = true)
    private MetaData metaData;

    @OneToMany(mappedBy = "testSuite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Actor> actors = new HashSet<>();

    @OneToMany(mappedBy = "testSuite")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TestCase> testCases = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Specification getSpecification() {
        return specification;
    }

    public TestSuite specification(Specification specification) {
        this.specification = specification;
        return this;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public TestSuite metaData(MetaData metaData) {
        this.metaData = metaData;
        return this;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public TestSuite actors(Set<Actor> actors) {
        this.actors = actors;
        return this;
    }

    public TestSuite addActors(Actor actor) {
        this.actors.add(actor);
        actor.setTestSuite(this);
        return this;
    }

    public TestSuite removeActors(Actor actor) {
        this.actors.remove(actor);
        actor.setTestSuite(null);
        return this;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Set<TestCase> getTestCases() {
        return testCases;
    }

    public TestSuite testCases(Set<TestCase> testCases) {
        this.testCases = testCases;
        return this;
    }

    public TestSuite addTestCases(TestCase testCase) {
        this.testCases.add(testCase);
        testCase.setTestSuite(this);
        return this;
    }

    public TestSuite removeTestCases(TestCase testCase) {
        this.testCases.remove(testCase);
        testCase.setTestSuite(null);
        return this;
    }

    public void setTestCases(Set<TestCase> testCases) {
        this.testCases = testCases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestSuite testSuite = (TestSuite) o;
        if (testSuite.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testSuite.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestSuite{" +
            "id=" + getId() +
            "}";
    }
}
