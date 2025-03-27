
package acme.features.authenticated.claim;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

public interface AuthenticatedClaimRepository extends AbstractRepository {

	@Query("select aa from AssistanceAgent aa where aa.userAccount = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select c from Claim c where assistanceAgent.id = :id")
	List<Claim> findClaimsByAssistanceAgent(int id);
}
