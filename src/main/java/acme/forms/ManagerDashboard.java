
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.airports.Airport;
import acme.entities.legs.LegStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	int							ranking;
	int							yearsToRetire;
	float						onTimeRatio;
	float						delayedRatio;
	Airport						mostPopularAirport;
	Airport						leastPopularAirport;
	Map<LegStatus, Integer>		numberOfLegsByStatus;
	Statistics					flightCostsStatistics;
}
