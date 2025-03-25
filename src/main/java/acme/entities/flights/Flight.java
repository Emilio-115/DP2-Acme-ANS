
package acme.entities.flights;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
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
import acme.entities.legs.LegRepository;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
	@ValidString(max = 50)
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
		return legRepository.findFirstLegByFlight(this).getDepartureDate();
	}

	@Transient
	public Date scheduledArrival() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findLastLegByFlight(this).getArrivalDate();
	}

	@Transient
	public String origin() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findFirstLegByFlight(this).getDepartureAirport().getCity();
	}

	@Transient
	public String destiny() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return legRepository.findLastLegByFlight(this).getArrivalAirport().getCity();
	}

	@Transient
	public int numberOfLayovers() {
		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);
		return (int) legRepository.countLegsByFlight(this);
	}


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager manager;

}
