
package acme.features.authenticated.claim;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.entities.claims.Claim;
import acme.realms.AssistanceAgent;

public class AuthenticatedListClaimService extends AbstractGuiService<Authenticated, Claim> {

	@Autowired
	private AuthenticatedClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AssistanceAgent assistanceAgent;
		int userAccountId;

		List<Claim> completedClaims = new ArrayList<>();
		int id;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		assistanceAgent = this.repository.findAssistanceAgentById(userAccountId);

		id = assistanceAgent.getId();
		completedClaims = this.repository.findClaimsByAssistanceAgent(id);

		super.getBuffer().addData(completedClaims);
	}

	@Override
	public void unbind(final Claim claimList) {
		Dataset dataset;

		dataset = super.unbindObject(claimList, "registrationMoment", "passengerEmail", "description", "type", "isAccepted", "completed", "assistanceAgent", "leg");

		super.getResponse().addData(dataset);
	}
}
