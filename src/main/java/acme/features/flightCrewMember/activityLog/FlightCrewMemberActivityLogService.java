
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

public class FlightCrewMemberActivityLogService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	protected FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int activityLogId = super.getRequest().getData("id", int.class);
		Optional<ActivityLog> activityLog = this.repository.findByIdAndFlightCrewMemberId(activityLogId, flightCrewMember.getId());

		boolean status = activityLog.map(fa -> fa.isDraftMode()).orElse(false);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int activityLogId = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findByIdAndFlightCrewMemberId(activityLogId, flightCrewMember.getId()).get();

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		int registeringAssignmentId = super.getRequest().getData("registeringAssignment", int.class);
		FlightAssignment registeringAssignment = this.repository.findPublishedFlightAssignmentByIdAndFlightCrewMemberId(registeringAssignmentId, flightCrewMember.getId()).get();

		super.bindObject(activityLog, "incidentType", "description", "severityLevel");
		activityLog.setRegisteringAssignment(registeringAssignment);
		activityLog.setRegisteredAt(MomentHelper.getCurrentMoment());
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "incidentType", "description", "severityLevel", "draftMode");

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		{
			List<FlightAssignment> validAssignments;

			if (!activityLog.isDraftMode())
				validAssignments = this.repository.findPublishedFlightAssignmentsByFlightCrewMemberId(flightCrewMemberId);
			else
				validAssignments = this.repository.findPublishedFlightAssignmentsByFlightCrewMemberIdLandedBefore(flightCrewMemberId, MomentHelper.getCurrentMoment());

			SelectChoices registeringAssignments;
			registeringAssignments = SelectChoicesHelper.from(validAssignments, fa -> fa.getLeg().flightNumber(), activityLog.getRegisteringAssignment());

			dataset.put("registeringAssignment", registeringAssignments.getSelected().getKey());
			dataset.put("registeringAssignments", registeringAssignments);
		}

		super.getResponse().addData(dataset);
	}
}
