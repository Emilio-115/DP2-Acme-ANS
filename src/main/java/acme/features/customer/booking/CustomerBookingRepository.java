
package acme.features.customer.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;
import acme.entities.bookings.Booking;

public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :id")
	Booking findBookingById(Integer id);

	List<Booking> findAllByCustomerId(Integer customerId);

	Optional<Booking> findByIdAndCustomerId(Integer id, Integer customerId);

}
