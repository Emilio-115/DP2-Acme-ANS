
package acme.entities.bookings;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface BookingRepository extends AbstractRepository {

	@Query("SELECT COUNT(br) FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	Long countPassangersByBookingId(Integer bookingId);

	@Query("SELECT b.locatorCode FROM Booking b WHERE b.id <> :bookingId AND b.locatorCode LIKE :locatorCode")
	Optional<String> findLocatorCodeFromDifferentBooking(Integer bookingId, String locatorCode);

	@Query("SELECT br FROM BookingRecord br WHERE br.id <> :id AND br.associatedBooking.id = :bookingId AND br.associatedPassenger.id = :passengerId")
	Optional<BookingRecord> findBookingRecordByBookingAndPassenger(Integer bookingId, Integer passengerId, Integer id);

	@Query("SELECT p.draftMode FROM Passenger p WHERE p.id = :id")
	boolean findPassengerDrafModeById(Integer id);

}
