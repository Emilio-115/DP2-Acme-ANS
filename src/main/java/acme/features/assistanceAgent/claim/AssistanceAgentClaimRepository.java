
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
import acme.realms.AssistanceAgent;

public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select aa from AssistanceAgent aa where aa.userAccount.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("select c from Claim c where assistanceAgent.id = :id AND c.completed = :completed")
	List<Claim> findClaimsByAssistanceAgent(int id, boolean completed);

	@Query("select c from Claim c where assistanceAgent.id = :id and c.id = :claimId")
	Claim findClaimByAssistanceAgent(int id, int claimId);

	Optional<Claim> findByIdAndAssistanceAgentId(Integer id, Integer agentId);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select c.leg from Claim c where c.id = :id")
	Leg findLegByClaimId(int id);

	@Query("select l from Leg l where l.status = :st")
	Collection<Leg> findAllLandedLegs(LegStatus st);

	@Query("select l from Leg l")
	Collection<Leg> findAllLegs();

	@Query("select c from Claim c where c.id = :id")
	Claim findClaimById(Integer id);

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId")
	List<TrackingLog> findAllTrackingLogsByClaimId(int claimId);
}
