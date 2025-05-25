
package acme.entities.flightAssignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.flightAssignment.ValidFlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMember.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidFlightAssignment
@Table(indexes = {
	@Index(columnList = "id, draftMode, status, flight_crew_member_id"), @Index(columnList = "id, draftMode, status, duty, leg_id"), @Index(columnList = "id, draftMode, status, duty, flight_crew_member_id, leg_id"),
	@Index(columnList = "id, flight_crew_member_id"), @Index(columnList = "draftMode, flight_crew_member_id"), @Index(columnList = "id, draftMode, status, flight_crew_member_id, leg_id"), @Index(columnList = "draftMode, flight_crew_member_id"),
	@Index(columnList = "draftMode, status, flight_crew_member_id")
})
public class FlightAssignment extends AbstractEntity {

	/**
	 * A flight assignment represents the allocation of a flight crew member to a specific leg of a flight.
	 * Each assignment specifies:
	 * 
	 * the flight crew's duty in that leg ("PILOT", "CO-PILOT", "LEAD ATTENDANT", "CABIN ATTENDANT")
	 * the moment of the last update (in the past)
	 * the current status of the assignment ("CONFIRMED", "PENDING", or "CANCELLED")
	 * and some remarks (up to 255 characters), if necessary.
	 * 
	 */

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember		flightCrewMember;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg						leg;

	@Mandatory
	@Valid
	@Automapped
	private FlightCrewDuty			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					updatedAt;

	@Mandatory
	@Valid
	@Automapped
	private FlightAssignmentStatus	status;

	@Mandatory
	@Automapped
	private boolean					draftMode;

	@Optional
	@ValidString
	@Automapped
	private String					remarks;

}
