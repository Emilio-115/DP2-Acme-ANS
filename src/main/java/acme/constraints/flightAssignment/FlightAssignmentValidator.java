
package acme.constraints.flightAssignment;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentRepository;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.entities.flightAssignment.FlightCrewDuty;
import acme.realms.flightCrewMember.FlightCrewMemberAvailabilityStatus;

@Validator
public class FlightAssignmentValidator extends AbstractValidator<ValidFlightAssignment, FlightAssignment> {

	@Override
	protected void initialise(final ValidFlightAssignment annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightAssignment flightAssignment, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		/*
		 * FlightCrewMember está disponible
		 * Si está publicado:
		 * * Leg no es draftMode
		 * Si está publicado y confirmado:
		 * * No hay otra asignación confirmada del mismo FCM que haga overlap
		 * * No hay otro piloto confirmado
		 * * No hay otra co-piloto confirmado
		 */

		if (flightAssignment == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var flightCrewMember = flightAssignment.getFlightCrewMember();
		var leg = flightAssignment.getLeg();

		if (flightCrewMember == null)
			super.state(context, false, "flightCrewMember", "javax.validation.constraints.NotNull.message");
		if (leg == null)
			super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");

		if (flightCrewMember != null) {
			var status = flightCrewMember.getAvailabilityStatus();
			boolean flightCrewMemberUnavailable = FlightCrewMemberAvailabilityStatus.AVAILABLE.equals(status);

			super.state(context, flightCrewMemberUnavailable, "flightCrewMember", "acme.validation.flight-assignment.flight-crew-member-unavailable.message");
		}

		if (!flightAssignment.isDraftMode() && leg != null) {
			boolean legNotPublished = leg.isDraftMode();

			super.state(context, legNotPublished, "leg", "acme.validation.flight-assignment.leg-not-published.message");
		}

		boolean flightConfirmed = !flightAssignment.isDraftMode() && FlightAssignmentStatus.CONFIRMED.equals(flightAssignment.getStatus());

		if (flightConfirmed && leg != null && flightCrewMember != null) {
			FlightAssignmentRepository repository = SpringHelper.getBean(FlightAssignmentRepository.class);

			boolean flightCrewMemberBusy = repository.isFlightCrewMemberBusy(flightAssignment, flightCrewMember, leg.getDepartureDate(), leg.getArrivalDate());

			super.state(context, flightCrewMemberBusy, "leg", "acme.validation.flight-assignment.flight-crew-member-busy.message");

			if (FlightCrewDuty.PILOT.equals(flightAssignment.getDuty())) {
				boolean pilotDutyTaken = repository.pilotDutyTaken(flightAssignment, leg);

				super.state(context, pilotDutyTaken, "duty", "acme.validation.flight-assignment.pilot-duty-taken.message");
			}

			else if (FlightCrewDuty.COPILOT.equals(flightAssignment.getDuty())) {
				boolean copilotDutyTaken = repository.copilotDutyTaken(flightAssignment, leg);

				super.state(context, copilotDutyTaken, "duty", "acme.validation.flight-assignment.copilot-duty-taken.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
