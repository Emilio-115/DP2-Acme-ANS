
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
	public boolean isValid(final BookingRecord bookingRecord, final ConstraintValidatorContext context) {

		assert context != null;
		boolean result;

		if (bookingRecord == null)
			return true;

		BookingRepository bookingRepository = SpringHelper.getBean(BookingRepository.class);

		boolean bookingNotNull = bookingRecord.getAssociatedBooking() != null;
		boolean passengerNotNull = bookingRecord.getAssociatedPassenger() != null;
		if (bookingNotNull && passengerNotNull) {
			boolean noExistingBookingRecord = bookingRepository.findBookingRecordByBookingAndPassenger(bookingRecord.getAssociatedBooking().getId(), bookingRecord.getAssociatedPassenger().getId(), bookingRecord.getId()).isEmpty();
			super.state(context, noExistingBookingRecord, "associatedPassenger", "acme.validation.bookingrecord.passenger");
		}
		if (passengerNotNull) {
			boolean passengerNoDraftMode = !bookingRepository.findPassengerDrafModeById(bookingRecord.getAssociatedPassenger().getId());
			super.state(context, passengerNoDraftMode, "associatedPassenger", "acme.validation.bookingrecord.passenger.draftmode");
		}

		result = !super.hasErrors(context);
		return result;
	}
}
