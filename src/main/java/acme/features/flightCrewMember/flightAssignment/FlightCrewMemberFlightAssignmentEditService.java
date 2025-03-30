
package acme.features.flightCrewMember.flightAssignment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.flightAssignment.FlightAssignmentStatus;
import acme.entities.flightAssignment.FlightCrewDuty;
import acme.entities.legs.Leg;
import acme.helpers.SelectChoicesHelper;
import acme.realms.flightCrewMember.FlightCrewMember;

public class FlightCrewMemberFlightAssignmentEditService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		Optional<FlightAssignment> flightAssignment;

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findByIdAndFlightCrewMemberId(flightAssignmentId, flightCrewMember.getId());
		status = flightAssignment.map(fa -> fa.isDraftMode()).orElse(false);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightAssignmentId;
		FlightAssignment flightAssignment;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId).get();

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {

		int legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId).orElse(null);

		super.bindObject(flightAssignment, "status", "duty", "remarks");
		flightAssignment.setLeg(leg);
		flightAssignment.setUpdatedAt(MomentHelper.getCurrentMoment());
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "remarks", "duty", "status", "draftMode");

		{
			List<Leg> pendingLegs;

			if (!flightAssignment.isDraftMode())
				pendingLegs = this.repository.findAllLegs();
			else {
				FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
				pendingLegs = this.repository.findLegsDepartingAfterWhereFlightCrewMemberIsFree(MomentHelper.getCurrentMoment(), flightAssignment.getId(), flightCrewMember.getId());
			}

			SelectChoices legs;
			legs = SelectChoicesHelper.from(pendingLegs, Leg::flightNumber, flightAssignment.getLeg());

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
