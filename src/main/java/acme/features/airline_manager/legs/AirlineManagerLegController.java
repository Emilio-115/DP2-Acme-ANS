
package acme.features.airline_manager.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.legs.Leg;
import acme.realms.airline_manager.AirlineManager;

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

	@Autowired
	private AirlineManagerLegCancelService	cancelService;

	@Autowired
	private AirlineManagerLegLandService	landService;

	@Autowired
	private AirlineManagerLegDelayService	delayService;

	@Autowired
	private AirlineManagerLegPublishService	publishService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("land", "update", this.landService);
		super.addCustomCommand("delay", "update", this.delayService);
		super.addCustomCommand("cancel", "update", this.cancelService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}
