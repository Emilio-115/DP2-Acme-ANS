
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.legs.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			var departureDate = leg.getDepartureDate();
			var arrivalDate = leg.getArrivalDate();

			boolean departureBeforeArrival = departureDate.before(arrivalDate);

			super.state(context, departureBeforeArrival, "departureDate", "acme.validation.activity-log.arrival-before-departure.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
