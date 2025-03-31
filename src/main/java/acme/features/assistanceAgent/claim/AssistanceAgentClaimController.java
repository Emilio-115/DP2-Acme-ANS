
package acme.features.assistanceAgent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimListService		listService;

	@Autowired
	private AssistanceAgentClaimShowService		showService;

	@Autowired
	private AssistanceAgentUndClaimListService	showUndService;

	@Autowired
	private AssistanceAgentClaimCreateService	createService;

	@Autowired
	private AssistanceAgentClaimDeleteService	deleteService;

	@Autowired
	private AssistanceAgentClaimUpdateService	updateService;


	@PostConstruct
	protected void initialize() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("undergoing", "list", this.showUndService);
	}
}
