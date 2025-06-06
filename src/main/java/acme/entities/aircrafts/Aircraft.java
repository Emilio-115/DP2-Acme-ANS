
package acme.entities.aircrafts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidShortText;
import acme.constraints.aircraft.ValidAircraft;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidAircraft
@Entity
@Table(indexes = {
	@Index(columnList = "id, registrationNumber"), @Index(columnList = "status"),
})
public class Aircraft extends AbstractEntity {

	/**
	 * An aircraft is a vehicle designed for air travel that belongs to an airline and is used to transport passengers
	 * between cities or countries. The system must store the following data about them:
	 *
	 * Model (up to 50 characters)
	 * Registration number (unique, up to 50 characters)
	 * Its capacity as a number of passengers
	 * Cargo weight (between 2K and 50K kgs)
	 * Status, which reports on whether the aircraft is in active service or under maintenance
	 * Optional details (up to 255 characters).
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				model;

	@Mandatory
	@ValidShortText
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private int					capacity;

	@Mandatory
	@ValidNumber(min = 2_000, max = 50_000)
	@Automapped
	private int					cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidString
	@Automapped
	private String				details;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
