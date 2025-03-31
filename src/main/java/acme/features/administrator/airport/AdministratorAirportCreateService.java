
package acme.features.administrator.airport;

import acme.client.services.GuiService;
import acme.entities.airports.Airport;

@GuiService
public class AdministratorAirportCreateService extends AdministratorAirportService {

	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

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
