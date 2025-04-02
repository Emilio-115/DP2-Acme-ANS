
package acme.features.airlineManager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.airlineManager.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Flight> flights;
		int airlineManagerId;

		airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findAllByManagerId(airlineManagerId);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;
		dataset = super.unbindObject(flight, "id");
		dataset.put("origin", flight.origin());
		dataset.put("destination", flight.destination());
		dataset.put("departureDate", flight.scheduledDeparture());
		dataset.put("arrivalDate", flight.scheduledArrival());
		dataset.put("published", !flight.isDraftMode());
		super.getResponse().addData(dataset);
	}

}
