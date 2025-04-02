
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
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	CustomerBookingRepository repository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status;
		Optional<Booking> booking = this.repository.findByIdAndCustomerId(bookingId, customerId);
		status = booking.isPresent();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int bookingId = super.getRequest().getData("id", int.class);
		Booking booking = this.repository.findBookingById(bookingId);
		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		int flightId = super.getRequest().getData("flight", int.class);
		Flight flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "creditCardLastNibble");
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		boolean notPublished = booking.isDraftMode();
		super.state(notPublished, "draftMode", "acme.validation.update.draftMode");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {

		Collection<Flight> availableFlights = this.repository.findAvailableFlights(MomentHelper.getCurrentMoment());
		if (booking.getFlight() != null && !availableFlights.contains(booking.getFlight()))
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

		dataset.put("flightSelfTransfer", booking.getFlight() != null ? booking.getFlight().isRequiresSelfTransfer() : "No Data");
		dataset.put("flightDescription", booking.getFlight() != null ? booking.getFlight().getDescription() : "No Data");
		dataset.put("departureDate", booking.getFlight() != null ? booking.getFlight().scheduledDeparture() : "No Data");
		dataset.put("arrivalDate", booking.getFlight() != null ? booking.getFlight().scheduledArrival() : "No Data");
		dataset.put("origin", booking.getFlight() != null ? booking.getFlight().origin() : "No Data");
		dataset.put("destination", booking.getFlight() != null ? booking.getFlight().destination() : "No Data");
		dataset.put("numberOfLayovers", booking.getFlight() != null ? booking.getFlight().numberOfLayovers() : "No Data");

		super.getResponse().addData(dataset);
		super.getResponse().addData(dataset);
	}
}
