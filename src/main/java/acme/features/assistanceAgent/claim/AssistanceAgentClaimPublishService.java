
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

		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "isAccepted");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		boolean notPublished = claim.isDraftMode();

		List<TrackingLog> trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());

		boolean allPublished = trackingLogs.stream().allMatch(x -> !x.isDraftMode());

		boolean result = notPublished && allPublished;

		super.state(result, "*", "acme.validation.update.draftMode.claim");
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
		dataset.put("complete", claim.isComplete());

		super.getResponse().addData(dataset);
	}
}
