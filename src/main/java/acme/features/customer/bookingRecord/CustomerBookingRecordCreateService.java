
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {

		boolean status = true;
		var a = super.getRequest();

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (super.getRequest().hasData("associatedBooking")) {
			int associatedBookingId = super.getRequest().getData("associatedBooking", int.class);
			if (associatedBookingId != 0) {
				Boolean ownsBooking = this.repository.isBookingOwnedByCustomerId(associatedBookingId, customerId);
				status = ownsBooking;
			}
		}

		if (super.getRequest().hasData("associatedPassenger")) {
			int associatedPassengerId = super.getRequest().getData("associatedPassenger", int.class);
			if (associatedPassengerId != 0) {
				Boolean ownsPassenger = this.repository.isPassengerOwnedByCustomerId(associatedPassengerId, customerId);
				status = status && ownsPassenger;
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord = new BookingRecord();
		super.getBuffer().addData(bookingRecord);

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		boolean isValid;
		Booking associatedBooking = bookingRecord.getAssociatedBooking();
		if (associatedBooking != null) {
			isValid = associatedBooking.isDraftMode();
			super.state(isValid, "associatedBooking", "acme.validation.booking.draftmode.message");
		}

	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		int bookingId = super.getRequest().getData("associatedBooking", int.class);
		int passengerId = super.getRequest().getData("associatedPassenger", int.class);
		Booking associatedBooking = this.repository.findBookingById(bookingId);
		Passenger associatedPassenger = this.repository.findPassengerById(passengerId);
		super.bindObject(bookingRecord);
		bookingRecord.setAssociatedBooking(associatedBooking);
		bookingRecord.setAssociatedPassenger(associatedPassenger);
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset;

		Collection<Booking> bookings = this.repository.findNonPublisedBookingsByCustomerId(customerId);
		Collection<Passenger> passengers = this.repository.findPublishedPassengersByCustomerId(customerId);

		SelectChoices bookingChoices;
		SelectChoices passengerChoices;
		bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingRecord.getAssociatedBooking());
		passengerChoices = SelectChoices.from(passengers, "passportNumber", bookingRecord.getAssociatedPassenger());
		dataset = super.unbindObject(bookingRecord);
		dataset.put("associatedBooking", bookingChoices.getSelected().getKey());
		dataset.put("bookingChoices", bookingChoices);

		dataset.put("associatedPassenger", passengerChoices.getSelected().getKey());
		dataset.put("passengerChoices", passengerChoices);

		super.getResponse().addData(dataset);
	}

}
