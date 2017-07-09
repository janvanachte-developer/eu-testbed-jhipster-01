package eu.europa.ec.digit.bris.testbed.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the MetaData entity.
 */
public class MetaDataDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String version;

    private String authors;

    private String description;

    private ZonedDateTime published;

    private ZonedDateTime lastModified;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getPublished() {
        return published;
    }

    public void setPublished(ZonedDateTime published) {
        this.published = published;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MetaDataDTO metaDataDTO = (MetaDataDTO) o;
        if(metaDataDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), metaDataDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MetaDataDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", version='" + getVersion() + "'" +
            ", authors='" + getAuthors() + "'" +
            ", description='" + getDescription() + "'" +
            ", published='" + getPublished() + "'" +
            ", lastModified='" + getLastModified() + "'" +
            "}";
    }
}
