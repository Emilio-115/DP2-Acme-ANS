
package acme.features.flightCrewMember.activityLog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.helpers.SelectChoicesHelper;
import acme.realms.flightCrewMember.FlightCrewMember;

public class FlightCrewMemberActivityLogEditService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected FlightCrewMemberActivityLogRepository repository;


	protected Optional<FlightAssignment> getRegisteringAssignmentFromRequest() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Integer registeringAssignmentId = super.getRequest().getData("registeringAssignment", Integer.class, null);
		return this.repository.findPublishedAndConfirmedFlightAssignmentByIdAndFlightCrewMemberId(registeringAssignmentId, flightCrewMember.getId());
	}

	protected boolean isRegisteringAssignmentAllowedIfPresent() {
		Integer registeringAssignmentId = super.getRequest().getData("registeringAssignment", Integer.class, null);

		if (registeringAssignmentId == null || registeringAssignmentId.equals(0))
			return true;

		return this.getRegisteringAssignmentFromRequest().isPresent();
	}

	@Override
	public void authorise() {
		boolean status = true;

		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int activityLogId = super.getRequest().getData("id", int.class);
		Optional<ActivityLog> activityLog = this.repository.findByIdAndFlightCrewMemberId(activityLogId, flightCrewMember.getId());

		if (activityLog.map(al -> !al.isDraftMode()).orElse(true))
			status = false;

		if (!this.isRegisteringAssignmentAllowedIfPresent())
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findByIdAndFlightCrewMemberId(activityLogId, flightCrewMember.getId()).orElse(null);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		FlightAssignment registeringAssignment = this.getRegisteringAssignmentFromRequest().orElse(null);

		super.bindObject(activityLog, "incidentType", "description", "severityLevel");
		activityLog.setRegisteringAssignment(registeringAssignment);
		activityLog.setRegisteredAt(MomentHelper.getCurrentMoment());
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registeredAt", "incidentType", "description", "severityLevel", "draftMode");

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		{
			List<FlightAssignment> validAssignments;

			if (!activityLog.isDraftMode())
				validAssignments = this.repository.findPublishedFlightAssignmentsByFlightCrewMemberId(flightCrewMemberId);
			else
				validAssignments = this.repository.findPublishedAndConfirmedFlightAssignmentsByFlightCrewMemberIdLandedBefore(flightCrewMemberId, MomentHelper.getCurrentMoment());

			SelectChoices registeringAssignments;
			registeringAssignments = SelectChoicesHelper.from(validAssignments, fa -> fa.getLeg().flightNumber(), activityLog.getRegisteringAssignment());

			dataset.put("registeringAssignment", registeringAssignments.getSelected().getKey());
			dataset.put("registeringAssignments", registeringAssignments);
		}

		super.getResponse().addData(dataset);
	}
}
