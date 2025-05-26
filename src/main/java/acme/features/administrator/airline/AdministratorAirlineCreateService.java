
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.GuiService;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAirlineCreateService extends AdministratorAirlineService {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {

		boolean status;
		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = true;
		else {
			int id = super.getRequest().getData("id", int.class);
			status = id == 0;
		}
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Airline airline = new Airline();

		super.getBuffer().addData(airline);
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

}
