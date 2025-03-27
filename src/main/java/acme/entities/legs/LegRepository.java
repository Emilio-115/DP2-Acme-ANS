
package acme.entities.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.aircrafts.Aircraft;
import acme.entities.flights.Flight;

public interface LegRepository extends JpaRepository<Leg, Integer> {

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight ORDER BY l.departureDate ASC")
	Leg findFirstLegByFlight(@Param("flight") Flight flight);

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight ORDER BY l.departureDate DESC")
	Leg findLastLegByFlight(@Param("flight") Flight flight);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight = :flight")
	long countLegsByFlight(@Param("flight") Flight flight);

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight")
	List<Leg> findAllLegsByFlight(@Param("flight") Flight flight);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(l) > 0 THEN true
				ELSE false
			END
		FROM Leg l
			WHERE
		l <> :leg AND
		l.aircraft= :aircraft AND
		(l.departureDate <= :arrivalDate AND l.arrivalDate >= :departureDate)
		""")
	public boolean isAircrafBusy(@Param("leg") Leg leg, @Param("aircraft") Aircraft aircraft, @Param("departureDate") Date departureDate, @Param("arrivalDate") Date arrivalDate);

	@Query("""
		SELECT
			l
		FROM Leg l
		WHERE
		l <> :leg AND
		l.aircraft= :aircraft AND
		(l.departureDate <= :arrivalDate AND l.arrivalDate >= :departureDate)
		""")
	public Leg get(@Param("leg") Leg leg, @Param("aircraft") final Aircraft aircraft, @Param("departureDate") Date departureDate, @Param("arrivalDate") Date arrivalDate);

}
