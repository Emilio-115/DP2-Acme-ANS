
package acme.features.flightCrewMember.activityLog;

import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends FlightCrewMemberActivityLogEditService {

	@Override
	public void authorise() {

		boolean status = true;

		if (!this.isRegisteringAssignmentAllowedIfPresent())
			status = false;

		if (status && super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			if (id != 0)
				status = false;
		}

		super.getResponse().setAuthorised(status);

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
