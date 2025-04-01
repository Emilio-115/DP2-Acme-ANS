
package acme.features.customer.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassengerController extends AbstractGuiController<Customer, Passenger> {

	@Autowired
	CustomerPassengerListService	listService;

	@Autowired
	CustomerPassengerShowService	showService;

	@Autowired
	CustomerPassengerListAllService	listAllService;

	@Autowired
	CustomerPassengerCreateService	createService;

	@Autowired
	CustomerPassengerUpdateService	updateService;

	@Autowired
	CustomerPassengerDeleteService	deleteService;

	@Autowired
	CustomerPassengerPublishService	publishService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-all", "list", this.listAllService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}
