
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.GuiService;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAirlineUpdateService extends AdministratorAirlineService {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}
}
