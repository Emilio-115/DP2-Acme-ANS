
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
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	CustomerBookingRepository repository;


	@Override
	public void authorise() {

		boolean status = true;
		if (super.getRequest().hasData("flight")) {
			int flightId = super.getRequest().getData("flight", int.class);
			if (flightId != 0) {
				Optional<Boolean> flightIsDraftmodeOpt = this.repository.findFlightDraftmodeValueById(flightId);
				if (flightIsDraftmodeOpt.isPresent())
					status = !flightIsDraftmodeOpt.get();
			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		Booking booking = new Booking();
		booking.setCustomer(customer);

		booking.setDraftMode(true);
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
		;
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
		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "creditCardLastNibble");

		dataset.put("travelClasses", choices);
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flightTagChoices", flightChoices);

		super.getResponse().addData(dataset);
	}
}
