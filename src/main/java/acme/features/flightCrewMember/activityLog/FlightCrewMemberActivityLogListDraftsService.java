
package acme.features.flightCrewMember.activityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListDraftsService extends FlightCrewMemberActivityLogListService {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		List<ActivityLog> activityLogs = this.repository.findDraftsByFlightCrewMemberId(flightCrewMember.getId());

		super.getBuffer().addData(activityLogs);
	}

}
