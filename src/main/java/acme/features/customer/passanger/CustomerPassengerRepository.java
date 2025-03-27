
package acme.features.customer.passanger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.passengers.Passenger;

public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT br.associatedPassenger FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(Integer bookingId);
}
