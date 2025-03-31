
package acme.constraints.leg;

import java.util.List;

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
		boolean isAircraftBusy = legRepository.isAircrafBusy(leg, leg.getAircraft(), leg.getDepartureDate(), leg.getArrivalDate());

		if (leg.getDepartureDate().after(leg.getArrivalDate()))
			super.state(context, false, "departureDate", "acme.validation.leg.arrival-before-departure.message");
		else if (isAircraftBusy)
			super.state(context, false, "aircraft", "acme.validation.leg.busy-aircraft.message");
		else {
			List<Leg> legsOfFlight = legRepository.findAllLegsByFlight(leg.getFlight());

			for (Leg l : legsOfFlight) {

				if (leg.equals(l))
					continue;

				boolean areDatesBeforeDeparture = leg.getArrivalDate().before(l.getDepartureDate()) && leg.getDepartureDate().before(l.getDepartureDate());
				boolean areDatesAfterArrival = leg.getArrivalDate().after(l.getArrivalDate()) && leg.getDepartureDate().after(l.getArrivalDate());

				boolean areDatesCorrect = areDatesAfterArrival || areDatesBeforeDeparture;

				super.state(context, areDatesCorrect, "dates", "acme.validation.leg.overlapping-legs.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
