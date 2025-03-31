
package acme.constraints.flightAssignment;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentRepository;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.entities.flightAssignment.FlightCrewDuty;

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
		 * Si está publicado:
		 * * Leg no es draftMode
		 * Si está publicado y confirmado:
		 * * No hay otra asignación confirmada del mismo FCM que haga overlap
		 * * No hay otro piloto confirmado
		 * * No hay otra co-piloto confirmado
		 */
		if (flightAssignment == null)
			return false;

		var flightCrewMember = flightAssignment.getFlightCrewMember();
		var leg = flightAssignment.getLeg();

		if (leg != null) {
			boolean legPublished = !leg.isDraftMode();

			super.state(context, legPublished, "leg", "acme.validation.flight-assignment.leg-not-published.message");
		}

		boolean flightConfirmed = !flightAssignment.isDraftMode() && FlightAssignmentStatus.CONFIRMED.equals(flightAssignment.getStatus());

		if (flightConfirmed && leg != null && flightCrewMember != null) {
			FlightAssignmentRepository repository = SpringHelper.getBean(FlightAssignmentRepository.class);

			boolean flightCrewMemberFree = repository.isFlightCrewMemberFree(flightAssignment.getId(), flightCrewMember.getId(), leg.getDepartureDate(), leg.getArrivalDate());

			super.state(context, flightCrewMemberFree, "leg", "acme.validation.flight-assignment.flight-crew-member-busy.message");

			if (FlightCrewDuty.PILOT.equals(flightAssignment.getDuty())) {
				boolean pilotDutyFree = repository.isDutyFree(flightAssignment.getId(), leg.getId(), FlightCrewDuty.PILOT);

				super.state(context, pilotDutyFree, "duty", "acme.validation.flight-assignment.pilot-duty-taken.message");
			}

			else if (FlightCrewDuty.COPILOT.equals(flightAssignment.getDuty())) {
				boolean copilotDutyFree = repository.isDutyFree(flightAssignment.getId(), leg.getId(), FlightCrewDuty.COPILOT);

				super.state(context, copilotDutyFree, "duty", "acme.validation.flight-assignment.copilot-duty-taken.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
