
package acme.constraints.airline;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineRepository;

@Validator
public class AirlineValidator extends AbstractValidator<ValidAirline, Airline> {

	@Override
	protected void initialise(final ValidAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (airline == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);

			String iataCode = airline.getIataCode();
			boolean isIataCodeTakenByAirlines = airlineRepository.isIataCodeTakenByAirlines(iataCode, airline.getId());
			boolean isIataCodeFree = !isIataCodeTakenByAirlines;

			super.state(context, isIataCodeFree, "iataCode", "acme.validation.airport.non-unique-iata-code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
