
package acme.features.flightCrewMember.flightAssignment;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	protected boolean flightAssignmentIsAuthorised(final FlightAssignment flightAssignment) {
		return true;
	}

	@Override
	public void authorise() {
		boolean status = true;

		boolean idIsValid = this.idIsAuthorised();
		if (!idIsValid)
			status = false;

		super.getResponse().setAuthorised(status);
	}
}
