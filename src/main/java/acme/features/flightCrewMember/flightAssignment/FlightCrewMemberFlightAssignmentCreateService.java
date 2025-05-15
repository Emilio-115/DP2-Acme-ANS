
package acme.features.flightCrewMember.flightAssignment;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentCreateService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	protected boolean checkLeg() {
		return true;
	}

	@Override
	protected boolean idIsAuthorised() {
		Integer id = super.getRequest().getData("id", Integer.class, null);
		return id == null || id == 0;
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
