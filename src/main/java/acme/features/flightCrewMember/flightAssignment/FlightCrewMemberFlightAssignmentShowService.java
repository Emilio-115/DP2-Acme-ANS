
package acme.features.flightCrewMember.flightAssignment;

import java.util.Optional;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends FlightCrewMemberFlightAssignmentService {

	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		Optional<FlightAssignment> flightAssignment;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);
		status = flightAssignment.isPresent();

		super.getResponse().setAuthorised(status);
	}

}
