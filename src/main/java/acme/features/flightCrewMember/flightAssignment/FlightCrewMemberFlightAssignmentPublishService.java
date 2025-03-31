
package acme.features.flightCrewMember.flightAssignment;

import acme.client.helpers.MomentHelper;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;
import acme.realms.flightCrewMember.FlightCrewMemberAvailabilityStatus;

@GuiService
public class FlightCrewMemberFlightAssignmentPublishService extends FlightCrewMemberFlightAssignmentEditService {

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);

		super.bind(flightAssignment);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		{
			FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
			boolean flightCrewMemberAvailable = flightCrewMember.getAvailabilityStatus().equals(FlightCrewMemberAvailabilityStatus.AVAILABLE);

			super.state(flightCrewMemberAvailable, "leg", "acme.validation.flight-assignment.flight-crew-member-unavailable.message");
		}
		{
			var leg = flightAssignment.getLeg();
			if (leg == null)
				return;

			var departureDate = leg.getDepartureDate();
			if (departureDate == null)
				return;

			boolean legPending = departureDate.after(MomentHelper.getCurrentMoment());
			super.state(legPending, "leg", "acme.validation.flight-assignment.leg-departed.message");
		}
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(true);

		super.unbind(flightAssignment);
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}
}
