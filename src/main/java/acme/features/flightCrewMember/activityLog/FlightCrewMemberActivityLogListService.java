
package acme.features.flightCrewMember.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected FlightCrewMemberActivityLogRepository repository;


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
