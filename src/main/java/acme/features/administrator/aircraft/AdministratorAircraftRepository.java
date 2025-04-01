
package acme.features.administrator.aircraft;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;

public interface AdministratorAircraftRepository extends AbstractRepository {

	@Query("""
		SELECT a FROM Aircraft a
		WHERE a.id = :aircraftId
		""")
	public Optional<Aircraft> findAircraftById(Integer aircraftId);

	@Query("""
		SELECT a FROM Aircraft a
		""")
	public List<Aircraft> findAllAircrafts();

	@Query("""
		SELECT a FROM Airline a
		WHERE a.id = :airlineId
		""")
	public Optional<Airline> findAirlineById(Integer airlineId);

	@Query("SELECT a FROM Airline a")
	public List<Airline> findAllAirlines();

}
