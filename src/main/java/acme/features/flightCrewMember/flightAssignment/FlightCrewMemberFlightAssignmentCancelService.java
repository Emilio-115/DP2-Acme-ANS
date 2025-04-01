
package acme.features.flightCrewMember.flightAssignment;

import java.util.Optional;

import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentCancelService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		Optional<FlightAssignment> flightAssignment;

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findByIdAndFlightCrewMemberId(flightAssignmentId, flightCrewMember.getId());
		status = flightAssignment.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		flightAssignment.setStatus(FlightAssignmentStatus.CANCELLED);
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
