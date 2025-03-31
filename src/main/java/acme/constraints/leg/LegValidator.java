
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;

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

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		boolean isAircraftBusy = legRepository.isAircrafBusy(leg.getId(), leg.getAircraft().getId(), leg.getDepartureDate(), leg.getArrivalDate());
		super.state(context, !isAircraftBusy, "departureDate", "acme.validation.leg.busy-aircraft.message");

		boolean isDepartureAfterArrival = leg.getDepartureDate().after(leg.getArrivalDate());
		super.state(context, !isDepartureAfterArrival, "aircraft", "acme.validation.leg.arrival-before-departure.message");

		boolean isLegOverlapping = legRepository.islegOverlapping(leg.getId(), leg.getFlight().getId(), leg.getDepartureDate(), leg.getArrivalDate());
		super.state(context, !isLegOverlapping, "dates", "acme.validation.activity-log.overlapping-legs.message");

		result = !super.hasErrors(context);

		return result;
	}

}
