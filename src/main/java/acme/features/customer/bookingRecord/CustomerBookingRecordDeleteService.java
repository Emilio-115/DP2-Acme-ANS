
package acme.features.customer.bookingRecord;

import java.util.Collection;
import java.util.Optional;

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
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		int bookingRecordId = super.getRequest().getData("id", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status;
		Optional<BookingRecord> bookingRecord = this.repository.findByBookingRecordIdAndCustomerId(bookingRecordId, customerId);
		status = bookingRecord.isPresent();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int bookingRecordId = super.getRequest().getData("id", int.class);
		BookingRecord bookingRecord = this.repository.findBookingRecordById(bookingRecordId);
		super.getBuffer().addData(bookingRecord);

	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		boolean canDelete = bookingRecord.getAssociatedBooking().isDraftMode();
		super.state(canDelete, "associatedBooking", "acme.validation.bookingrecord.delete");

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {

		this.repository.delete(bookingRecord);

	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset;

		Collection<Booking> bookings = this.repository.findBookingsByCustomerId(customerId);
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
