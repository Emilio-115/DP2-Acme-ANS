
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.realms.flightCrewMember.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	/**
	 * The system must handle flight crew member dashboards with the following indicators:
	 * 
	 * The last five destinations to which they have been assigned.
	 * The number of legs that have an activity log record with an incident severity ranging from 0 up to 3, 4 up to 7, and 8 up to 10.
	 * The crew members who were assigned with him or her in their last leg.
	 * Their flight assignments grouped by their statuses.
	 * The average, minimum, maximum, and standard deviation of the number of flight assignments they had in the last month.
	 * 
	 */

	private static final long							serialVersionUID	= 1L;

	List<String>										lastFiveDestinations;

	int													legsWithLowSeverityIncidents;
	int													legsWithMediumSeverityIncidents;
	int													legsWithHighSeverityIncidents;

	List<FlightCrewMember>								crewmatesForLastLeg;

	Map<FlightAssignmentStatus, List<FlightAssignment>>	numberBookingsByTravelClass;

	Statistics											lastMonthsFlightAssignmentStatistics;

}
