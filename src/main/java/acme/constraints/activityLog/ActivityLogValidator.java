
package acme.constraints.activityLog;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignment.FlightAssignmentStatus;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (activityLog == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var registeredAt = activityLog.getRegisteredAt();
		if (registeredAt == null) {
			super.state(context, false, "registeredAt", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var registeringAssignment = activityLog.getRegisteringAssignment();
		if (registeringAssignment == null) {
			super.state(context, false, "registeringAssignment", "javax.validation.constraints.NotNull.message");
			return false;
		}

		boolean assignmentIsPublished = !registeringAssignment.isDraftMode();
		super.state(context, assignmentIsPublished, "registeringAssignment", "acme.validation.activity-log.assignment-not-published.message");

		boolean assignmentIsConfirmed = FlightAssignmentStatus.CONFIRMED.equals(registeringAssignment.getStatus());
		super.state(context, assignmentIsConfirmed, "registeringAssignment", "acme.validation.activity-log.assignment-not-confirmed.message");

		var leg = registeringAssignment.getLeg();
		if (leg == null) {
			super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var arrivalDate = leg.getArrivalDate();
		if (arrivalDate == null) {
			super.state(context, false, "arrivalDate", "javax.validation.constraints.NotNull.message");
			return false;
		}

		boolean registeredAfterLanding = registeredAt.after(arrivalDate);

		super.state(context, registeredAfterLanding, "registeredAt", "acme.validation.activity-log.registered-before-landing.message");

		result = !super.hasErrors(context);

		return result;
	}

}
