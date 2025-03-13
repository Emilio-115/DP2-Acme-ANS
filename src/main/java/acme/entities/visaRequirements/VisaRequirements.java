
package acme.entities.visaRequirements;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class VisaRequirements extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				origin;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				destination;

	@Mandatory
	@Valid
	@Automapped
	private VisaRequirementType	type;

}
