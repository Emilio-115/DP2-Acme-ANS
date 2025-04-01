
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
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

		Collection<Flight> availableFlights = this.repository.findAvailableFlights(MomentHelper.getCurrentMoment());
		if (!availableFlights.contains(booking.getFlight()))
			availableFlights.add(booking.getFlight());
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		flightChoices = SelectChoices.from(availableFlights, "tag", booking.getFlight());
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardLastNibble", "draftMode");

		dataset.put("price", booking.price());
		dataset.put("travelClasses", choices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flightTagChoices", flightChoices);

		dataset.put("flightSelfTransfer", booking.getFlight().isRequiresSelfTransfer());
		dataset.put("flightDescription", booking.getFlight().getDescription());
		dataset.put("departureDate", booking.getFlight().scheduledDeparture());
		dataset.put("arrivalDate", booking.getFlight().scheduledArrival());
		dataset.put("origin", booking.getFlight().origin());
		dataset.put("destiny", booking.getFlight().destiny());
		dataset.put("numberOfLayovers", booking.getFlight().numberOfLayovers());

		super.getResponse().addData(dataset);
	}
}
