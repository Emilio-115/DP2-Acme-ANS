
package acme.features.assistanceAgent.trackingLog;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int trackingLogId;
		boolean status;

		trackingLogId = super.getRequest().getData("id", int.class);

		status = this.validDelete(trackingLogId);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int trackingLogId;
		trackingLogId = super.getRequest().getData("id", int.class);

		TrackingLog trackingLog = this.repository.findTrackingLogById(trackingLogId).get();

		super.getBuffer().addGlobal("security", trackingLog.getId());
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		int claimId = trackingLog.getClaim().getId();
		super.getResponse().addGlobal("claimId", claimId);
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "undergoingStep", "resolutionPercentage", "resolution", "status", "draftMode", "reclaim");
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);

	}

	private boolean validDelete(final int trackingLogId) {
		boolean status = false;
		Optional<TrackingLog> trackingLog = this.repository.findTrackingLogById(trackingLogId);
		if (trackingLog.isPresent()) {
			Claim claim = trackingLog.get().getClaim();
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = claim.getAssistanceAgent().getId() == agentId;
		}
		return status;
	}
}
