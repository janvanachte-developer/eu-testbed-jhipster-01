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
 * A Specification.
 */
@Entity
@Table(name = "specification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "specification")
public class Specification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Domain domain;

    @OneToOne
    @JoinColumn(unique = true)
    private MetaData metaData;

    @OneToMany(mappedBy = "specification")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TestSuite> testSuites = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public Specification domain(Domain domain) {
        this.domain = domain;
        return this;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public Specification metaData(MetaData metaData) {
        this.metaData = metaData;
        return this;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Set<TestSuite> getTestSuites() {
        return testSuites;
    }

    public Specification testSuites(Set<TestSuite> testSuites) {
        this.testSuites = testSuites;
        return this;
    }

    public Specification addTestSuites(TestSuite testSuite) {
        this.testSuites.add(testSuite);
        testSuite.setSpecification(this);
        return this;
    }

    public Specification removeTestSuites(TestSuite testSuite) {
        this.testSuites.remove(testSuite);
        testSuite.setSpecification(null);
        return this;
    }

    public void setTestSuites(Set<TestSuite> testSuites) {
        this.testSuites = testSuites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Specification specification = (Specification) o;
        if (specification.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), specification.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Specification{" +
            "id=" + getId() +
            "}";
    }
}
