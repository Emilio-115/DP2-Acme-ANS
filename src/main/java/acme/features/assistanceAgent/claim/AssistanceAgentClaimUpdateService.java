
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int assistanceAgentId;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);
		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claim = this.repository.findClaimByAssistanceAgent(assistanceAgentId, claimId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		assert claim != null;

		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		assert claim != null;
	}

	@Override
	public void perform(final Claim claim) {
		assert claim != null;

		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		assert claim != null;

		SelectChoices choices;
		Dataset dataset;
		SelectChoices legChoices;
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);

		choices = SelectChoices.from(ClaimType.class, claim.getType());
		legChoices = SelectChoices.from(legs, "id", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
		dataset.put("types", choices);
		dataset.put("landedLegs", legChoices);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
