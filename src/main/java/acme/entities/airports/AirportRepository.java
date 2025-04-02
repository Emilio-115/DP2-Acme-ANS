
package acme.entities.airports;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirportRepository extends AbstractRepository {

	@Query("""
		SELECT
			CASE
				WHEN COUNT(a) > 0 THEN true
				ELSE false
			END
		FROM Airport a
		WHERE a.iataCode = :iataCode
		AND a.id <> :airportId
		""")
	public boolean isIataCodeTaken(String iataCode, Integer airportId);
}
