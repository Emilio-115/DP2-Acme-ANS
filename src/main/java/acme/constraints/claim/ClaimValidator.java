
package acme.constraints.claim;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.claims.Claim;

@Validator
public class ClaimValidator extends AbstractValidator<ValidClaim, Claim> {

	@Override
	protected void initialise(final ValidClaim annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (claim == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var registrationMoment = claim.getRegistrationMoment();
		if (registrationMoment == null) {
			super.state(context, false, "registrationMoment", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var leg = claim.getLeg();
		if (leg == null) {
			super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");
			return false;
		}

		var accepted = claim.getIsAccepted();
		var complete = claim.isComplete();
		if (!complete && accepted != null) {
			super.state(context, false, "isAccepted", "acme.validation.claim.not-completed-claim");
			return false;
		}
		if (complete && accepted == null) {
			super.state(context, false, "isAccepted", "javax.validation.constraints.NotNull.message");
			return false;
		}

		result = !super.hasErrors(context);

		return result;
	}

}
