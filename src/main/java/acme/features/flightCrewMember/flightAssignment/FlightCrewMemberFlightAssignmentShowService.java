
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.entities.flightAssignment.FlightCrewDuty;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMember.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {

		int flightAssignmentId;
		boolean status;

		flightAssignmentId = super.getRequest().getData("id", int.class);

		Optional<FlightAssignment> flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		status = flightAssignment.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int flightAssignmentId = super.getRequest().getData("id", int.class);

		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId).get();

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {

		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "remarks", "duty", "status", "draftMode");

		{
			List<FlightCrewMember> availableMembers;

			if (!flightAssignment.isDraftMode())
				availableMembers = this.repository.findAllCrewMembers();
			else
				availableMembers = this.repository.findAvailableFlightCrewMembers();

			SelectChoices flightCrewMembers;
			flightCrewMembers = SelectChoices.from(availableMembers, "employeeCode", flightAssignment.getFlightCrewMember());

			dataset.put("flightCrewMember", flightCrewMembers.getSelected().getKey());
			dataset.put("flightCrewMembers", flightCrewMembers);
		}
		{
			List<Leg> pendingLegs;

			if (!flightAssignment.isDraftMode())
				pendingLegs = this.repository.findAllLegs();
			else
				pendingLegs = this.repository.findLegsDepartingAfter(MomentHelper.getCurrentMoment());

			SelectChoices legs;
			legs = SelectChoices.from(pendingLegs, "flightNumberDigits", flightAssignment.getLeg());

			dataset.put("leg", legs.getSelected().getKey());
			dataset.put("legs", legs);
		}
		{
			SelectChoices duties;
			duties = SelectChoices.from(FlightCrewDuty.class, flightAssignment.getDuty());

			dataset.put("duties", duties);
		}
		{
			SelectChoices statuses;
			statuses = SelectChoices.from(FlightAssignmentStatus.class, flightAssignment.getStatus());

			dataset.put("statuses", statuses);
		}

		super.getResponse().addData(dataset);

	}
}
