
package acme.entities.flightAssignment;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fa) = 0 THEN true
				ELSE false
			END
		FROM FlightAssignment fa
		WHERE fa.id <> :flightAssignmentId
		AND fa.draftMode = false
		AND fa.status = acme.entities.flightAssignment.FlightAssignmentStatus.CONFIRMED
		AND fa.flightCrewMember.id = :flightCrewMemberId
		AND (fa.leg.departureDate <= :arrivalDate AND fa.leg.arrivalDate >= :departureDate)
		""")
	public boolean isFlightCrewMemberFree(Integer flightAssignmentId, Integer flightCrewMemberId, Date departureDate, Date arrivalDate);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fa) = 0 THEN true
				ELSE false
			END
		FROM FlightAssignment fa
		WHERE fa.id <> :flightAssignmentId
		AND fa.draftMode = false
		AND fa.status = acme.entities.flightAssignment.FlightAssignmentStatus.CONFIRMED
		AND fa.leg.id = :legId
		AND fa.duty = :duty
		""")
	public boolean isDutyFree(Integer flightAssignmentId, Integer legId, FlightCrewDuty duty);

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fa) = 0 THEN true
				ELSE false
			END
		FROM FlightAssignment fa
		WHERE fa.id <> :flightAssignmentId
		AND fa.draftMode = false
		AND fa.status != acme.entities.flightAssignment.FlightAssignmentStatus.CANCELLED
		AND fa.leg.id = :legId
		AND fa.flightCrewMember.id = :flightCrewMemberId
		AND fa.duty = :duty
		""")
	public boolean isLegMemberDutyUnique(Integer flightAssignmentId, Integer legId, Integer flightCrewMemberId, FlightCrewDuty duty);

}
