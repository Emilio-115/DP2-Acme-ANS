/*
 * AirlineManagerLegPublishService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airlineManager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.airlineManager.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		AirlineManager airlineManager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId).orElse(null);
		airlineManager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(airlineManager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int flightId;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId).get();

		super.getBuffer().addData(flight);
	}

	@Override
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "description", "cost", "requiresSelfTransfer");
		flight.setDraftMode(false);
	}

	@Override

	public void validate(final Flight flight) {

		boolean haveALeg = flight.numberOfLayovers() >= 0;
		super.state(haveALeg, "*", "acme.validation.flight.no-legs.message");

		List<Leg> legs = this.repository.findAllLegsByFlightId(flight.getId());
		for (Leg leg : legs)
			if (leg.getAircraft() != null) {
				boolean isAircraftActive = leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);
				super.state(isAircraftActive, "*", "acme.validation.flight.aircraft-under-maintenance.message");
				boolean isLegPublished = !leg.isDraftMode();
				super.state(isLegPublished, "*", "acme.validation.flight.leg-not-published.message");
				break;
			}
	}

	@Override
	public void perform(final Flight flight) {

		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");
		dataset.put("draftMode", true);
		dataset.put("origin", flight.origin());
		dataset.put("destination", flight.destination());
		dataset.put("departureDate", flight.scheduledDeparture());
		dataset.put("arrivalDate", flight.scheduledArrival());
		dataset.put("numberOfLayovers", flight.numberOfLayovers());
		super.getResponse().addData(dataset);
	}

}
