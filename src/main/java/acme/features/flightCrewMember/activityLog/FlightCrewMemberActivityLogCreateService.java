
package acme.features.flightCrewMember.activityLog;

import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends FlightCrewMemberActivityLogEditService {

	@Override
	public void authorise() {

		super.getResponse().setAuthorised(this.getRegisteringAssignmentFromRequest().isPresent());

	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();

		activityLog.setDraftMode(true);

		super.getBuffer().addData(activityLog);
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
