
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.bookings.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomersDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<String>				lastFiveDestination;

	Money						moneySpentInBookingsLastYear;

	Map<TravelClass, Integer>	numberBookingsByTravelClass;

	Statistics					bookingCostStatistics;

	Statistics					bookingPassengerStatistics;

}
