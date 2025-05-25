
package acme.features.assistanceAgent.claim;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Request;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AuthoriseAndLoadMethods extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	public boolean securityId(final Request request) {
		String method = super.getRequest().getMethod();
		boolean status = true;
		if (method.equals("POST")) {
			int claimId = super.getRequest().getData("id", int.class);
			int securityId = super.getRequest().getData("claimId", int.class);

			status = claimId == securityId;
		}
		return status;
	}

	public Optional<Claim> getClaim(final int agentId, final Request request) {
		String method = request.getMethod();

		int claimId;
		if (method.equals("GET"))
			claimId = request.getData("claimId", int.class);
		else
			claimId = request.getData("id", int.class);
		Optional<Claim> claim = this.repository.findByIdAndAssistanceAgentId(claimId, agentId);

		return claim;
	}

	public Leg getLeg(final Request request) {
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Claim claim = this.getClaim(agentId, request).get();
		return claim.getLeg();
	}

	public boolean validLeg(final Request request) {
		Leg leg = this.getLeg(request);
		Collection<Leg> legs = this.repository.findAllLandedLegs(LegStatus.LANDED);
		boolean status = true;

		if (request.getMethod().equals("POST"))
			status = leg != null && legs.contains(leg);

		return status;
	}
}
