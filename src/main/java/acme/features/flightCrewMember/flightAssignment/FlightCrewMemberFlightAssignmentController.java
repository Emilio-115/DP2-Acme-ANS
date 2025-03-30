
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentListPlannedService		listPlannedService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListInProgressService	listInProgressService;

	@Autowired
	private FlightCrewMemberFlightAssignmentListDraftsService		listDraftsService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService				showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService			createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService			updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService			deleteService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService			publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addCustomCommand("list-in-progress", "list", this.listInProgressService);
		super.addCustomCommand("list-drafts", "list", this.listDraftsService);
	}

}
