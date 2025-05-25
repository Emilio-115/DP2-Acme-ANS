
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		else {
			status = false;
			int legId = super.getRequest().getData("leg", int.class);
			Leg leg = this.repository.findLegById(legId);
			boolean legStatus = this.validLeg(leg);
			int claimId = super.getRequest().getData("id", int.class);
			if (claimId == 0 && legStatus)
				status = true;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent assistanceAgent;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();

		claim.setAssistanceAgent(assistanceAgent);
		claim.setDraftMode(true);
		claim.setIsAccepted(ClaimStatus.PENDING);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
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

		dataset = super.unbindObject(claim, "passengerEmail", "description", "type", "isAccepted", "draftMode");
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("status", status);
		dataset.put("landedLegs", legChoices);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

	private boolean validLeg(final Leg leg) {
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);
		boolean status = leg == null || legs.contains(leg);

		return status;
	}

}
