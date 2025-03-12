
package acme.entities.activityLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.flightAssignment.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActivityLog extends AbstractEntity {

	/**
	 * An activity log records incidents that occur during a flight.
	 * They are logged by any of the flight crew members assigned to the corresponding leg and after the leg has taken place.
	 * The incidents include weather-related disruptions, route deviations, passenger issues, or mechanical failures, to mention a few.
	 * Each log entry includes:
	 * 
	 * a registration moment (in the past)
	 * a type of incident (up to 50 characters)
	 * a description (up to 255 characters)
	 * and a severity level (ranging from 0 to 10, where 0 indicates no issue and 10 represents a highly critical situation)
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	registeringAssignment;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registeredAt;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				incidentType;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private int					severityLevel;

}
