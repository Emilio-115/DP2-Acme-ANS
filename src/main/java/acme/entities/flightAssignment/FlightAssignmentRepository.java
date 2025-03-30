
package acme.entities.flightAssignment;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMember.FlightCrewMember;

public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fa) > 0 THEN true
				ELSE false
			END
		FROM FlightAssignment fa
		WHERE fa <> :flightAssignment
		AND fa.draftMode = false
		AND fa.status = 'CONFIRMED'
		AND fa.flightCrewMember = :flightCrewMemebr
		AND (fa.leg.departureDate <= :arrivalDate AND fa.leg.arrivalDate >= :departureDate)
		""")
	public boolean isFlightCrewMemberBusy(FlightAssignment flightAssignment, FlightCrewMember flightCrewMember, Date departureDate, Date arrivalDate);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fa) > 0 THEN true
				ELSE false
			END
		FROM FlightAssignment fa
		WHERE fa <> :flightAssignment
		AND fa.leg = :leg
		AND fa.duty = 'PILOT'
		""")
	public boolean pilotDutyTaken(FlightAssignment flightAssignment, Leg leg);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fa) > 0 THEN true
				ELSE false
			END
		FROM FlightAssignment fa
		WHERE fa <> :flightAssignment
		AND fa.leg = :leg
		AND fa.duty = 'COPILOT'
		""")
	public boolean copilotDutyTaken(FlightAssignment flightAssignment, Leg leg);
}
