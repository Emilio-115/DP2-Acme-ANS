
package acme.entities.recomendations;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recomendation extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString
	@Automapped
	private String				name;

	@Mandatory
	@ValidString
	@Automapped
	private String				timezone;

	@Mandatory
	@ValidString
	@Automapped
	private String				closedBucket;

	@Mandatory
	@ValidString
	@Automapped
	private String				categories;

	@Mandatory
	@ValidString
	@Automapped
	private String				country;

	@Mandatory
	@ValidString
	@Automapped
	private String				locality;

	@Mandatory
	@ValidString
	@Automapped
	private String				address;

}
