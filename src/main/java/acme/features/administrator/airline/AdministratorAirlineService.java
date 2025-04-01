
package acme.features.administrator.airline;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineType;

@GuiService
public class AdministratorAirlineService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	protected AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		int airlineId = super.getRequest().getData("id", int.class);
		Optional<Airline> airline = this.repository.findAirlineById(airlineId);

		boolean status = airline.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int airlineId = super.getRequest().getData("id", int.class);
		Airline airline = this.repository.findAirlineById(airlineId).get();

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		dataset = super.unbindObject(airline, "name", "iataCode", "website", "type", "foundationMoment", "email", "phoneNumber");

		{
			SelectChoices types;
			types = SelectChoices.from(AirlineType.class, airline.getType());

			dataset.put("types", types);
		}

		super.getResponse().addData(dataset);
	}
}
