
package acme.features.airline_manager.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiController
public class AirlineManagerLegController extends AbstractGuiController<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegCreateService	createService;

	@Autowired
	private AirlineManagerLegListService	listService;

	@Autowired
	private AirlineManagerLegShowService	showService;

	@Autowired
	private AirlineManagerLegUpdateService	updateService;

	@Autowired
	private AirlineManagerLegDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

	}

}
