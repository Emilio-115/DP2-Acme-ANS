
package acme.entities.aircrafts;

import java.util.Date;
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

	@Query("""
		SELECT
			CASE
				WHEN COUNT(l) > 0 THEN true
				ELSE false
			END
		FROM Leg l
			WHERE
		l.aircraft.id = :aircraftId AND
		l.departureDate >= :date AND
		l.draftMode = false
		""")
	public boolean isAircraftBusyInTheFuture(Integer aircraftId, Date date);

}
