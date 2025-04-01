
package acme.features.airline_manager.legs;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.features.airline_manager.flights.AirlineManagerFlightRepository;
import acme.realms.airline_manager.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AirlineManagerLegRepository		repository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean authorized = true;

		Integer flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findFlightById(flightId).orElseThrow(() -> new RuntimeException("No flight with id: " + flightId));

		if (!flight.isDraftMode())
			authorized = false;

		Integer airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Optional<Flight> optionalFlight = this.flightRepository.findByIdAndManagerId(flightId, airlineManagerId);

		if (optionalFlight.isEmpty())
			authorized = false;

		super.getResponse().setAuthorised(authorized);
	}

	@Override
	public void load() {
		Leg leg;

		Integer flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findFlightById(flightId).orElseThrow(() -> new RuntimeException("No flight with id: " + flightId));

		leg = new Leg();
		leg.setFlight(flight);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {

		super.bindObject(leg, "departureDate", "arrivalDate", "flightNumberDigits", "status");

		int departureAirportId = super.getRequest().getData("departureAirport", int.class);
		Airport departureAirport = this.repository.findAirportById(departureAirportId).orElse(null);
		leg.setDepartureAirport(departureAirport);

		int arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		Airport arrivalAirport = this.repository.findAirportById(arrivalAirportId).orElse(null);
		leg.setArrivalAirport(arrivalAirport);

		int aircraftId = super.getRequest().getData("aircraft", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId).orElse(null);
		leg.setAircraft(aircraft);

		leg.setStatus(LegStatus.ON_TIME);

	}

	@Override
	public void validate(final Leg leg) {

		if (leg.getAircraft() != null) {
			boolean isAircraftActive = leg.getAircraft().getStatus().equals(AircraftStatus.ACTIVE);
			super.state(isAircraftActive, "aircraft", "acme.validation.flight.aircraft-under-maintenance.message");
		}
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;
		dataset = super.unbindObject(leg, "departureDate", "arrivalDate", "flightNumberDigits");
		dataset.put("flightNumber", null);
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

	/*
	 * flightNumberDigits;
	 * departureDate;
	 * arrivalDate;
	 * status;
	 * -------------------------
	 * arrivalAirport;
	 * departureAirport;
	 * aircraft;
	 * flight;
	 */

}
