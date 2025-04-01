
package acme.features.airline_manager.flights;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerFlightController extends AbstractGuiController<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightCreateService	createService;

	@Autowired
	private AirlineManagerFlightListService		listService;

	@Autowired
	private AirlineManagerFlightShowService		showService;

	@Autowired
	private AirlineManagerFlightUpdateService	updateService;

	@Autowired
	private AirlineManagerFlightDeleteService	deleteService;

	@Autowired
	private AirlineManagerFlightPublishService	publishService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);

	}

}
