
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidInitialsNumberIdentifier;
import acme.entities.airlines.Airline;
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
	@ValidNumber(min = 0, max = 120)
	@Automapped
	private int					yearOfExpirience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.DATE)
	private Date				birthDate;

	@Optional
	@ValidUrl
	@Automapped
	private String				pictureLink;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;
}
