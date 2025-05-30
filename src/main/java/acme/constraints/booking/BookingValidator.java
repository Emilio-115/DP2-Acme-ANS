
package acme.constraints.booking;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.SpringHelper;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (booking == null)
			return true;

		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
		Optional<String> foundLocatorCode = bookingRepository.findLocatorCodeFromDifferentBooking(booking.getId(), booking.getLocatorCode());

		var flight = booking.getFlight();
		if (flight != null) {
			boolean flightNoDraftMode = bookingRepository.checkFlightIsStillFutureById(flight.getId(), MomentHelper.getCurrentMoment());

			super.state(context, flightNoDraftMode, "flight", "acme.validation.booking.flight.future");
		}

		Long numberPassegers = bookingRepository.countPassengersByBookingId(booking.getId());
		boolean needPassengers = !booking.isDraftMode() && numberPassegers.equals(0L);
		super.state(context, !needPassengers, "flight", "acme.validation.booking.passenger");

		boolean locatorNotUsed = foundLocatorCode.isEmpty();

		super.state(context, locatorNotUsed, "locatorCode", "acme.validation.booking.locatorCode");

		boolean needCreditCardLastNibble = !booking.isDraftMode() && booking.getCreditCardLastNibble() == null;
		super.state(context, !needCreditCardLastNibble, "creditCardLastNibble", "acme.validation.booking.creditcardlastnibble");

		result = !super.hasErrors(context);
		return result;
	}

}
