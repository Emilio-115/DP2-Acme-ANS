
package acme.features.airlineManager.flights;

import java.util.List;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;

public interface AirlineManagerFlightRepository extends AbstractRepository {

	List<Flight> findAllByManagerId(Integer managerId);

}
