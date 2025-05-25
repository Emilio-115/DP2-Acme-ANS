
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.flights.Flight;
import acme.entities.passengers.Passenger;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(Integer id);

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :customerId")
	List<Booking> findAllByCustomerId(Integer customerId);

	@Query("SELECT b FROM Booking b WHERE b.id = :id AND b.customer.id = :customerId")
	Optional<Booking> findByIdAndCustomerId(Integer id, Integer customerId);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = false AND NOT EXISTS(SELECT l FROM Leg l WHERE l.flight = f AND l.departureDate <= :currentMoment)")
	Collection<Flight> findAvailableFlights(Date currentMoment);

	@Query("SELECT f FROM Flight f WHERE f.id = :id")
	Flight findFlightById(Integer id);

	@Query("SELECT f.draftMode FROM Flight f WHERE f.id = :id")
	Optional<Boolean> findFlightDraftmodeValueById(Integer id);

	@Query("SELECT count(f)>0 FROM Flight f WHERE f.id = :id AND f.draftMode = false AND NOT EXISTS(SELECT l FROM Leg l WHERE l.flight.id = :id AND l.departureDate <= :currentMoment)")
	Boolean checkFlightIsAvailableById(Integer id, Date currentMoment);

	@Query("SELECT br FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	List<BookingRecord> findBookingRecordsByBookingId(Integer bookingId);

	@Query("SELECT br.associatedPassenger FROM BookingRecord br WHERE br.associatedBooking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(Integer bookingId);

}
