
package acme.features.assistanceAgent.trackingLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		int claimId;

		List<TrackingLog> trackingLogs;
		claimId = super.getRequest().getData("claimId", int.class);

		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claimId);

		//System.out.println("ID " + super.getRequest().getData("claim", Claim.class));

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "resolutionPercentage", "status", "draftMode");

		super.addPayload(dataset, trackingLog, "claim.id");
		super.getResponse().addData(dataset);
	}
}
