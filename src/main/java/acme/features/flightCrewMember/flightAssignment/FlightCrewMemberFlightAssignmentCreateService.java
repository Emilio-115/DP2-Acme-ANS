
package acme.features.flightCrewMember.flightAssignment;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends FlightCrewMemberFlightAssignmentService {

	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

}
