
package acme.features.airline_manager.flights;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

public interface AirlineManagerFlightRepository extends AbstractRepository {

	List<Flight> findAllByManagerId(Integer managerId);

	Optional<Flight> findByIdAndManagerId(Integer flightId, Integer managerId);

	Optional<Flight> findFlightById(Integer flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	List<Leg> findAllLegsByFlightId(Integer flightId);

}
