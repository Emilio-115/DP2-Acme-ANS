
package acme.constraints.trackingLog;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog TL, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (TL == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			TrackingLogStatus TLstatus = TL.getStatus();
			if (TLstatus == null) {
				super.state(context, false, "status", "javax.validation.constraints.NotNull.message");
				return false;
			} else {
				{
					double percentage = TL.getResolutionPercentage();
					boolean correctStatusCompleted = percentage < 100.00 && TLstatus.equals(TrackingLogStatus.PENDING);
					boolean correctStatusUncompleted = percentage >= 100.00 && !TLstatus.equals(TrackingLogStatus.PENDING);

					boolean correctStatus = correctStatusCompleted || correctStatusUncompleted;

					super.state(context, correctStatus, "status", "acme.validation.percentage.message");

				}
				{
					String resolutionDescription = TL.getResolution();
					boolean correctDescriptionCompleted = !TLstatus.equals(TrackingLogStatus.PENDING) && !resolutionDescription.isEmpty();
					boolean correctDescriptionUncompleted = TLstatus.equals(TrackingLogStatus.PENDING) && resolutionDescription.isEmpty();

					boolean correctDescription = correctDescriptionCompleted || correctDescriptionUncompleted;

					super.state(context, correctDescription, "resolution", "acme.validation.resolutionDescription.message");
				}
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
