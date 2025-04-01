
package acme.features.flightCrewMember.activityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiController
public class FlightCrewMemberActivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogListPublishedService	listPublishedService;

	@Autowired
	private FlightCrewMemberActivityLogListDraftsService	listDraftsService;

	@Autowired
	private FlightCrewMemberActivityLogShowService			showService;

	@Autowired
	private FlightCrewMemberActivityLogCreateService		createService;

	@Autowired
	private FlightCrewMemberActivityLogUpdateService		updateService;

	@Autowired
	private FlightCrewMemberActivityLogDeleteService		deleteService;

	@Autowired
	private FlightCrewMemberActivityLogPublishService		publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listPublishedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-drafts", "list", this.listDraftsService);
	}

}
