
package acme.features.customer.bookingRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;

public interface CustomerBookingRecordRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId AND b.draftMode = true")
	List<Booking> findPublishedBookingsByCustomerId(Integer customerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :customerId AND p.draftMode = false")
	List<Passenger> findPublishedPassengersByCustomerId(Integer customerId);

	@Query("SELECT b FROM Booking b WHERE b.id = :id")
	Booking findBookingById(Integer id);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(Integer id);

}
