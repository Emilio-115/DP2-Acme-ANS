
package acme.constraints.bookingRecord;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.BookingRepository;

@Validator
public class BookingRecordValidator extends AbstractValidator<ValidBookingRecord, BookingRecord> {

	@Override
	protected void initialise(final ValidBookingRecord annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final BookingRecord value, final ConstraintValidatorContext context) {

		assert context != null;
		boolean result;

		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);

		boolean bookingNotNull = value.getAssociatedBooking() != null;
		if (bookingNotNull) {
			boolean noExistingBookingRecord = bookingRepository.findBookingRecordByBookingAndPassenger(value.getAssociatedBooking().getId(), value.getAssociatedPassenger().getId()).isEmpty();
			super.state(context, noExistingBookingRecord, "associatedPassenger", "acme.validation.bookingrecord.passenger");
		}
		if (value.getAssociatedPassenger() != null) {
			boolean passengerNoDraftMode = !bookingRepository.findPassengerDrafModeById(value.getAssociatedPassenger().getId());
			super.state(context, passengerNoDraftMode, "associatedPassenger", "acme.validation.bookingrecord.passenger.draftmode");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
