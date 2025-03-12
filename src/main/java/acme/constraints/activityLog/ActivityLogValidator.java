
package acme.constraints.activityLog;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLogs.ActivityLog;

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
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			var registeredAt = activityLog.getRegisteredAt();
			var arrivalDate = activityLog.getRegisteringAssignment().getLeg().getArrivalDate();

			boolean registeredAfterLanding = registeredAt.after(arrivalDate);

			super.state(context, registeredAfterLanding, "registeredAt", "acme.validation.activity-log.registered-before-landing.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
