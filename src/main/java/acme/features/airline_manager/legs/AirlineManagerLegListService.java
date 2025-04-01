
package acme.features.airline_manager.legs;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.features.airline_manager.flights.AirlineManagerFlightRepository;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository		repository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {
		boolean status = true;
		Integer flightId = super.getRequest().getData("flightId", int.class);
		Integer airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Optional<Flight> optionalFlight = this.repository.findByIdAndManagerId(flightId, airlineManagerId);

		status &= optionalFlight.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Leg> legs;

		Integer flightId = super.getRequest().getData("flightId", int.class);

		legs = this.repository.findAllByFlightId(flightId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		dataset = super.unbindObject(leg, "departureDate", "arrivalDate", "status", "draftMode");
		dataset.put("departureAirport", leg.getDepartureAirport().getCity());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getCity());
		dataset.put("flight", leg.getFlight());
		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<Leg> legs) {
		Integer flightId = super.getRequest().getData("flightId", int.class);
		super.getResponse().addGlobal("flightId", flightId);

		Flight flight = this.flightRepository.findFlightById(flightId).get();
		super.getResponse().addGlobal("flightDraftMode", flight.isDraftMode());
	}

}
