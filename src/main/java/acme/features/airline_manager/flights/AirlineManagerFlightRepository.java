
package acme.features.airline_manager.flights;

import java.util.List;
import java.util.Optional;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;

public interface AirlineManagerFlightRepository extends AbstractRepository {

	List<Flight> findAllByManagerId(Integer managerId);

	Optional<Flight> findByIdAndManagerId(Integer flightId, Integer managerId);

	Optional<Flight> findFlightById(Integer flightId);

}
