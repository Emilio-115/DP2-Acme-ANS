
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
