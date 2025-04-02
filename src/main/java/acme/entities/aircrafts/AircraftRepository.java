
package acme.entities.aircrafts;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AircraftRepository extends AbstractRepository {

	@Query("""
		SELECT a
		FROM Aircraft a
		WHERE
		a.id != :aircraftId AND
		a.registrationNumber = :registrationNumber
		""")
	public List<Aircraft> findAllByRegistrationNumber(Integer aircraftId, String registrationNumber);

}
