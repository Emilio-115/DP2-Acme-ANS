
package acme.features.airline_manager.flights;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.airline_manager.AirlineManager;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {

		int managerId;
		int flightId;
		boolean status;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightId = super.getRequest().getData("id", int.class);

		Optional<Flight> optionalFlight = this.repository.findByIdAndManagerId(flightId, managerId);

		status = optionalFlight.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight = null;
		int flightId = super.getRequest().getData("id", int.class);

		Optional<Flight> flightOptional = this.repository.findFlightById(flightId);

		if (flightOptional.isPresent()) {
			flight = flightOptional.get();
			super.getBuffer().addData(flight);
		}

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset.put("origin", flight.origin());
		dataset.put("destiny", flight.destiny());
		dataset.put("departureDate", flight.scheduledDeparture());
		dataset.put("arrivalDate", flight.scheduledArrival());
		dataset.put("numberOfLayovers", flight.numberOfLayovers());
		super.getResponse().addData(dataset);
	}

}
