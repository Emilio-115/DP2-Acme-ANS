
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		int assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		super.getResponse().setAuthorised(this.validPublish(assistanceAgentId) && this.securityId());
	}

	@Override
	public void load() {

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Claim claim = this.getClaim(agentId).get();
		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
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

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "isAccepted", "draftMode");
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("status", status);
		dataset.put("landedLegs", legChoices);

		super.getResponse().addData(dataset);
	}

	private Optional<Claim> getClaim(final int agentId) {
		String method = super.getRequest().getMethod();

		int claimId;
		if (method.equals("GET"))
			claimId = super.getRequest().getData("claimId", int.class);
		else
			claimId = super.getRequest().getData("id", int.class);
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		return claim;
	}

	private boolean securityId() {
		String method = super.getRequest().getMethod();
		boolean status = true;
		if (method.equals("POST")) {
			int claimId = super.getRequest().getData("id", int.class);
			int securityId = super.getRequest().getData("claimId", int.class);

			status = claimId == securityId;
		}
		return status;
	}

	private boolean validPublish(final int agentId) {
		int legId;
		Leg leg = null;
		boolean status;
		boolean draftStatus = false;

		Optional<Claim> claim = this.getClaim(agentId);

		if (claim.isPresent()) {
			draftStatus = claim.get().isDraftMode();
			if (super.getRequest().getMethod().equals("POST")) {
				legId = super.getRequest().getData("leg", int.class);
				leg = this.repository.findLegById(legId);
			} else
				leg = claim.get().getLeg();
		}
		status = this.validLeg(leg) && draftStatus;
		return status;
	}

	private boolean validLeg(final Leg leg) {
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);
		boolean status = leg != null && legs.contains(leg);

		return status;
	}
}
