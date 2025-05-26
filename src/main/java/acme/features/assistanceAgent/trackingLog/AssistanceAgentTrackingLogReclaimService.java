
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
public class AssistanceAgentTrackingLogReclaimService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;

		String method = super.getRequest().getMethod();
		boolean statusTrackingLog = true;
		int trackingLogId;

		status = this.canReclaim();
		if (method.equals("POST")) {
			trackingLogId = super.getRequest().getData("id", int.class);
			statusTrackingLog = trackingLogId == 0;
		}
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

		//Request hola = super.getRequest();

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);

		super.bindObject(trackingLog, "undergoingStep", "resolutionPercentage", "resolution", "status");
		trackingLog.setReclaim(true);
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

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "undergoingStep", "resolutionPercentage", "resolution", "draftMode", "status");
		dataset.put("statuses", choices);
		dataset.put("claim", claim);
		dataset.put("claimId", claimId);

		super.getResponse().addData(dataset);

	}

	private boolean canReclaim() {
		int claimId = super.getRequest().getData("claimId", int.class);
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);
		boolean status = false;

		if (claim.isPresent()) {
			List<TrackingLog> reclaimedTrackingLogs = this.repository.findTopPercentage(claimId, true);
			if (reclaimedTrackingLogs.isEmpty()) {
				List<TrackingLog> trackingLogs = this.repository.findTopPercentage(claimId, false);
				status = !trackingLogs.isEmpty() && !trackingLogs.get(0).isDraftMode() && trackingLogs.get(0).getResolutionPercentage().equals(100.00);
			} else
				status = !reclaimedTrackingLogs.get(0).isDraftMode() && !reclaimedTrackingLogs.get(0).getResolutionPercentage().equals(100.00);
		}
		return status;

	}
}
