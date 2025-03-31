/*
 * AirlineManagerLegDeleteService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.airline_manager.legs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegDeleteService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int managerId;
		int legId;
		boolean status = true;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		legId = super.getRequest().getData("id", int.class);

		Optional<Leg> optionalLeg = this.repository.findLegById(legId);

		status &= optionalLeg.isPresent();

		if (optionalLeg.isPresent()) {
			Leg leg = optionalLeg.get();
			Optional<Flight> optionalFlight = this.repository.findByIdAndManagerId(leg.getFlight().getId(), managerId);

			status &= optionalFlight.isPresent();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int legId;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId).get();

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.delete(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		dataset = super.unbindObject(leg, "departureDate", "arrivalDate", "status");
		dataset.put("departureAirport", leg.getDepartureAirport());
		dataset.put("arrivalAirport", leg.getArrivalAirport());
		dataset.put("aircraft", leg.getAircraft());
		dataset.put("flight", leg.getFlight());
		dataset.put("flightNumber", leg.flightNumber());
		dataset.put("draftMode", leg.getFlight().isDraftMode());
		super.getResponse().addData(dataset);
	}

}
