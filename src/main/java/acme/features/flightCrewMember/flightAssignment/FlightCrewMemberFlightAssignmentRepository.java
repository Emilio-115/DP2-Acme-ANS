
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

	@Query("SELECT fa FROM FlightAssignment fa")
	List<FlightAssignment> findFlightAssignments();
	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.leg.departureTime >= :cutoff
		AND fa.draftMode = false
		""")
	List<FlightAssignment> findPlannedFlightAssignments(Date cutoff);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.leg.departureTime <= :cutoff
		AND fa.draftMode = false
		""")
	List<FlightAssignment> findInProgressFlightAssignments(Date cutoff);

	@Query("""
		SELECT fa FROM FlightAssignment fa¡
		AND fa.draftMode = true
		""")
	List<FlightAssignment> findDraftFlightAssignments();

	List<FlightAssignment> findAllByFlightCrewMemberId(Integer flightCrewMemberId);

	Optional<FlightAssignment> findByIdAndFlightCrewMemberId(Integer id, Integer flightCrewMemberId);

	@Query("""
		select fcm from FlightCrewMember fcm
		where fcm.availabilityStatus = 'AVAILABLE'
		""")
	List<FlightCrewMember> findAvailableFlightCrewMembers();

	@Query("select fcm from FlightCrewMember fcm")
	List<FlightCrewMember> findAllCrewMembers();

	@Query("SELECT l from Leg l WHERE l.id = :id")
	Optional<Leg> findLegById(Integer id);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select l from Leg l where l.departureDate > :cutoff")
	List<Leg> findLegsDepartingAfter(Date cutoff);

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.id = :id")
	Optional<FlightCrewMember> findFlightCrewMemberById(Integer id);

}
