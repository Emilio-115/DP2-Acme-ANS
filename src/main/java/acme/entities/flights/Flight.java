
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidShortText;
import acme.constraints.flight.ValidFlight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;
import acme.realms.airlineManager.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlight
@Table(indexes = {
	@Index(columnList = "draftMode"),
})
public class Flight extends AbstractEntity {

	/*
	 * A flight is a scheduled journey made by airlines to transport passengers between two locations.
	 * The system must store the following data about them:
	 * - Tag that highlights some feature of the flight such as "the fastest", "the cheapest" (up to 50
	 * characters)
	 * - An indication on whether it requires self-transfer or not
	 * - A cost
	 * - An optional description (up to 255 characters).
	 * --- Derivadas ---
	 * - A scheduled departure
	 * - A scheduled arrival
	 * - the origin and destination cities
	 * - the number of layovers.
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				requiresSelfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@Automapped
	private boolean				draftMode			= true;


	@Transient
	public Date scheduledDeparture() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		Leg leg = legRepository.findFirstLegByFlightIdOrderByDepartureDate(this.getId());
		return leg != null ? leg.getDepartureDate() : null;
	}

	@Transient
	public Date scheduledArrival() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		Leg leg = legRepository.findFirstLegByFlightIdOrderByDepartureDateDesc(this.getId());
		return leg != null ? leg.getArrivalDate() : null;
	}

	@Transient
	public String origin() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		Leg leg = legRepository.findFirstLegByFlightIdOrderByDepartureDate(this.getId());
		return leg != null ? leg.getDepartureAirport().getCity() : "No Data";
	}

	@Transient
	public String destination() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		Leg leg = legRepository.findFirstLegByFlightIdOrderByDepartureDateDesc(this.getId());
		return leg != null ? leg.getArrivalAirport().getCity() : "No Data";
	}

	@Transient
	public int numberOfLayovers() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return (int) Long.max(legRepository.countLegsByFlight(this.getId()) - 1, 0);
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager manager;

}
