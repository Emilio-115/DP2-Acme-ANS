
package acme.features.customer.bookingRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<BookingRecord> bookingRecords = this.repository.findBookingRecordByCustomerId(customerId);
		super.getBuffer().addData(bookingRecords);

	}
	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord);
		dataset.put("associatedBooking", bookingRecord.getAssociatedBooking().getLocatorCode());
		dataset.put("associatedPassenger", bookingRecord.getAssociatedPassenger().getPassportNumber());
		super.addPayload(dataset, bookingRecord, "associatedPassenger.fullName");
		super.getResponse().addData(dataset);

	}
}
