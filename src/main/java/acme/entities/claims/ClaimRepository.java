
package acme.entities.claims;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog tl WHERE tl.claim.id = :id ORDER BY tl.resolutionPercentage DESC")
	List<TrackingLog> getAllTrackingLogFromClaim(int id);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim getClaimById(int id);

}
