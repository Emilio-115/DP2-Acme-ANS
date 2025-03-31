
package acme.constraints.booking;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
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
	public boolean isValid(final Booking value, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);
		Long numberPassegers = bookingRepository.countPassangersByBookingId(value.getId());
		Optional<String> foundLocatorCode = bookingRepository.findLocatorCodeFromDifferentBooking(value.getId(), value.getLocatorCode());
		boolean flightNoDraftMode = !value.getFlight().isDraftMode();

		super.state(context, flightNoDraftMode, "*", "acme.validation.booking.flight.draftMode");

		boolean locatorNotUsed = foundLocatorCode.isEmpty();

		super.state(context, locatorNotUsed, "locatorCode", "acme.validation.booking.locatorCode");

		boolean needCreditCardLastNibble = !value.isDraftMode() && value.getCreditCardLastNibble() == null;
		super.state(context, !needCreditCardLastNibble, "creditCardLastNibble", "acme.validation.booking.creditcardlastnibble");

		boolean needPassengers = !value.isDraftMode() && numberPassegers.equals(0L);
		super.state(context, !needPassengers, "*", "acme.validation.booking.passenger");

		result = !super.hasErrors(context);
		return result;
	}

}
