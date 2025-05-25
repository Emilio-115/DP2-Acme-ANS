
package acme.features.flightCrewMember.activityLog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListPublishedService extends FlightCrewMemberActivityLogListService {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Integer registeringAssignmentId = super.getRequest().getData("flightAssignmentId", Integer.class, null);

		if (registeringAssignmentId != null) {
			Optional<FlightAssignment> assignment = this.repository.findPublishedAndConfirmedFlightAssignmentByIdAndFlightCrewMemberId(registeringAssignmentId, flightCrewMember.getId());
			if (assignment.isEmpty()) {
				super.getResponse().setAuthorised(false);
				return;
			}
		}

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Integer flightAssignmentId = super.getRequest().getData("flightAssignmentId", Integer.class, null);

		List<ActivityLog> activityLogs;

		if (flightAssignmentId == null)
			activityLogs = this.repository.findPublishedByFlightCrewMemberId(flightCrewMember.getId());
		else
			activityLogs = this.repository.findPublishedByFlightCrewMemberIdAndFlightAssignmentId(flightCrewMember.getId(), flightAssignmentId);

		super.getBuffer().addData(activityLogs);
	}

}
