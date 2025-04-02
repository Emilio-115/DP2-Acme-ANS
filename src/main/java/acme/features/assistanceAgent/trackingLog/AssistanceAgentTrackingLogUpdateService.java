
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;

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
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

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

		int claimId;
		Claim claim;

		claimId = super.getRequest().getData("claim", int.class);
		claim = this.repository.findClaimById(claimId);

		super.bindObject(trackingLog, "undergoingStep", "resolutionPercentage", "resolution", "status");
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		trackingLog.setClaim(claim);
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		assert trackingLog != null;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		assert trackingLog != null;

		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {

		int assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<Claim> claims = this.repository.findAllClaimsByAssistanceAgentId(assistanceAgentId);
		SelectChoices choices;
		SelectChoices claimsChoices;
		Dataset dataset;

		claimsChoices = SelectChoices.from(claims, "id", trackingLog.getClaim());
		choices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "undergoingStep", "resolutionPercentage", "resolution", "draftMode", "status");
		dataset.put("claim", choices.getSelected().getKey());
		dataset.put("claimOptions", claimsChoices);
		dataset.put("statuses", choices);

		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
