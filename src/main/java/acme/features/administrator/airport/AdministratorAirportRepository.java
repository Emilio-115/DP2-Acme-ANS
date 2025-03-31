
package acme.features.administrator.airport;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.airports.Airport;

public interface AdministratorAirportRepository extends AbstractRepository {

	@Query("""
		SELECT a FROM Airport a
		WHERE a.id = :airportId
		""")
	public Optional<Airport> findAirportById(Integer airportId);

	@Query("""
		SELECT a FROM Airport a
		""")
	public List<Airport> findAirports();

}
