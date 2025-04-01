
package acme.features.administrator.airline;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAirlineListService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Airline> airlines;
		int administratorId;

		administratorId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlines = this.repository.findAllAirlines();

		super.getBuffer().addData(airlines);
	}

	@Override
	public void unbind(final Airline airline) {

		Dataset dataset;
		dataset = super.unbindObject(airline, "id");
		dataset.put("name", airline.getName());
		dataset.put("iataCode", airline.getIataCode());
		dataset.put("departureDate", airline.getWebsite());
		dataset.put("type", airline.getType());
		dataset.put("foundationMoment", airline.getFoundationMoment());
		dataset.put("email", airline.getEmail());
		dataset.put("phoneNumber", airline.getPhoneNumber());
		super.getResponse().addData(dataset);
	}
}
