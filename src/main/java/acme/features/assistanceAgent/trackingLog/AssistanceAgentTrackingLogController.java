
package acme.features.assistanceAgent.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentTrackingLogController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogListService	listService;

	@Autowired
	private AssistanceAgentTrackingLogShowService	showService;

	@Autowired
	private AssistanceAgentTrackingLogCreateService	createService;

	/*
	 * @Autowired
	 * private AssistanceAgentClaimDeleteService deleteService;
	 * 
	 * @Autowired
	 * private AssistanceAgentClaimUpdateService updateService;
	 * 
	 * @Autowired
	 * private AssistanceAgentClaimPublishService publishService;
	 */


	@PostConstruct
	protected void initialize() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		//super.addBasicCommand("delete", this.deleteService);
		//super.addBasicCommand("update", this.updateService);

		//super.addCustomCommand("publish", "update", this.publishService);
		//super.addCustomCommand("undergoing", "list", this.showUndService);
	}
}
