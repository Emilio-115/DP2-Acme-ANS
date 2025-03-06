
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

	@Automapped
	@Mandatory
	@ValidString(max = 50)
	private String				model;

	@Column(unique = true)
	@Mandatory
	@ValidString(max = 50)
	private String				registrationNumber;

	@Automapped
	@Mandatory
	@ValidNumber(min = 0)
	private int					capacity;

	@Automapped
	@Mandatory
	@ValidNumber(min = 2_000, max = 50_000)
	private int					cargoWeight;

	@Automapped
	@Mandatory
	private AircraftStatus		status;

	@Automapped
	@ValidString
	private String				details;

}
