
package acme.entities.legs;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.leg.ValidLeg;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
@Table(indexes = {
	@Index(columnList = "departureDate"), @Index(columnList = "arrivalDate"), @Index(columnList = "departureDate, arrivalDate"), @Index(columnList = "departureDate, draftMode"), @Index(columnList = "id, departureDate, draftMode"),
	@Index(columnList = "departureDate, arrivalDate, draftMode"),
})
public class Leg extends AbstractEntity {

	/**
	 * A flight aggregates several legs. A leg represents an individual segment of a flight, typically corresponding to layovers or connections.
	 * The system must store the follow-ing data for each leg:
	 * - A unique flight number (composed of the airline's IATA code followed by four digits, unique)
	 * - a scheduled departure
	 * - a scheduled arrival
	 * - a duration in hours
	 * - a status ("ON TIME", "DELAYED", "CANCELLED", "LANDED").
	 * - departure airport
	 * - arrival airports
	 * - aircraft
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[0-9]{4}$")
	@Automapped
	private String				flightNumberDigits;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				departureDate;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				arrivalDate;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode			= true;


	@Transient
	public int durationInHours() {
		Instant departureInstant = this.departureDate.toInstant();
		Instant arrivalInstant = this.arrivalDate.toInstant();

		Duration duration = Duration.between(departureInstant, arrivalInstant);

		return duration.toHoursPart();
	}

	@Transient
	public String flightNumber() {

		if (this.aircraft != null)
			return this.aircraft.getAirline().getIataCode() + this.flightNumberDigits;
		return "No Data";
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

}
