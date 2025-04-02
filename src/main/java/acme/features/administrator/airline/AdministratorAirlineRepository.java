
package acme.features.administrator.airline;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airlines.Airline;

public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("""
		SELECT a FROM Airline a
		WHERE a.id = :airlineId
		""")
	public Optional<Airline> findAirlineById(Integer airlineId);

	@Query("SELECT a FROM Airline a")
	public List<Airline> findAllAirlines();
}
