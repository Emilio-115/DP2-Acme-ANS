
package acme.features.flightCrewMember.flightAssignment;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "duty", "status");
		dataset.put("employeeCode", flightAssignment.getFlightCrewMember().getEmployeeCode());
		dataset.put("flightNumber", flightAssignment.getLeg().flightNumber());
		super.addPayload(dataset, flightAssignment, "remarks", "leg.departureAirport.name", "leg.departureAirport.iataCode", "leg.departureAirport.city", "leg.departureAirport.country", "leg.arrivalAirport.name", "leg.arrivalAirport.iataCode",
			"leg.arrivalAirport.city", "leg.arrivalAirport.country");
		super.getResponse().addData(dataset);

	}

}
