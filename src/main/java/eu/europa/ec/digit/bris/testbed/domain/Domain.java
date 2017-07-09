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
 * A Domain.
 */
@Entity
@Table(name = "domain")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "domain")
public class Domain implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private MetaData metaData;

    @OneToMany(mappedBy = "domain")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Specification> specifications = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public Domain metaData(MetaData metaData) {
        this.metaData = metaData;
        return this;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Set<Specification> getSpecifications() {
        return specifications;
    }

    public Domain specifications(Set<Specification> specifications) {
        this.specifications = specifications;
        return this;
    }

    public Domain addSpecifications(Specification specification) {
        this.specifications.add(specification);
        specification.setDomain(this);
        return this;
    }

    public Domain removeSpecifications(Specification specification) {
        this.specifications.remove(specification);
        specification.setDomain(null);
        return this;
    }

    public void setSpecifications(Set<Specification> specifications) {
        this.specifications = specifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Domain domain = (Domain) o;
        if (domain.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), domain.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Domain{" +
            "id=" + getId() +
            "}";
    }
}
