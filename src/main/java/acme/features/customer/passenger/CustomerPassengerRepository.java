
package acme.features.customer.passenger;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.passengers.Passenger;

public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT br.associatedPassenger FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(Integer bookingId);

	@Query("SELECT b FROM Booking b WHERE b.id = :bookingId AND b.customer.id = :customerId")
	Optional<Booking> findBookingByIdAndCustomerId(Integer bookingId, Integer customerId);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :customerId")
	List<Passenger> findPassengersByCustomerId(Integer customerId);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id AND p.customer.id = :customerId")
	Optional<Passenger> findPassengerByIdAndCustomerId(Integer id, Integer customerId);

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(Integer id);
}
