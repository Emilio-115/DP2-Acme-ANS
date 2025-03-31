
package acme.features.flightCrewMember.activityLog;

import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends FlightCrewMemberActivityLogEditService {

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bind(activityLog);

		activityLog.setDraftMode(false);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}
}
