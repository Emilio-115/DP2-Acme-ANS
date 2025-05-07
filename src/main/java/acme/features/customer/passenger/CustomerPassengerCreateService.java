
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status = true;

		if (super.getRequest().hasData("id")) {
			int id = super.getRequest().getData("id", int.class);
			if (id != 0)
				status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		Passenger passenger = new Passenger();
		passenger.setDraftMode(true);
		passenger.setCustomer(customer);
		super.getBuffer().addData(passenger);

	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "birthDate", "specialNeeds");
		super.getResponse().addData(dataset);
	}
}
