
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;
		status = this.validUpdate();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim = this.getClaim().get();

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

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

	private Optional<Claim> getClaim() {
		String method = super.getRequest().getMethod();
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int claimId;
		if (method.equals("GET"))
			claimId = super.getRequest().getData("claimId", int.class);
		else
			claimId = super.getRequest().getData("id", int.class);
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		return claim;
	}

	private Leg getLeg(final Claim claim) {
		int legId;
		Leg leg;

		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			legId = claim.getLeg().getId();
		else
			legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		return leg;
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

	private boolean validUpdate() {
		Optional<Claim> claim = this.getClaim();
		boolean status = false;
		if (claim.isPresent()) {
			Leg leg = this.getLeg(claim.get());
			status = this.securityId() && this.validLeg(leg) && claim.get().isDraftMode();
		}
		return status;
	}

	private boolean validLeg(final Leg leg) {
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);
		boolean status = leg != null && legs.contains(leg);
		return status;
	}
}
