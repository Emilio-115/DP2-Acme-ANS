
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

		if (leg.getAircraft() != null) {

			boolean isAircraftBusy = legRepository.isAircrafBusy(leg.getId(), leg.getAircraft().getId(), leg.getDepartureDate(), leg.getArrivalDate());
			super.state(context, !isAircraftBusy, "aircraft", "acme.validation.leg.busy-aircraft.message");

			if (leg.getAircraft().getAirline() != null) {

				boolean isFlightNumberUsed = legRepository.isFlightNumberUsed(leg.getId(), leg.getAircraft().getAirline().getId(), leg.getFlightNumberDigits());
				super.state(context, !isFlightNumberUsed, "flightNumberDigits", "acme.validation.leg.unique-flight-number.message");
			}
		}

		if (leg.getDepartureDate() != null) {
			boolean isDepartureAfterArrival = leg.getDepartureDate().after(leg.getArrivalDate());
			super.state(context, !isDepartureAfterArrival, "departureDate", "acme.validation.leg.arrival-before-departure.message");
		}

		super.state(context, leg.getFlight() != null, "flight", "javax.validation.constraints.NotNull.message");
		if (leg.getFlight() != null) {
			boolean isLegOverlapping = legRepository.islegOverlapping(leg.getId(), leg.getFlight().getId(), leg.getDepartureDate(), leg.getArrivalDate());
			super.state(context, !isLegOverlapping, "departureDate", "acme.validation.leg.overlapping-legs.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
