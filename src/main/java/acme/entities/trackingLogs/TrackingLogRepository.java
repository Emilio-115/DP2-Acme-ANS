
package acme.entities.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface TrackingLogRepository extends AbstractRepository {

	@Query("""
		SELECT tl
		FROM TrackingLog tl
		WHERE tl.claim.id = :claimId
		ORDER BY tl.resolutionPercentage DESC
		""")
	List<TrackingLog> findTopPercentage(int claimId);
}
