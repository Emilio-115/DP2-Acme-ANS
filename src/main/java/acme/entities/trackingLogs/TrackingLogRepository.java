
package acme.entities.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface TrackingLogRepository extends AbstractRepository {

	@Query("""
		SELECT tl
		FROM TrackingLog tl
		WHERE tl.claim.id = :claimId AND tl.reclaim = :reclaim
		ORDER BY tl.resolutionPercentage DESC
		""")
	List<TrackingLog> findTopPercentage(int claimId, boolean reclaim);

	@Query("""
		SELECT tl
		FROM TrackingLog tl
		WHERE tl.claim.id = :claimId AND tl.reclaim = :reclaim
		ORDER BY tl.lastUpdateMoment DESC, tl.resolutionPercentage DESC
		""")
	List<TrackingLog> findTopDateReclaim(int claimId, boolean reclaim);
}
