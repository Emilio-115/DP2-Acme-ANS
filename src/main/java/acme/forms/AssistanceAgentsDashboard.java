
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentsDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Double						ratioAceptedClaims;

	Double						rejectedClaims;

	List<String>				top3Months;

	Statistics					numberOfClaims;

	Statistics					numberOfClaimsInThatMonth;
}
