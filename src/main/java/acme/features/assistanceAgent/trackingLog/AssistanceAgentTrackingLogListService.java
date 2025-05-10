
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

		int claimId = super.getRequest().getData("claimId", int.class);
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Claim claim = this.repository.findClaimById(claimId);
		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && claim != null && claim.getAssistanceAgent().getId() == agentId;

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

		boolean reclaimed = trackingLogs.stream().anyMatch(x -> x.isReclaim());

		List<TrackingLog> TLs = this.repository.findTopPercentage(claimId, reclaimed);
		double topPercentage = 0.0;
		boolean published = true;
		boolean end = false;
		if (!TLs.isEmpty()) {
			topPercentage = TLs.get(0).getResolutionPercentage();
			published = !TLs.get(0).isDraftMode();
		}
		boolean finish = false;
		if (!reclaimed)
			finish = topPercentage == 100.00;
		else {
			finish = true;
			if (topPercentage == 100.00)
				end = true;
		}
		super.getResponse().addGlobal("finish", finish);
		super.getResponse().addGlobal("published", published);
		super.getResponse().addGlobal("claimId", claimId);
		super.getResponse().addGlobal("end", end);

		Claim claim = this.repository.findClaimById(claimId);
		super.getResponse().addGlobal("claimDraftMode", claim.isDraftMode());
	}
}
