
package acme.features.assistanceAgent.trackingLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		int trackingLogId = super.getRequest().getData("id", int.class);
		boolean status;
		TrackingLog trackingLog = this.repository.findTrackingLogById(trackingLogId);
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && trackingLog != null && trackingLog.getClaim().getAssistanceAgent().getId() == agentId;
		;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int trackingLogId;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {

		Claim claim = trackingLog.getClaim();

		super.bindObject(trackingLog, "undergoingStep", "resolutionPercentage", "resolution", "status");
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		Double per = trackingLog.getResolutionPercentage();
		TrackingLogStatus st = trackingLog.getStatus();

		if (per != null && st != null)
			if (per == 100.00) {
				claim.setCompleted(true);
				ClaimStatus cs = st.equals(TrackingLogStatus.ACCEPTED) ? ClaimStatus.ACCEPTED : ClaimStatus.REJECTED;
				claim.setIsAccepted(cs);
			}
		trackingLog.setClaim(claim);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean notPublished = trackingLog.isDraftMode();

		List<TrackingLog> trackingLogs;
		trackingLogs = this.repository.findTopPercentage(trackingLog.getClaim().getId(), trackingLog.isReclaim());

		super.state(notPublished, "*", "acme.validation.update.draftMode.tracking-log");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
		this.repository.save(trackingLog);
		this.repository.save(trackingLog.getClaim());

	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "undergoingStep", "resolutionPercentage", "resolution", "status", "draftMode", "reclaim");
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);

	}
}
