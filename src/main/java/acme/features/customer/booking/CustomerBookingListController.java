
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.Booking;
import acme.realms.Customer;

@GuiController
public class CustomerBookingListController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private CustomerBookingListService listService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("list", this.listService);

	}

}
