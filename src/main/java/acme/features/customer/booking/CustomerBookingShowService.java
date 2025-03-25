
package acme.features.customer.booking;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {

		int customerId;
		int bookingId;
		boolean status;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		bookingId = super.getRequest().getData("id", int.class);

		Optional<Booking> booking = this.repository.findByIdAndCustomerId(bookingId, customerId);

		status = booking.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int bookingId = super.getRequest().getData("id", int.class);

		booking = this.repository.findBookingById(bookingId);

		super.getBuffer().addData(booking);
	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;
		SelectChoices choices;
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardLastNibble");
		dataset.put("price", booking.price());
		dataset.put("travelClasses", choices);
		super.getResponse().addData(dataset);
	}
}
