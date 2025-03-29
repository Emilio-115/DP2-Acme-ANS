
package acme.features.assistanceAgent.claim;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select aa from AssistanceAgent aa where aa.userAccount.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select c from Claim c where assistanceAgent.id = :id AND c.completed = :completed")
	List<Claim> findClaimsByAssistanceAgent(int id, boolean completed);

	Optional<Claim> findByIdAndAssistanceAgentId(Integer id, Integer agentId);

	@Query("select l from Leg l where l.id = :id AND l.status = LANDED")
	Leg findLegById(int id);

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(Integer id);
}
