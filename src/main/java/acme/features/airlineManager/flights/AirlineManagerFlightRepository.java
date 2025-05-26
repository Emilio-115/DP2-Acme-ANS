
package acme.features.airlineManager.flights;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	List<Flight> findAllByManagerId(Integer managerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId AND f.manager.id = :managerId")
	Optional<Flight> findByIdAndManagerId(Integer flightId, Integer managerId);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId")
	Optional<Flight> findFlightById(Integer flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	List<Leg> findAllLegsByFlightId(Integer flightId);

}
