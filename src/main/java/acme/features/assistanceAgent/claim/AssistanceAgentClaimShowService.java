
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		int agentId;
		int claimId;
		boolean status;

		int assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claimId = super.getRequest().getData("id", int.class);

		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		status = claim.isPresent() && claim.get().getAssistanceAgent().getId() == assistanceAgentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Claim claim;
		int claimId = super.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {

		SelectChoices choices;
		Dataset dataset;
		SelectChoices legChoices;
		SelectChoices status;
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);

		choices = SelectChoices.from(ClaimType.class, claim.getType());
		legChoices = SelectChoices.from(legs, "flightNumberDigits", claim.getLeg());
		status = SelectChoices.from(ClaimStatus.class, claim.getIsAccepted());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "isAccepted", "draftMode");
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("status", status);
		dataset.put("landedLegs", legChoices);
		dataset.put("readonly", false);
		super.addPayload(dataset, claim, "id");

		super.getResponse().addData(dataset);
	}
}
