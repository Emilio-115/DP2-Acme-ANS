
package acme.constraints.assistanceAgent;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		var code = assistanceAgent.getEmployeeCode();
		if (code == null) {
			super.state(context, false, "employeeCode", "javax.validation.constraints.NotNull.message");
			return false;
		} else {

		}

		return false;
	}

}
