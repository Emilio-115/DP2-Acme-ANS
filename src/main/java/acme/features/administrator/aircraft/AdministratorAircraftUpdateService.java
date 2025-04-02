
package acme.features.administrator.aircraft;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.aircrafts.AircraftStatus;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	protected AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		int aircraftId = super.getRequest().getData("id", int.class);
		Optional<Aircraft> aircraft = this.repository.findAircraftById(aircraftId);

		boolean status = aircraft.isPresent();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int aircraftId = super.getRequest().getData("id", int.class);
		Aircraft aircraft = this.repository.findAircraftById(aircraftId).get();

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");

		int airlineId = super.getRequest().getData("airline", int.class);
		Airline airline = this.repository.findAirlineById(airlineId).orElse(null);
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");

		SelectChoices aircraftStatusChoices;
		aircraftStatusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		dataset.put("aircraftStatusChoices", aircraftStatusChoices);

		Collection<Airline> airlines = this.repository.findAllAirlines();
		SelectChoices airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset.put("airlineChoices", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
