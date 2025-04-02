
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

		if (activityLog == null)
			return true;

		var registeringAssignment = activityLog.getRegisteringAssignment();

		if (registeringAssignment != null) {

			boolean assignmentIsPublished = !registeringAssignment.isDraftMode();
			super.state(context, assignmentIsPublished, "registeringAssignment", "acme.validation.activity-log.assignment-not-published.message");

			boolean assignmentIsConfirmed = FlightAssignmentStatus.CONFIRMED.equals(registeringAssignment.getStatus());
			super.state(context, assignmentIsConfirmed, "registeringAssignment", "acme.validation.activity-log.assignment-not-confirmed.message");

			var registeredAt = activityLog.getRegisteredAt();
			var leg = registeringAssignment.getLeg();

			if (leg != null) {
				var arrivalDate = leg.getArrivalDate();

				if (registeredAt != null && arrivalDate != null) {

					boolean registeredAfterLanding = registeredAt.after(arrivalDate);
					super.state(context, registeredAfterLanding, "registeredAt", "acme.validation.activity-log.registered-before-landing.message");

				}
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
