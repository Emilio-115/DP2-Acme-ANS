
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;
		int assistanceAgentId;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claim = this.repository.findByIdAndAssistanceAgentId(claimId, assistanceAgentId).get();

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
		boolean status;
		int claimId = super.getRequest().getData("id", int.class);

		List<TrackingLog> trackingLogs = this.repository.findAllTrackingLogsByClaimId(claimId);

		status = trackingLogs.size() == 0;

		super.state(status, "*", "assistance-agent.claim.delete.claim-linked");
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		SelectChoices choices;
		Dataset dataset;
		SelectChoices legChoices;
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);

		choices = SelectChoices.from(ClaimType.class, claim.getType());
		legChoices = SelectChoices.from(legs, "id", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg", "draftMode", "isAccepted");
		dataset.put("types", choices);
		dataset.put("landedLegs", legChoices);
		dataset.put("complete", claim.isComplete());

		super.getResponse().addData(dataset);
	}
}
