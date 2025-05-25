
package acme.constraints.aircraft;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftRepository;
import acme.entities.aircrafts.AircraftStatus;

@Validator
public class AircraftValidator extends AbstractValidator<ValidAircraft, Aircraft> {

	@Override
	protected void initialise(final ValidAircraft annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Aircraft aircraft, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (aircraft == null)
			return true;

		AircraftRepository aircraftRepository = SpringHelper.getBean(AircraftRepository.class);
		List<Aircraft> aircrafts = aircraftRepository.findAllByRegistrationNumber(aircraft.getId(), aircraft.getRegistrationNumber());
		boolean isIndentifierFree = aircrafts.isEmpty();

		super.state(context, isIndentifierFree, "registrationNumber", "acme.validation.aircraft.non-unique-registration-number");

		if (aircraft.getStatus() != null)
			if (aircraft.getStatus().equals(AircraftStatus.UNDER_MAINTENANCE)) {
				Date currentDate = MomentHelper.getCurrentMoment();
				boolean isAircraftBusyInTheFuture = aircraftRepository.isAircraftBusyInTheFuture(aircraft.getId(), currentDate);
				super.state(context, !isAircraftBusyInTheFuture, "status", "acme.validation.aircraft.busy-cant-be-under-maintenance");
			}

		result = !super.hasErrors(context);

		return result;
	}

}
