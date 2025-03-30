
package acme.features.customer.passenger;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		int passengerId = super.getRequest().getData("id", int.class);
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		boolean status;
		Optional<Passenger> passenger = this.repository.findPassengerByIdAndCustomerId(passengerId, customerId);
		status = passenger.isPresent();
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

	}

}
