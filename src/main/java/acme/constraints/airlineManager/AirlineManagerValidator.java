
package acme.constraints.airlineManager;

import java.util.List;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.realms.airlineManager.AirlineManager;
import acme.realms.airlineManager.AirlineManagerRepository;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (airlineManager == null)
			return true;

		AirlineManagerRepository managerRepository = SpringHelper.getBean(AirlineManagerRepository.class);
		List<AirlineManager> managers = managerRepository.findAllAirlineManagersWithIndentifier(airlineManager.getId(), airlineManager.getIdentifier());
		boolean isIndentifierFree = managers.isEmpty();

		super.state(context, isIndentifierFree, "identifier", "acme.validation.airline-manager.non-unique-identifier");

		result = !super.hasErrors(context);

		return result;
	}

}
