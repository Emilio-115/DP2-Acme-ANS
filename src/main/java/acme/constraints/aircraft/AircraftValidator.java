
package acme.constraints.aircraft;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftRepository;

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

		result = !super.hasErrors(context);

		return result;
	}

}
