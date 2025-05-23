
package acme.entities.bookings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLocatorCode;
import acme.constraints.booking.ValidBooking;
import acme.entities.flights.Flight;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
@Table(indexes = {
	@Index(columnList = "id, locatorCode"), @Index(columnList = "id, customer_id")
})
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Optional
	@ValidString(pattern = "^\\d{4}$")
	@Automapped
	private String				creditCardLastNibble;


	@Transient
	public Money price() {
		BookingRepository repository = SpringHelper.getBean(BookingRepository.class);
		Long numberPassengers = repository.countPassengersByBookingId(this.getId());
		Money price = new Money();
		if (this.getFlight() != null) {
			price.setCurrency(this.flight.getCost().getCurrency());
			price.setAmount(this.flight.getCost().getAmount() * numberPassengers);
		} else {
			price.setCurrency("EUR");
			price.setAmount(0.);
		}
		return price;
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer	customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

}
