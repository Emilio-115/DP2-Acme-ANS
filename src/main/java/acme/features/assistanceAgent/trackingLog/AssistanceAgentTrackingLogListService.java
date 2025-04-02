
package acme.features.assistanceAgent.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

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

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "resolutionPercentage", "status", "reclaim");
		dataset.put("claim", trackingLog.getClaim());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLogs) {
		Integer claimId = super.getRequest().getData("claimId", int.class);

		List<TrackingLog> TLs = this.repository.findTopPercentage(claimId);
		double topPercentage = 0.0;
		boolean published = false;
		if (!TLs.isEmpty() || TLs != null)
			topPercentage = TLs.get(0).getResolutionPercentage();
		published = !TLs.get(0).isDraftMode();

		boolean finish = topPercentage == 100.00;

		super.getResponse().addGlobal("finish", finish);
		super.getResponse().addGlobal("published", published);
		super.getResponse().addGlobal("claimId", claimId);

		Claim claim = this.repository.findClaimById(claimId);
		super.getResponse().addGlobal("claimDraftMode", claim.isDraftMode());
	}
}
