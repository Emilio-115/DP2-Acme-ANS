
package acme.features.flightCrewMember.activityLog;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;

public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("""
		SELECT al FROM ActivityLog al
		WHERE al.id = :activityLogId
		AND al.registeringAssignment.flightCrewMember.id = :flightCrewMemberId
		""")
	public Optional<ActivityLog> findByIdAndFlightCrewMemberId(Integer activityLogId, Integer flightCrewMemberId);

	@Query("""
		SELECT al FROM ActivityLog al
		WHERE al.registeringAssignment.flightCrewMember.id = :flightCrewMemberId
		""")
	public List<ActivityLog> findAllByFlightCrewMemberId(Integer flightCrewMemberId);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.id = :flightAssignmentId
		AND fa.flightCrewMember.id = :flightCrewMemberId
		AND fa.draftMode = false
		""")
	public Optional<FlightAssignment> findPublishedFlightAssignmentByIdAndFlightCrewMemberId(Integer flightAssignmentId, Integer flightCrewMemberId);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.flightCrewMember.id = :flightCrewMemberId
		AND fa.draftMode = false
		""")
	public List<FlightAssignment> findPublishedFlightAssignmentsByFlightCrewMemberId(Integer flightCrewMemberId);

	@Query("""
		SELECT fa FROM FlightAssignment fa
		WHERE fa.flightCrewMember.id = :flightCrewMemberId
		AND fa.leg.arrivalDate < :cutoff
		AND fa.draftMode = false
		""")
	public List<FlightAssignment> findPublishedFlightAssignmentsByFlightCrewMemberIdLandedBefore(Integer flightCrewMemberId, Date cutoff);

}
