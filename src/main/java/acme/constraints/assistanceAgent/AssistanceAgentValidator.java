
package acme.constraints.assistanceAgent;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.realms.assistanceAgent.AssistanceAgent;
import acme.realms.assistanceAgent.AssistanceAgentRepository;

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

		if (assistanceAgent == null)
			return true;

		var code = assistanceAgent.getEmployeeCode();
		if (code == null) {
			super.state(context, false, "employeeCode", "javax.validation.constraints.NotNull.message");
			return false;
		}

		AssistanceAgentRepository assistanceAgentRepository = SpringHelper.getBean(AssistanceAgentRepository.class);
		boolean isEmployeeCodeFree = assistanceAgentRepository.isEmployeeCodeFree(assistanceAgent.getId(), code);

		super.state(context, isEmployeeCodeFree, "employeeCode", "acme.validation.flight-crew-member.non-unique-employee-code");

		result = !super.hasErrors(context);

		return result;
	}

}
