
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;
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
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Optional<Claim> claim = this.getClaim(agentId);

		status = claim.isPresent() && this.securityId() && claim.get().isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		claim = this.getClaim(agentId).get();

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
	}

	@Override
	public void perform(final Claim claim) {
		List<TrackingLog> trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());

		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);
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

}
