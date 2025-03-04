
package acme.entities.legs;

import java.beans.Transient;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;
import acme.entities.airports.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
	private String				fightNumberDigits;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	private Date				departureDate;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	private Date				arrivalDate;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory

	@Valid

	@ManyToOne(optional = false)
	private Airport				departureAirport;

	@Mandatory

	@Valid

	@ManyToOne(optional = false)
	private Airport				arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;


	@Transient
	public int durationInHours() {
		Instant departureInstant = this.departureDate.toInstant();
		Instant arrivalInstant = this.arrivalDate.toInstant();

		Duration duration = Duration.between(departureInstant, arrivalInstant);

		return duration.toHoursPart();
	}

	@Transient
	public String flightNumber() {
		return this.airline.getIataCode() + this.fightNumberDigits;
	}
}
