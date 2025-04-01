/*
 * AuthenticatedAnnouncementRepository.java
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

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.announcements.Announcement;

@Repository
public interface AuthenticatedAnnouncementRepository extends AbstractRepository {

	@Query("select a from Announcement a where a.id = :id")
	Announcement findAnnouncementById(int id);

	@Query("select a from Announcement a where a.moment >= :deadline")
	Collection<Announcement> findAnnouncementsByMoment(Date deadline);

}
