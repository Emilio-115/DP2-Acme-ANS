
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
	 * The flight crew members are the people responsible for operating aircrafts
	 * and ensuring passenger safety and comfort during a flight.
	 * The system must store the following data about them:
	 * 
	 * an employee code (unique, pattern "^[A-Z]{2-3}\d{6}$", where the first two or three letters correspond to their initials)
	 * a phone number (pattern "^\+?\d{6,15}$")
	 * their language skills (up to 255 characters)
	 * their availability status ("AVAILABLE", "ON VACATION", "ON LEAVE")
	 * the airline they are working for
	 * and their salary.
	 * Optionally, the system may store his or her years of experience.
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
