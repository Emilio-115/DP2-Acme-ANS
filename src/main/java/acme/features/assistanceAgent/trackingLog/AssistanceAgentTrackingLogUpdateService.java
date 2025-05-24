
package acme.features.assistanceAgent.trackingLog;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;

		int claimId = super.getRequest().getData("claimId", int.class);

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);
		Optional<TrackingLog> trackingLog = this.getTrackingLog();

		boolean trackingLogStatus = trackingLog.isPresent() && trackingLog.get().isDraftMode() && this.securityId();
		status = claim.isPresent();

		super.getResponse().setAuthorised(status && trackingLogStatus);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		trackingLog = this.getTrackingLog().get();
		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "undergoingStep", "resolutionPercentage", "resolution", "status");
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());

	}

	@Override
	public void validate(final TrackingLog trackingLog) {

	}

	@Override
	public void perform(final TrackingLog trackingLog) {

		this.repository.save(trackingLog);
		this.repository.save(trackingLog.getClaim());

	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		int claimId = trackingLog.getClaim().getId();
		super.getResponse().addGlobal("claimId", claimId);
		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "undergoingStep", "resolutionPercentage", "resolution", "draftMode", "status", "reclaim");
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

	private Optional<TrackingLog> getTrackingLog() {
		String method = super.getRequest().getMethod();
		int trackingLogId;
		if (method.equals("GET"))
			trackingLogId = super.getRequest().getData("trackingLogId", int.class);
		else
			trackingLogId = super.getRequest().getData("id", int.class);
		return this.repository.findTrackingLogById(trackingLogId);
	}

	private boolean securityId() {
		String method = super.getRequest().getMethod();
		boolean status = true;
		if (method.equals("POST")) {
			int claimId = super.getRequest().getData("id", int.class);
			int securityId = super.getRequest().getData("trackingLogId", int.class);

			status = claimId == securityId;
		}
		return status;
	}
}
