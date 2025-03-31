
package acme.features.flightCrewMember.activityLog;

import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;

@GuiService
public class FlightCrewMemberActivityLogUpdateService extends FlightCrewMemberActivityLogEditService {

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}
}
