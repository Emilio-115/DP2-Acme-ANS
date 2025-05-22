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

package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.airlineManager.AirlineManager;

@GuiService
public class AirlineManagerLegDelayService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repository;


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

			status &= leg.getStatus().equals(LegStatus.ON_TIME);

			status &= !leg.isDraftMode();
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int legId;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId).orElse(null);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {
		leg.setStatus(LegStatus.DELAYED);
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		dataset = super.unbindObject(leg, "departureDate", "arrivalDate", "flightNumberDigits");
		dataset.put("flightNumber", leg.flightNumber());
		dataset.put("draftMode", leg.isDraftMode());
		dataset.put("flightDraftMode", leg.getFlight().isDraftMode());

		Collection<Airport> airports = this.repository.findAllAirports();

		SelectChoices airportDepartureChoices = SelectChoices.from(airports, "name", leg.getDepartureAirport());
		SelectChoices airportArrivalChoices = SelectChoices.from(airports, "name", leg.getArrivalAirport());

		dataset.put("departureAirportChoices", airportDepartureChoices);
		dataset.put("arrivalAirportChoices", airportArrivalChoices);

		dataset.put("departureAirport", airportDepartureChoices.getSelected().getKey());
		dataset.put("arrivalAirport", airportArrivalChoices.getSelected().getKey());

		Collection<Aircraft> availableAircrafts = this.repository.findAllActiveAircrafts();
		if (!availableAircrafts.contains(leg.getAircraft()) && leg.getAircraft() != null)
			availableAircrafts.add(leg.getAircraft());

		SelectChoices aircraftChoices = SelectChoices.from(availableAircrafts, "registrationNumber", leg.getAircraft());
		dataset.put("aircraftChoices", aircraftChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());

		SelectChoices statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());
		dataset.put("statusChoices", statusChoices);
		dataset.put("status", statusChoices.getSelected().getKey());

		dataset.put("flight", leg.getFlight());
		dataset.put("flightId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}

}
