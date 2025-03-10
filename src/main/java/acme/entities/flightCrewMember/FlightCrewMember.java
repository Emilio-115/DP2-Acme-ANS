
package acme.entities.flightCrewMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidInitialsNumberIdentifier;
import acme.constraints.ValidPhoneNumber;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractEntity {

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
	private static final long					serialVersionUID	= 1L;

	@Mandatory
	@ValidInitialsNumberIdentifier
	@Column(unique = true)
	private String								employeeCode;

	@Mandatory
	@ValidPhoneNumber
	@Automapped
	private String								phoneNumber;

	@Mandatory
	@ValidString
	@Automapped
	private String								languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private FlightCrewMemberAvailabilityStatus	availabilityStatus;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline								employerAirline;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money								salary;

	@Optional
	@Valid
	@Automapped
	private Integer								yearsOfExperience;

}
