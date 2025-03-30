
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.helpers.MomentHelper;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentListInProgressService extends FlightCrewMemberFlightAssignmentListService {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void load() {
		List<FlightAssignment> flightAssignments;

		flightAssignments = this.repository.findInProgressFlightAssignments(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(flightAssignments);
	}

}
