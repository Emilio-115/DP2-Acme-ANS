
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
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
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	CustomerBookingRepository repository;


	@Override
	public void authorise() {
		int bookingId = super.getRequest().getData("id", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status;
		Optional<Booking> booking = this.repository.findByIdAndCustomerId(bookingId, customerId);
		status = booking.isPresent() && booking.get().isDraftMode();
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
		super.bindObject(booking);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final Booking booking) {
		boolean hasPassengers = !this.repository.findPassengersByBookingId(booking.getId()).isEmpty();
		boolean hasLastNibble = !booking.getCreditCardLastNibble().isBlank();
		super.state(hasPassengers, "*", "acme.validation.booking.passenger");
		super.state(hasLastNibble, "creditCardLastNibble", "acme.validation.booking.creditcardlastnibble");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {

		Date currentMoment = MomentHelper.getCurrentMoment();
		Collection<Flight> availableFlights = this.repository.findAvailableFlights(currentMoment);
		boolean flightAvailable = this.repository.checkFlightIsAvailableById(booking.getFlight().getId(), currentMoment);
		Dataset dataset;
		SelectChoices choices;
		SelectChoices flightChoices;
		choices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		flightChoices = SelectChoices.from(availableFlights, "tag", flightAvailable ? booking.getFlight() : null);
		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "creditCardLastNibble", "draftMode");

		dataset.put("price", booking.price());
		dataset.put("travelClasses", choices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flightTagChoices", flightChoices);

		dataset.put("flightSelfTransfer", flightAvailable ? booking.getFlight().isRequiresSelfTransfer() : "No Data");
		dataset.put("flightDescription", flightAvailable ? booking.getFlight().getDescription() : "No Data");
		dataset.put("departureDate", flightAvailable ? booking.getFlight().scheduledDeparture() : "No Data");
		dataset.put("arrivalDate", flightAvailable ? booking.getFlight().scheduledArrival() : "No Data");
		dataset.put("origin", flightAvailable ? booking.getFlight().origin() : "No Data");
		dataset.put("destination", flightAvailable ? booking.getFlight().destination() : "No Data");
		dataset.put("numberOfLayovers", flightAvailable ? booking.getFlight().numberOfLayovers() : "No Data");

		super.getResponse().addData(dataset);
		super.getResponse().addData(dataset);
	}
}
