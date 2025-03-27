
package acme.features.authenticated.assistancesAgents;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;

public interface AuthenticatedAssistanceAgentRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select c from Claim c where assistanceAgent.id = :id")
	List<Claim> findClaimsByAssistanceAgent(int id);
}
