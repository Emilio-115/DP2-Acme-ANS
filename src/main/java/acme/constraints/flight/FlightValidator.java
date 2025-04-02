
package acme.constraints.flight;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

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

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		if (!flight.isDraftMode()) {
			List<Leg> legs = legRepository.findAllLegsByFlight(flight.getId());
			for (Leg leg : legs) {
				boolean isPublished = !leg.isDraftMode();
				super.state(context, isPublished, "draftMode", "acme.validation.flight.leg-not-published.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
