
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;

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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();

		flightAssignment.setDraftMode(true);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		int flightCrewMemberId = super.getRequest().getData("flightCrewMember", int.class);
		FlightCrewMember flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId).get();

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId).get();

		super.bindObject(flightAssignment, "status", "duty", "remarks");
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLeg(leg);
		flightAssignment.setUpdatedAt(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "remarks", "duty", "status", "draftMode");

		{
			List<FlightCrewMember> availableMembers = this.repository.findAvailableFlightCrewMembers();

			SelectChoices flightCrewMembers;
			flightCrewMembers = SelectChoices.from(availableMembers, "employeeCode", flightAssignment.getFlightCrewMember());

			dataset.put("flightCrewMember", flightCrewMembers.getSelected().getKey());
			dataset.put("flightCrewMembers", flightCrewMembers);
		}
		{
			List<Leg> pendingLegs = this.repository.findLegsDepartingAfter(MomentHelper.getCurrentMoment());

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
