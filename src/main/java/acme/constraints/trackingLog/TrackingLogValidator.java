
package acme.constraints.trackingLog;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.trackingLogs.TrackingLog;
import acme.entities.trackingLogs.TrackingLogRepository;
import acme.entities.trackingLogs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			TrackingLogRepository trackingLogRepository = SpringHelper.getBean(TrackingLogRepository.class);
			TrackingLogStatus TLstatus = trackingLog.getStatus();
			var percentage = trackingLog.getResolutionPercentage();
			if (TLstatus == null) {
				super.state(context, false, "status", "javax.validation.constraints.NotNull.message");
				return false;
			}
			if (percentage == null) {
				super.state(context, false, "resolutionPercentage", "javax.validation.constraints.NotNull.message");
				return false;
			} else {
				boolean statusWrongCompleted = percentage == 100.00 && !TLstatus.equals(TrackingLogStatus.PENDING);
				boolean statusWrongUngoing = percentage < 100.00 && TLstatus.equals(TrackingLogStatus.PENDING);

				boolean res = statusWrongCompleted || statusWrongUngoing;

				super.state(context, res, "status", "acme.validation.percentage.message");
				{
					String resolutionDescription = trackingLog.getResolution();
					boolean correctDescriptionCompleted = !TLstatus.equals(TrackingLogStatus.PENDING) && !resolutionDescription.isEmpty();
					boolean correctDescriptionUncompleted = TLstatus.equals(TrackingLogStatus.PENDING) && resolutionDescription.isEmpty();

					boolean correctDescription = correctDescriptionCompleted || correctDescriptionUncompleted;

					super.state(context, correctDescription, "resolution", "acme.validation.resolutionDescription.message");
				}
				{
					var updateMoment = trackingLog.getLastUpdateMoment();
					if (updateMoment == null) {
						super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
						return false;
					} else if (updateMoment.before(trackingLog.getClaim().getRegistrationMoment())) {
						super.state(context, false, "*", "acme.validation.tracking-log.not-good-update-date");
						return false;
					} else {
						List<TrackingLog> topDate = trackingLogRepository.findTopDateReclaim(trackingLog.getClaim().getId(), trackingLog.isReclaim());

						boolean cond;

						if (topDate == null || topDate.isEmpty())
							cond = true;
						else if (topDate.get(0).getId() == trackingLog.getId())
							cond = true;
						else if (topDate.contains(trackingLog)) {
							Integer ind = topDate.indexOf(trackingLog);
							cond = ind == topDate.size() - 1 ? !trackingLog.getLastUpdateMoment().before(topDate.get(ind).getLastUpdateMoment()) : true;
						} else
							cond = !trackingLog.getLastUpdateMoment().before(topDate.get(0).getLastUpdateMoment());

						super.state(context, cond, "*", "acme.validation.tracking-log.update-before-other");
					}
				}
				{
					List<TrackingLog> topPercentage = trackingLogRepository.findTopPercentage(trackingLog.getClaim().getId(), trackingLog.isReclaim());

					boolean cond;

					if (topPercentage == null || topPercentage.isEmpty())
						cond = trackingLog.getResolutionPercentage() >= 0.00;
					else if (topPercentage.get(0).getId() == trackingLog.getId())
						cond = trackingLog.getResolutionPercentage() >= topPercentage.get(0).getResolutionPercentage();
					else if (topPercentage.contains(trackingLog))
						cond = true;
					else
						cond = trackingLog.getResolutionPercentage() > topPercentage.get(0).getResolutionPercentage();

					super.state(context, cond, "resolutionPercentage", "acme.validation.create.tracking-log.low-percentage");
				}
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
