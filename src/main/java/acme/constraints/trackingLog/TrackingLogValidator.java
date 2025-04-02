
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
	public boolean isValid(final TrackingLog TL, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (TL == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			TrackingLogRepository trackingLogRepository = SpringHelper.getBean(TrackingLogRepository.class);
			TrackingLogStatus TLstatus = TL.getStatus();
			var percentage = TL.getResolutionPercentage();
			if (TLstatus == null) {
				super.state(context, false, "status", "javax.validation.constraints.NotNull.message");
				return false;
			}
			if (percentage == null) {
				super.state(context, false, "resolutionPercentage", "javax.validation.constraints.NotNull.message");
				return false;
			} else {
				{
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
				{
					var updateMoment = TL.getLastUpdateMoment();
					if (updateMoment == null) {
						super.state(context, false, "lastUpdateMoment", "javax.validation.constraints.NotNull.message");
						return false;
					} else if (!updateMoment.after(TL.getClaim().getRegistrationMoment())) {
						super.state(context, false, "lastUpdateMoment", "acme.validation.tracking-log.not-good-update-date");
						return false;
					} else {
						List<TrackingLog> topDate = trackingLogRepository.findTopDateReclaim(TL.getClaim().getId());

						boolean cond;

						if (topDate == null || topDate.isEmpty())
							cond = true;
						else if (topDate.get(0).getId() == TL.getId())
							cond = TL.getLastUpdateMoment().after(topDate.get(0).getLastUpdateMoment()) || TL.getLastUpdateMoment().equals(topDate.get(0).getLastUpdateMoment());
						else if (topDate.contains(TL))
							cond = true;
						else
							cond = TL.getLastUpdateMoment().after(topDate.get(0).getLastUpdateMoment());

						super.state(context, cond, "lastUpdateMoment", "acme.validation.tracking-log.update-before-other");
					}
				}
				if (!TL.isReclaim()) {
					List<TrackingLog> topPercentage = trackingLogRepository.findTopPercentage(TL.getClaim().getId());

					boolean cond;

					if (topPercentage == null || topPercentage.isEmpty())
						cond = TL.getResolutionPercentage() >= 0.00;
					else if (topPercentage.get(0).getId() == TL.getId())
						cond = TL.getResolutionPercentage() >= topPercentage.get(0).getResolutionPercentage();
					else if (topPercentage.contains(TL))
						cond = true;
					else
						cond = TL.getResolutionPercentage() > topPercentage.get(0).getResolutionPercentage();

					super.state(context, cond, "resolutionPercentage", "acme.validation.create.tracking-log.low-percentage");
				} else {
					List<TrackingLog> topPercentage = trackingLogRepository.findTopPercentageReclaim(TL.getClaim().getId());

					boolean cond;

					if (topPercentage == null || topPercentage.isEmpty())
						cond = TL.getResolutionPercentage() >= 0.00;
					else if (topPercentage.get(0).getId() == TL.getId())
						cond = TL.getResolutionPercentage() >= topPercentage.get(0).getResolutionPercentage();
					else if (topPercentage.contains(TL))
						cond = true;
					else
						cond = TL.getResolutionPercentage() > topPercentage.get(0).getResolutionPercentage();

					super.state(context, cond, "resolutionPercentage", "acme.validation.create.tracking-log.low-percentage");
				}
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
