
package acme.entities.bookings;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	Long countPassangersByBookingId(Integer bookingId);

	@Query("SELECT b.locatorCode FROM Booking b WHERE b.id <> :bookingId AND b.locatorCode LIKE :locatorCode")
	Optional<String> findLocatorCodeFromDifferentBooking(Integer bookingId, String locatorCode);

}
