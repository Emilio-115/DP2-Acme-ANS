
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT aa FROM AssistanceAgent aa WHERE aa.userAccount.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("SELECT c FROM Claim c WHERE assistanceAgent.id = :id")
	List<Claim> findClaimsByAssistanceAgent(int id);

	@Query("SELECT c FROM Claim c WHERE assistanceAgent.id = :id AND c.id = :claimId")
	Claim findClaimByAssistanceAgent(int id, int claimId);

	Optional<Claim> findByIdAndAssistanceAgentId(Integer id, Integer agentId);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT c.leg FROM Claim c WHERE c.id = :id")
	Leg findLegByClaimId(int id);

	@Query("SELECT l FROM Leg l WHERE l.status = :st")
	Collection<Leg> findAllLandedLegs(LegStatus st);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(Integer id);

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId")
	List<TrackingLog> findAllTrackingLogsByClaimId(int claimId);
}
