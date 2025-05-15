
package acme.features.flightCrewMember.flightAssignment;

import acme.client.helpers.MomentHelper;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentStatus;

@GuiService
public class FlightCrewMemberFlightAssignmentConfirmService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	public boolean flightAssignmentIsAuthorised(final FlightAssignment flightAssignment) {
		return FlightAssignmentStatus.PENDING.equals(flightAssignment.getStatus()) && !flightAssignment.isDraftMode();
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		flightAssignment.setStatus(FlightAssignmentStatus.CONFIRMED);

		flightAssignment.setUpdatedAt(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		flightAssignment.setStatus(FlightAssignmentStatus.PENDING);

		super.unbind(flightAssignment);
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}
}
