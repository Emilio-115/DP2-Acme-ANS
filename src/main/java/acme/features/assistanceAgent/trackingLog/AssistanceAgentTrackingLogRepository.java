
package acme.features.assistanceAgent.trackingLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :claimId")
	List<TrackingLog> findAllTrackingLogsByClaimId(int claimId);

	@Query("SELECT aa FROM AssistanceAgent aa WHERE aa.userAccount.id = :id")
	AssistanceAgent findAssistanceAgentById(int id);

	@Query("SELECT tl FROM TrackingLog tl")
	List<TrackingLog> findAllTrackingLogs();

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(int id);
}
