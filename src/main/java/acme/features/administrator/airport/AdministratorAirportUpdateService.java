
package acme.features.administrator.airport;

import acme.client.services.GuiService;
import acme.entities.airports.Airport;

@GuiService
public class AdministratorAirportUpdateService extends AdministratorAirportService {

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}
}
