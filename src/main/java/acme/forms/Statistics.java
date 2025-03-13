
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Statistics extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private Integer				count;
	private Double				average;
	private Double				minimum;
	private Double				maximum;
	private Double				standardDeviation;

}
