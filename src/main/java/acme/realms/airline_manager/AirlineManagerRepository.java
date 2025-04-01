
package acme.realms.airline_manager;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AirlineManagerRepository extends AbstractRepository {

	@Query("""
			SELECT a
			FROM AirlineManager a
			WHERE
			a.id != :airlineManagerId AND
			a.identifier = :identifier
		""")
	List<AirlineManager> findAllAirlineManagersWithIndentifier(Integer airlineManagerId, String identifier);

}
