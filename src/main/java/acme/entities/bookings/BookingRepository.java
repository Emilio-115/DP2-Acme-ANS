
package acme.entities.bookings;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	Long countPassangersByBookingId(Integer bookingId);

}
