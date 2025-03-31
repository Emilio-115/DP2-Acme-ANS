
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		int agentId;
		int claimId;
		boolean status;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claimId = super.getRequest().getData("id", int.class);

		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		status = claim.isPresent();

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
		Collection<Leg> legs = this.repository.findAllLegs();

		choices = SelectChoices.from(ClaimType.class, claim.getType());
		legChoices = SelectChoices.from(legs, "id", claim.getLeg());

		System.out.println("CLAIM " + claim.getRegistrationMoment());
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "isAccepted");
		dataset.put("registrationMoment", claim.getRegistrationMoment());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("landedLegs", legChoices);
		dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}
}
