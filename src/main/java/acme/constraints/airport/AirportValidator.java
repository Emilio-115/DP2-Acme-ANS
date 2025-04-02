
package acme.constraints.airport;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.airlines.AirlineRepository;
import acme.entities.airports.Airport;
import acme.entities.airports.AirportRepository;

@Validator
public class AirportValidator extends AbstractValidator<ValidAirport, Airport> {

	@Override
	protected void initialise(final ValidAirport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (airport == null)
			return true;

		AirportRepository airportRepository = SpringHelper.getBean(AirportRepository.class);
		AirlineRepository airlineRepository = SpringHelper.getBean(AirlineRepository.class);

		String iataCode = airport.getIataCode();
		boolean isIataCodeTakenByAirports = airportRepository.isIataCodeTakenByAirports(iataCode, airport.getId());
		boolean isIataCodeTakenByAirlines = airlineRepository.isIataCodeTakenByAirlines(iataCode);
		boolean isIataCodeFree = !isIataCodeTakenByAirports && !isIataCodeTakenByAirlines;

		super.state(context, isIataCodeFree, "iataCode", "acme.validation.airport.non-unique-iata-code.message");

		result = !super.hasErrors(context);

		return result;
	}

}
