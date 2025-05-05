
package acme.features.assistanceAgent.trackingLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentTrackingLogController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogListService		listService;

	@Autowired
	private AssistanceAgentTrackingLogShowService		showService;

	/*
	 * Mientras una tracking log no este publicada no se podra crear otra. Este boton desaparece cuando se llega al
	 * 100 de progreso. El funcionamiento con el boton reclaim es igual.
	 */
	@Autowired
	private AssistanceAgentTrackingLogCreateService		createService;

	@Autowired
	private AssistanceAgentTrackingLogUpdateService		updateService;

	@Autowired
	private AssistanceAgentTrackingLogDeleteService		deleteService;

	/*
	 * Una vez que se publique una tracking log con un 100% de progreso, el estado (aceptado o rechazado)
	 * modificara el estado de claim. Mientras este en modo borrador no lo hara
	 */
	@Autowired
	private AssistanceAgentTrackingLogPublishService	publishService;

	/*
	 * Solo esta disponible cuando una tracking log con un 100% de progreso es publicada
	 */
	@Autowired
	private AssistanceAgentTrackingLogReclaimService	reclaimService;


	@PostConstruct
	protected void initialize() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("reclaim", "create", this.reclaimService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
