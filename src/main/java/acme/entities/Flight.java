
package acme.entities;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
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
	@Valid
	@Automapped
	private Boolean				requiresSelfTransfer;

	@Mandatory
	@ValidNumber(fraction = 2)
	@Automapped
	private float				cost;

	@Optional
	@ValidString
	@Automapped
	private String				description;

}
