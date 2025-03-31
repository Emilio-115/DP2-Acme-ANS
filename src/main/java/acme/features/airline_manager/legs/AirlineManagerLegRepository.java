
package acme.features.airline_manager.legs;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

public interface AirlineManagerLegRepository extends AbstractRepository {

	@Query("SELECT l FROM Leg l WHERE l.flight.manager.id = :managerId")
	List<Leg> findAllByAirlineManagerId(Integer managerId);

	Optional<Leg> findLegById(Integer legId);

	Optional<Flight> findByIdAndManagerId(Integer flightId, Integer managerId);

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	List<Leg> findAllByFlightId(Integer flightId);

	Optional<Airport> findAirportById(Integer airportId);

	Optional<Aircraft> findAircraftById(Integer aircraftId);
}
