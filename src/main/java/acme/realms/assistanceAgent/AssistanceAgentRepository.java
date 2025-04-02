
package acme.realms.assistanceAgent;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("""
			SELECT
			CASE
				WHEN COUNT(aa) = 0 THEN true
				ELSE false
			END
		FROM AssistanceAgent aa
		WHERE aa.id != :assistanceAgentId
		AND aa.employeeCode = :employeeCode
		""")
	boolean isEmployeeCodeFree(Integer assistanceAgentId, String employeeCode);

}
