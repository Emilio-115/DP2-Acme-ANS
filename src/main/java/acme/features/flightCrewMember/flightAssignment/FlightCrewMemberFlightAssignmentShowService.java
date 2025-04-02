
package acme.features.flightCrewMember.flightAssignment;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	public boolean authoriseFlightAssignment(final FlightAssignment flightAssignment) {
		return true;
	}
}
