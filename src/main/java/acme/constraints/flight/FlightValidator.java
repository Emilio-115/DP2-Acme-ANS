
package acme.constraints.flight;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.flights.Flight;

@Validator
public class FlightValidator extends AbstractValidator<ValidFlight, Flight> {

	@Override
	protected void initialise(final ValidFlight annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Flight flight, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flight == null)
			return true;

		boolean hasALeg = flight.numberOfLayovers() >= 0;
		if (!flight.isDraftMode())
			super.state(context, hasALeg, "draftMode", "acme.validation.flight.no-legs.message");

		result = !super.hasErrors(context);

		return result;
	}

}
