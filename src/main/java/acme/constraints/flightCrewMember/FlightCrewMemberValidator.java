
package acme.constraints.flightCrewMember;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.realms.flightCrewMember.FlightCrewMember;
import acme.realms.flightCrewMember.FlightCrewMemberRepository;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (flightCrewMember == null)
			return true;

		FlightCrewMemberRepository flightCrewMemberRepository = SpringHelper.getBean(FlightCrewMemberRepository.class);
		boolean isEmployeeCodeFree = flightCrewMemberRepository.isEmployeeCodeFree(flightCrewMember.getId(), flightCrewMember.getEmployeeCode());

		super.state(context, isEmployeeCodeFree, "employeeCode", "acme.validation.flight-crew-member.non-unique-employee-code");

		result = !super.hasErrors(context);

		return result;
	}

}
