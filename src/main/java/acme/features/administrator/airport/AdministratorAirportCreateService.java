
package acme.features.administrator.airport;

import acme.client.services.GuiService;
import acme.entities.airports.Airport;

@GuiService
public class AdministratorAirportCreateService extends AdministratorAirportService {

	@Override
	public void authorise() {
		Integer airportId = super.getRequest().getData("id", Integer.class, null);

		boolean status = airportId == null || airportId == 0;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport = new Airport();

		super.getBuffer().addData(airport);
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

}
