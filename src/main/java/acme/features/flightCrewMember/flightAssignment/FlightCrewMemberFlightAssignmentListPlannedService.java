
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListPlannedService extends FlightCrewMemberFlightAssignmentListService {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		List<FlightAssignment> flightAssignments;

		flightAssignments = this.repository.findPlannedFlightAssignments(flightCrewMember.getId(), MomentHelper.getCurrentMoment());

		super.getBuffer().addData(flightAssignments);
	}

}
