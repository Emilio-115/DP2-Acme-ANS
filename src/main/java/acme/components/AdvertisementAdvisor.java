/*
 * AdvertisementAdvisor.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.advertisements.Advertisement;

@ControllerAdvice
public class AdvertisementAdvisor {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdvertisementRepository repository;

	// Beans ------------------------------------------------------------------


	@ModelAttribute("advertisement")
	public Advertisement getAdvertisement() {
		Advertisement result;

		try {
			result = this.repository.findRandomAdvertisement();
		} catch (final Throwable oops) {
			result = null;
		}

		return result;
	}

}
