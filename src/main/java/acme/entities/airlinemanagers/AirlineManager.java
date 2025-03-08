
package acme.entities.airlinemanagers;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidInitialsNumberIdentifier;
import lombok.Getter;
import lombok.Setter;

/*
 * Airline managers are the people responsible for managing flights.
 * The system must handle the following information about managers:
 * 
 * Identifier number (unique, pattern "^[A-Z]{2-3}\d{6}$"
 * where the first two or three letters correspond to their ini-tials)
 * 
 * years of experience in the airline
 * 
 * date of birth
 * 
 * optional link to a picture that must be stored somewhere else
 */

@Entity
@Getter
@Setter
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidInitialsNumberIdentifier
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@Automapped
	private int					yearOfExpirience;

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				birthDate;

	@Automapped
	private String				pictureLink;
}
