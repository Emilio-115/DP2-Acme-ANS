
package acme.entities.legs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.flights.Flight;

public interface LegRepository extends JpaRepository<Leg, Integer> {

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight ORDER BY l.departureDate ASC")
	Leg findFirstLegByFlight(@Param("flight") Flight flight);

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight ORDER BY l.departureDate DESC")
	Leg findLastLegByFlight(@Param("flight") Flight flight);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight = :flight")
	long countLegsByFlight(@Param("flight") Flight flight);
}
