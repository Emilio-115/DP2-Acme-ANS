
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.legs.Leg;
import acme.entities.legs.LegRepository;
import acme.entities.legs.LegStatus;

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
			return true;

		LegRepository legRepository = SpringHelper.getBean(LegRepository.class);

		if (leg.getAircraft() != null) {

			boolean isAircraftBusy = legRepository.isAircrafBusy(leg.getId(), leg.getAircraft().getId(), leg.getDepartureDate(), leg.getArrivalDate());
			super.state(context, !isAircraftBusy, "aircraft", "acme.validation.leg.busy-aircraft.message");

			if (leg.getAircraft().getAirline() != null) {

				boolean isFlightNumberUsed = legRepository.isFlightNumberUsed(leg.getId(), leg.getAircraft().getAirline().getId(), leg.getFlightNumberDigits());
				super.state(context, !isFlightNumberUsed, "flightNumberDigits", "acme.validation.leg.unique-flight-number.message");
			}
		}

		if (leg.getDepartureDate() != null && leg.getArrivalDate() != null) {
			boolean isDepartureDateBeforeArrivalDate = leg.getDepartureDate().before(leg.getArrivalDate());
			super.state(context, isDepartureDateBeforeArrivalDate, "departureDate", "acme.validation.leg.arrival-before-departure.message");
		}

		if (leg.getFlight() != null) {
			boolean isLegOverlapping = legRepository.isLegOverlapping(leg.getId(), leg.getFlight().getId(), leg.getDepartureDate(), leg.getArrivalDate());
			super.state(context, !isLegOverlapping, "departureDate", "acme.validation.leg.overlapping-legs.message");

			if (leg.getStatus() != null) {
				boolean isLegStatusConsistentWithDraftMode = !leg.isDraftMode() || leg.getStatus().equals(LegStatus.ON_TIME);
				super.state(context, isLegStatusConsistentWithDraftMode, "status", "acme.validation.leg.draft-mode-status.message");
			}

		}

		if (leg.getArrivalAirport() != null && leg.getDepartureAirport() != null)
			super.state(context, leg.getArrivalAirport().getId() != leg.getDepartureAirport().getId(), "departureAirport", "acme.validation.leg.same-airport.message");

		result = !super.hasErrors(context);

		return result;
	}

}
