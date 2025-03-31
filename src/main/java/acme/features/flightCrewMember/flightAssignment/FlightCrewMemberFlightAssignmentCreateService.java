
package acme.features.flightCrewMember.flightAssignment;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;
import acme.realms.flightCrewMember.FlightCrewMemberAvailabilityStatus;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	public void authorise() {

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		super.getResponse().setAuthorised(flightCrewMember.getAvailabilityStatus().equals(FlightCrewMemberAvailabilityStatus.AVAILABLE));

	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		FlightAssignment flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);
		flightAssignment.setFlightCrewMember(flightCrewMember);

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
