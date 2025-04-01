
package acme.entities.claims;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.claim.ValidClaim;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidClaim
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Optional
	@Valid
	@Automapped
	private Boolean				isAccepted;

	@Mandatory
	@Automapped
	boolean						draftMode;

	@Mandatory
	@Valid
	@ManyToOne
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne
	private Leg					leg;


	@Transient
	public boolean isComplete() {
		boolean result;
		ClaimRepository repository;

		repository = SpringHelper.getBean(ClaimRepository.class);
		List<TrackingLog> trackingLogs = repository.getAllTrackingLogFromClaim(this.getId());
		if (!trackingLogs.isEmpty())
			result = trackingLogs.get(0).getResolutionPercentage() < 100.00;
		else
			return false;
		return !result;
	}

}
