
package acme.realms.flightCrewMember;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("""
		SELECT
			CASE
				WHEN COUNT(fcm) = 0 THEN true
				ELSE false
			END
		FROM FlightCrewMember fcm
		WHERE fcm.id != :flightCrewMemberId
		AND fcm.employeeCode = :employeeCode
		""")
	boolean isEmployeeCodeFree(Integer flightCrewMemberId, String employeeCode);

}
