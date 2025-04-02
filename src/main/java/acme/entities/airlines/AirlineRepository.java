
package acme.entities.airlines;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirlineRepository extends AbstractRepository {

	@Query("""
		SELECT
			CASE
				WHEN COUNT(a) > 0 THEN true
				ELSE false
			END
		FROM Airline a
		WHERE a.iataCode = :iataCode
		AND a.id <> :airlineId
		""")
	public boolean isIataCodeTaken(String iataCode, Integer airlineId);
}
