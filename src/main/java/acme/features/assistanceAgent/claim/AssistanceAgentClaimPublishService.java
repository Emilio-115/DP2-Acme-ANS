
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
import acme.entities.legs.LegStatus;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		int claimId = super.getRequest().getData("id", int.class);
		int assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status;
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, assistanceAgentId);
		status = claim.isPresent();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int claimId = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimById(claimId);
		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		boolean notPublished = claim.isDraftMode();
		super.state(notPublished, "draftMode", "acme.validation.update.draftMode");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
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
}
