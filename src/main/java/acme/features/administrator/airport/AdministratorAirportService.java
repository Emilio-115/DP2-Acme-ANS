
package acme.features.administrator.airport;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.entities.airports.Airport;
import acme.entities.airports.OperationalScope;

public class AdministratorAirportService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	protected AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		Integer airportId = super.getRequest().getData("id", Integer.class, null);
		Optional<Airport> airport = this.repository.findAirportById(airportId);

		boolean status = airport.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int airportId = super.getRequest().getData("id", int.class);
		Airport airport = this.repository.findAirportById(airportId).get();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "contactPhoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void unbind(final Airport airport) {
		Dataset dataset;
		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "contactPhoneNumber");

		{
			SelectChoices operationalScopes;
			operationalScopes = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

			dataset.put("operationalScopes", operationalScopes);
		}

		super.getResponse().addData(dataset);
	}
}
