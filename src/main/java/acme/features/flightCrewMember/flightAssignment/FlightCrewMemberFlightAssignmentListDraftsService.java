
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentListDraftsService extends FlightCrewMemberFlightAssignmentListService {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void load() {
		List<FlightAssignment> flightAssignments;

		flightAssignments = this.repository.findDraftFlightAssignments();

		super.getBuffer().addData(flightAssignments);
	}

}
