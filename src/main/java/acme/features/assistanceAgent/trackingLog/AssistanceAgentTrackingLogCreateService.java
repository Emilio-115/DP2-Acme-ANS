
package acme.features.assistanceAgent.trackingLog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		boolean canCreate = true;

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int claimId = super.getRequest().getData("claimId", int.class);
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		String method = super.getRequest().getMethod();
		boolean statusTrackingLog = true;
		int trackingLogId;

		List<TrackingLog> trackingLogs = this.repository.findTopPercentage(claimId, false);
		if (!trackingLogs.isEmpty() && (trackingLogs.get(0).isDraftMode() || trackingLogs.get(0).getResolutionPercentage().equals(100.00)))
			canCreate = false;

		if (method.equals("POST")) {
			trackingLogId = super.getRequest().getData("id", int.class);
			statusTrackingLog = trackingLogId == 0;
		}

		status = claim.isPresent() && canCreate;
		super.getResponse().setAuthorised(status && statusTrackingLog);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;

		trackingLog = new TrackingLog();
		trackingLog.setDraftMode(true);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		super.bindObject(trackingLog, "undergoingStep", "resolutionPercentage", "resolution", "status");

		trackingLog.setReclaim(false);
		trackingLog.setDraftMode(true);
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setClaim(claim);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		int claimId = super.getRequest().getData("claimId", int.class);
		Claim claim = this.repository.findClaimById(claimId);
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "undergoingStep", "resolutionPercentage", "resolution", "draftMode", "status", "reclaim");
		dataset.put("statuses", choices);
		dataset.put("claim", claim);
		dataset.put("claimId", claimId);

		super.getResponse().addData(dataset);

	}

}
