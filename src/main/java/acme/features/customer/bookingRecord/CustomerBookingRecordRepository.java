
package acme.features.customer.bookingRecord;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;

public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
	List<Booking> findBookingsByCustomerId(Integer customerId);

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.draftMode = true")
	List<Booking> findNonPublisedBookingsByCustomerId(Integer customerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :customerId AND p.draftMode = false")
	List<Passenger> findPublishedPassengersByCustomerId(Integer customerId);

	@Query("SELECT b FROM Booking b WHERE b.id = :id")
	Booking findBookingById(Integer id);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(Integer id);

	@Query("SELECT br FROM BookingRecord br WHERE br.id = :id AND br.associatedBooking.customer.id = :customerId")
	Optional<BookingRecord> findByBookingRecordIdAndCustomerId(Integer id, Integer customerId);

	@Query("SELECT br FROM BookingRecord br WHERE br.id = :id")
	BookingRecord findBookingRecordById(Integer id);

	@Query("SELECT br FROM BookingRecord br WHERE br.associatedBooking.customer.id = :customerId")
	List<BookingRecord> findBookingRecordByCustomerId(Integer customerId);

}
