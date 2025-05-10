
package acme.entities.bookings;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.constraints.bookingRecord.ValidBookingRecord;
import acme.entities.passengers.Passenger;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBookingRecord
@Table(indexes = {
	@Index(columnList = "id, associated_booking_id, associated_passenger_id")
})
public class BookingRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				associatedBooking;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Passenger			associatedPassenger;

}
