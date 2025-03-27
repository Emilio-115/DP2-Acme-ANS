
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {

		int customerId;
		int bookingId;
		boolean status;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		bookingId = super.getRequest().getData("bookingId", int.class);

		status = this.repository.findBookingByIdAndCustomerId(bookingId, customerId).isPresent();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Passenger> passengers;
		int bookingId = super.getRequest().getData("bookingId", int.class);
		passengers = this.repository.findPassengersByBookingId(bookingId);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;
		dataset = super.unbindObject(passenger, "fullName", "passportNumber");
		super.getResponse().addData(dataset);

	}

}
