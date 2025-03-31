
package acme.entities.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LegRepository extends JpaRepository<Leg, Integer> {

	//@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.departureDate ASC")
	Leg findFirstLegByFlightIdOrderByDepartureDate(Integer flightId);

	//@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.departureDate DESC")
	Leg findFirstLegByFlightIdOrderByDepartureDateDesc(Integer flightId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.id = :flightId")
	long countLegsByFlight(Integer flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	List<Leg> findAllLegsByFlight(Integer flightId);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(l) > 0 THEN true
				ELSE false
			END
		FROM Leg l
			WHERE
		l.id != :legId AND
		l.aircraft.id = :aircraftId AND
		(l.departureDate <= :arrivalDate AND l.arrivalDate >= :departureDate)
		""")
	public boolean isAircrafBusy(Integer legId, Integer aircraftId, Date departureDate, Date arrivalDate);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(l) > 0 THEN true
				ELSE false
			END
		FROM Leg l
		WHERE
		l.id != :legId AND
		l.flight.id = :flightId AND
		(l.departureDate <= :arrivalDate AND l.arrivalDate >= :departureDate)
		""")
	public boolean islegOverlapping(Integer legId, Integer flightId, Date departureDate, Date arrivalDate);

}
