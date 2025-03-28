
package acme.features.assistanceAgent.claim;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;
import acme.realms.Customer;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		int agentId;
		int claimId;
		boolean status;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claimId = super.getRequest().getData("id", int.class);

		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		status = claim.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int claimId = super.getRequest().getData("id", int.class);

		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "isAccepted", "completed", "leg");
		Customer customer = claim.getCustomer();
		dataset.put("customer", customer.getIdentity().getFullName());
		super.getResponse().addData(dataset);
	}
}
