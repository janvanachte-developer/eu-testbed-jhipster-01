package eu.europa.ec.digit.bris.testbed.service.dto;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the TestStep entity.
 */
public class TestStepDTO implements Serializable {

    private Long id;

    private Long testCaseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(Long testCaseId) {
        this.testCaseId = testCaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestStepDTO testStepDTO = (TestStepDTO) o;
        if(testStepDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testStepDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestStepDTO{" +
            "id=" + getId() +
            "}";
    }
}
