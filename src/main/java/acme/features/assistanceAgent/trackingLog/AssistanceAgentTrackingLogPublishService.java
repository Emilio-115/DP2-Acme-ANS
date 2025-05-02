
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
		status = trackingLog != null;
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
		assert trackingLog != null;
		int claimId = super.getRequest().getData("claimId", int.class);
		Claim claim = this.repository.findClaimById(claimId);

		super.bindObject(trackingLog, "undergoingStep", "resolutionPercentage", "resolution", "status", "lastUpdateMoment");
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		Double per = super.getRequest().getData("resolutionPercentage", double.class);
		TrackingLogStatus st = super.getRequest().getData("status", TrackingLogStatus.class);

		if (per == 100.00) {
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

		int thisTL = trackingLogs.indexOf(trackingLog);

		boolean allPublished = true;
		if (thisTL != trackingLogs.size() - 1)
			allPublished = !trackingLogs.get(thisTL + 1).isDraftMode();
		boolean result = notPublished && allPublished;

		super.state(result, "*", "acme.validation.update.draftMode.tracking-log");
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
