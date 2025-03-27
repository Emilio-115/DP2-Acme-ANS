
package acme.features.authenticated.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.entities.claims.Claim;

public class AuthenticatedClaimController extends AbstractGuiController<Authenticated, Claim> {

	@Autowired
	private AuthenticatedListClaimService listService;


	@PostConstruct
	protected void initialize() {
		super.addBasicCommand("list", this.listService);
	}
}
