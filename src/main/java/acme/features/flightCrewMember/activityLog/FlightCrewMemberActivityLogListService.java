
package acme.features.flightCrewMember.activityLog;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {

		Dataset dataset;
		dataset = super.unbindObject(activityLog, "incidentType", "severityLevel");
		dataset.put("flightNumber", activityLog.getRegisteringAssignment().getLeg().flightNumber());
		super.getResponse().addData(dataset);

	}

}
