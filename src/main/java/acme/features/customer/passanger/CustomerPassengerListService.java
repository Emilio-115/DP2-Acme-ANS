
package acme.features.customer.passanger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

}
