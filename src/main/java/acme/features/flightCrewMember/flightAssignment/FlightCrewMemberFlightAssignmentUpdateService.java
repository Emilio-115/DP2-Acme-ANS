
package acme.features.flightCrewMember.flightAssignment;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentUpdateService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	protected boolean checkLeg() {
		return true;
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
