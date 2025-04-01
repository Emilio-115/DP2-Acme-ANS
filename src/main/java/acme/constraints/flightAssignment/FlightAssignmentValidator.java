
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

		FlightAssignmentRepository repository = SpringHelper.getBean(FlightAssignmentRepository.class);

		boolean result = true;

		/*
		 * Si está publicado:
		 * * Leg no es draftMode
		 * Si está publicado y confirmado:
		 * * No hay otra asignación confirmada del mismo FCM que haga overlap
		 * * No hay otro piloto confirmado
		 * * No hay otra co-piloto confirmado
		 */
		if (flightAssignment == null) {
			super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var flightCrewMember = flightAssignment.getFlightCrewMember();
		if (flightCrewMember == null) {
			super.state(context, false, "flightCrewMember", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var leg = flightAssignment.getLeg();
		if (leg == null) {
			super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var status = flightAssignment.getStatus();
		if (status == null) {
			super.state(context, false, "status", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var duty = flightAssignment.getDuty();
		if (duty == null) {
			super.state(context, false, "duty", "javax.validation.constraints.NotNull.message");
			return false;
		}

		if (leg.getFlight() != null) {

			boolean legPublished = !leg.getFlight().isDraftMode();

			super.state(context, legPublished, "leg", "acme.validation.flight-assignment.leg-not-published.message");
		}

		boolean flightPublishedAndNotCancelled = !flightAssignment.isDraftMode() && (status.equals(FlightAssignmentStatus.CONFIRMED) || status.equals(FlightAssignmentStatus.PENDING));

		if (flightPublishedAndNotCancelled) {
			boolean legMemberDutyUnique = repository.isLegMemberDutyUnique(flightAssignment.getId(), leg.getId(), flightCrewMember.getId(), flightAssignment.getDuty());

			super.state(context, legMemberDutyUnique, "leg", "acme.validation.flight-assignment.member-already-in-leg-with-duty.message");
		}

		boolean flightConfirmed = !flightAssignment.isDraftMode() && status.equals(FlightAssignmentStatus.CONFIRMED);

		if (flightConfirmed) {

			boolean flightCrewMemberFree = repository.isFlightCrewMemberFree(flightAssignment.getId(), flightCrewMember.getId(), leg.getDepartureDate(), leg.getArrivalDate());

			super.state(context, flightCrewMemberFree, "leg", "acme.validation.flight-assignment.flight-crew-member-busy.message");

			if (duty.equals(FlightCrewDuty.PILOT)) {
				boolean pilotDutyFree = repository.isDutyFree(flightAssignment.getId(), leg.getId(), FlightCrewDuty.PILOT);

				super.state(context, pilotDutyFree, "duty", "acme.validation.flight-assignment.pilot-duty-taken.message");
			}

			else if (duty.equals(FlightCrewDuty.COPILOT)) {
				boolean copilotDutyFree = repository.isDutyFree(flightAssignment.getId(), leg.getId(), FlightCrewDuty.COPILOT);

				super.state(context, copilotDutyFree, "duty", "acme.validation.flight-assignment.copilot-duty-taken.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
