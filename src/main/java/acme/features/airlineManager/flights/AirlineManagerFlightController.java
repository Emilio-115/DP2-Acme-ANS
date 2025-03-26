
package acme.features.airlineManager.flights;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightController extends AbstractGuiController<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightService listService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);

	}

}
