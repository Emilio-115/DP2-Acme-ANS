
package acme.realms.flightCrewMember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidInitialsNumberIdentifier;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.flightCrewMember.ValidFlightCrewMember;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightCrewMember
@Table(indexes = {
	@Index(columnList = "availabilityStatus")
})
public class FlightCrewMember extends AbstractRole {

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
	@ValidNumber(min = 0, max = 75)
	@Automapped
	private Integer								yearsOfExperience;

}
