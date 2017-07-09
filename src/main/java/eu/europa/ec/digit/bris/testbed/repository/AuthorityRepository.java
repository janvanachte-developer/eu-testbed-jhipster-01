package eu.europa.ec.digit.bris.testbed.repository;

import eu.europa.ec.digit.bris.testbed.domain.Authority;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
