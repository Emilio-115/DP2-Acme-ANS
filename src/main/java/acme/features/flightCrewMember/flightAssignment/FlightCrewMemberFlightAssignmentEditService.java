
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
import acme.realms.flightCrewMember.FlightCrewMemberAvailabilityStatus;

public class FlightCrewMemberFlightAssignmentEditService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	protected FlightCrewMemberFlightAssignmentRepository repository;


	protected boolean checkLeg() {
		return false;
	}

	protected boolean isLegValidForUpdatingIfPresent() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Integer legId = super.getRequest().getData("leg", Integer.class, null);
		Integer flightAssignmentId = super.getRequest().getData("id", Integer.class, null);

		if (legId == null || legId == 0)
			return true;

		Optional<Leg> leg = this.repository.findLegDepartingAfterWhereFlightCrewMemberIsFreeById(legId, MomentHelper.getCurrentMoment(), flightAssignmentId, flightCrewMember.getId());
		return leg.isPresent();
	}

	protected boolean flightAssignmentIsAuthorised(final FlightAssignment flightAssignment) {
		return flightAssignment.isDraftMode();
	}

	protected boolean idIsAuthorised() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Integer flightAssignmentId = super.getRequest().getData("id", Integer.class, null);
		Optional<FlightAssignment> flightAssignment = this.repository.findByIdAndFlightCrewMemberId(flightAssignmentId, flightCrewMember.getId());
		return flightAssignment.map(this::flightAssignmentIsAuthorised).orElse(false);
	}

	@Override
	public void authorise() {
		boolean status = true;

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		if (!flightCrewMember.getAvailabilityStatus().equals(FlightCrewMemberAvailabilityStatus.AVAILABLE))
			status = false;

		boolean idIsValid = this.idIsAuthorised();
		if (!idIsValid)
			status = false;

		if (this.checkLeg())
			if (!this.isLegValidForUpdatingIfPresent())
				status = false;

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
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "updatedAt", "remarks", "duty", "status", "draftMode");

		{
			List<Leg> pendingLegs;

			if (!flightAssignment.isDraftMode())
				pendingLegs = this.repository.findAllLegs();
			else
				pendingLegs = this.repository.findLegsDepartingAfterWhereFlightCrewMemberIsFree(MomentHelper.getCurrentMoment(), flightAssignment.getId(), flightCrewMember.getId());

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
		{
			boolean showActivityLogs = !flightAssignment.isDraftMode() && flightAssignment.getLeg().getDepartureDate().before(MomentHelper.getCurrentMoment());
			if (showActivityLogs)
				dataset.put("showActivityLogs", true);
		}

		super.getResponse().addData(dataset);
	}
}
