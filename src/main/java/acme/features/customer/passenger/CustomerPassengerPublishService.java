
package acme.features.customer.passenger;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status;
		Optional<Passenger> passenger = this.repository.findPassengerByIdAndCustomerId(passengerId, customerId);
		status = passenger.isPresent() && passenger.get().isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int passengerId = super.getRequest().getData("id", int.class);
		Passenger passenger = this.repository.findPassengerById(passengerId);
		super.getBuffer().addData(passenger);

	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger);
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);
	}
}
