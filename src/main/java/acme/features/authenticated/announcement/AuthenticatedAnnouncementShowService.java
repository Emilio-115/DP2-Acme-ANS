/*
 * AuthenticatedAnnouncementShowService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.features.authenticated.announcement;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.announcements.Announcement;

@GuiService
public class AuthenticatedAnnouncementShowService extends AbstractGuiService<Authenticated, Announcement> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedAnnouncementRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Announcement announcement;
		Date deadline;

		id = super.getRequest().getData("id", int.class);
		announcement = this.repository.findAnnouncementById(id);
		deadline = MomentHelper.deltaFromCurrentMoment(-30, ChronoUnit.DAYS);
		status = MomentHelper.isAfter(announcement.getMoment(), deadline);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Announcement announcement;
		int id;

		id = super.getRequest().getData("id", int.class);
		announcement = this.repository.findAnnouncementById(id);

		super.getBuffer().addData(announcement);
	}

	@Override
	public void unbind(final Announcement announcement) {
		Dataset dataset;

		dataset = super.unbindObject(announcement, "title", "moment", "status", "text", "moreInfo");

		super.getResponse().addData(dataset);
	}

}
