
package acme.features.administrator.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;

@GuiService
public class AdministratorAirportListService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Airport> airports = this.repository.findAirports();

		super.getBuffer().addData(airports);
	}

	@Override
	public void unbind(final Airport airport) {

		Dataset dataset;
		dataset = super.unbindObject(airport, "name", "iataCode", "city");
		super.addPayload(dataset, airport, "operationalScope", "country", "website", "email", "contactPhoneNumber");
		super.getResponse().addData(dataset);

	}

}
