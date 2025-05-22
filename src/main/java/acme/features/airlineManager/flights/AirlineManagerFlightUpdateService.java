/*
 * AirlineManagerLegUpdateService.java
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

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.airlineManager.AirlineManager;

@GuiService
public class AirlineManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("id", int.class);
		int airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flight = this.repository.findByIdAndManagerId(flightId, airlineManagerId).orElse(null);
		status = flight != null && flight.isDraftMode();

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
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset.put("origin", flight.origin());
		dataset.put("destination", flight.destination());
		dataset.put("departureDate", flight.scheduledDeparture());
		dataset.put("arrivalDate", flight.scheduledArrival());
		dataset.put("numberOfLayovers", flight.numberOfLayovers());
		super.getResponse().addData(dataset);
	}

}
