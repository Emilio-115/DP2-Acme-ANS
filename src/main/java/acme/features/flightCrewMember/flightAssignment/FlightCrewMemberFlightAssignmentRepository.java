
package acme.features.flightCrewMember.flightAssignment;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMember.FlightCrewMember;

public interface FlightCrewMemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	Optional<FlightAssignment> findFlightAssignmentById(Integer id);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.id = :id
		AND fa.flightCrewMember.id = :flightCrewMemberId
		""")
	Optional<FlightAssignment> findFlightAssignmentByIdAndFlightCrewMemberId(Integer id, Integer flightCrewMemberId);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.leg.departureDate >= :cutoff
		AND fa.draftMode = false
		""")
	List<FlightAssignment> findPlannedFlightAssignments(Date cutoff);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.leg.departureDate <= :cutoff
		AND fa.draftMode = false
		""")
	List<FlightAssignment> findInProgressFlightAssignments(Date cutoff);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.draftMode = true
		AND fa.flightCrewMember.id = :flightCrewMemberId
		""")
	List<FlightAssignment> findDraftFlightAssignmentsByFlightCrewMemberId(Integer flightCrewMemberId);

	List<FlightAssignment> findAllByFlightCrewMemberId(Integer flightCrewMemberId);

	Optional<FlightAssignment> findByIdAndFlightCrewMemberId(Integer id, Integer flightCrewMemberId);

	@Query("""
		select fcm from FlightCrewMember fcm
		where fcm.availabilityStatus = acme.realms.flightCrewMember.FlightCrewMemberAvailabilityStatus.AVAILABLE
		""")
	List<FlightCrewMember> findAvailableFlightCrewMembers();

	@Query("select fcm from FlightCrewMember fcm")
	List<FlightCrewMember> findAllCrewMembers();

	@Query("SELECT l from Leg l WHERE l.id = :id")
	Optional<Leg> findLegById(Integer id);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("""
		SELECT l FROM Leg l
		WHERE l.departureDate > :cutoff
		AND l.flight.draftMode = false
		AND NOT EXISTS(
			SELECT fa
			FROM FlightAssignment fa
			WHERE fa.id <> :flightAssignmentId
			AND fa.draftMode = false
			AND fa.status = acme.entities.flightAssignment.FlightAssignmentStatus.CONFIRMED
			AND fa.flightCrewMember.id = :flightCrewMemberId
			AND (fa.leg.departureDate <= l.arrivalDate AND fa.leg.arrivalDate >= l.departureDate)
		)
		""")
	List<Leg> findLegsDepartingAfterWhereFlightCrewMemberIsFree(Date cutoff, Integer flightAssignmentId, Integer flightCrewMemberId);

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.id = :id")
	Optional<FlightCrewMember> findFlightCrewMemberById(Integer id);

}
