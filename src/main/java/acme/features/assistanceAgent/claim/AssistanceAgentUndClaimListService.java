
package acme.features.assistanceAgent.claim;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentUndClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent assistanceAgent;
		int userAccountId;

		List<Claim> completedClaims;
		int id;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgent = this.repository.findAssistanceAgentById(userAccountId);

		id = assistanceAgent.getId();
		completedClaims = this.repository.findClaimsByAssistanceAgent(id);
		completedClaims = completedClaims.stream().filter(x -> x.getIsAccepted().equals(ClaimStatus.PENDING)).toList();
		super.getBuffer().addData(completedClaims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "isAccepted", "type");
		dataset.put("leg", claim.getLeg().getFlightNumberDigits());

		super.getResponse().addData(dataset);
	}
}
