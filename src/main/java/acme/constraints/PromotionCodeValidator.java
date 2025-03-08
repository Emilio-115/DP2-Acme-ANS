
package acme.constraints;

import java.time.Year;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, String> {

	@Override
	public void initialize(final ValidPromotionCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String promotionCode, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (promotionCode != null) {
			{
				boolean patternMatched = Pattern.matches("^[A-Z]{4}-[0-9]{2}$", promotionCode);

				super.state(context, patternMatched, "*", "acme.validation.promotion-code.mismatch.message");
			}
			{
				boolean lastTwoDigitsMatchYear;

				try {
					int length = promotionCode.length();
					String lastTwoDigitsString = promotionCode.substring(length - 2, length);
					int lastTwoDigits = Integer.parseInt(lastTwoDigitsString);

					int year = Year.now().getValue() % 100;

					lastTwoDigitsMatchYear = lastTwoDigits == year;

				} catch (Error e) {
					lastTwoDigitsMatchYear = false;
				}

				super.state(context, lastTwoDigitsMatchYear, "*", "acme.validation.promotion-code.last-two-digits-year.message");
			}
		}

		result = !super.hasErrors(context);

		return result;

	}

}
