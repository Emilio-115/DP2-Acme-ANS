
package acme.features.airlineManager.legs;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.features.airlineManager.flights.AirlineManagerFlightRepository;
import acme.realms.airlineManager.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository		repository;

	@Autowired
	private AirlineManagerFlightRepository	flightRepository;


	@Override
	public void authorise() {

		boolean authorized = true;

		Integer flightId = super.getRequest().getData("flightId", int.class);

		Integer airlineManagerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Optional<Flight> optionalFlight = this.flightRepository.findByIdAndManagerId(flightId, airlineManagerId);

		if (optionalFlight.isEmpty())
			authorized = false;

		if (optionalFlight.isPresent()) {
			Flight flight = optionalFlight.get();
			if (!flight.isDraftMode())
				authorized = false;
		}

		if (super.getRequest().hasData("id", boolean.class)) {
			int aircraftId = super.getRequest().getData("id", int.class);
			authorized &= aircraftId == 0;
		}

		if (super.getRequest().hasData("aircraft", int.class) && super.getRequest().getData("aircraft", int.class) != 0) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			Aircraft aircraft = this.repository.findAircraftById(aircraftId).orElse(null);
			Collection<Aircraft> availableAircrafts = this.repository.findAllActiveAircrafts();

			authorized &= availableAircrafts.contains(aircraft);
		}

		if (super.getRequest().hasData("arrivalAirport", int.class) && super.getRequest().getData("arrivalAirport", int.class) != 0) {
			int airportId = super.getRequest().getData("arrivalAirport", int.class);
			Optional<Airport> airport = this.repository.findAirportById(airportId);

			authorized &= airport.isPresent();
		}

		if (super.getRequest().hasData("departureAirport", int.class) && super.getRequest().getData("departureAirport", int.class) != 0) {
			int airportId = super.getRequest().getData("departureAirport", int.class);
			Optional<Airport> airport = this.repository.findAirportById(airportId);

			authorized &= airport.isPresent();
		}

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

		if (leg.getArrivalDate() != null && leg.getDepartureDate() != null) {
			Date currentDate = MomentHelper.getCurrentMoment();
			super.state(currentDate.before(leg.getDepartureDate()), "departureDate", "acme.validation.leg.past-departure-date.message");
			super.state(currentDate.before(leg.getArrivalDate()), "arrivalDate", "acme.validation.leg.past-arrival-date.message");
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
		Aircraft aircraft = leg.getAircraft();
		if (!availableAircrafts.contains(leg.getAircraft()))
			aircraft = null;

		SelectChoices aircraftChoices = SelectChoices.from(availableAircrafts, "registrationNumber", aircraft);
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
