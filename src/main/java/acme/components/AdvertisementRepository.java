/*
 * AdvertisementRepository.java
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

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;
import acme.entities.advertisements.Advertisement;

@Repository
public interface AdvertisementRepository extends AbstractRepository {

	@Query("select count(a) from Advertisement a")
	int countAdvertisements();

	@Query("select a from Advertisement a")
	List<Advertisement> findAllAdvertisements(PageRequest pageRequest);

	default Advertisement findRandomAdvertisement() {
		Advertisement result;
		int count, index;
		PageRequest page;
		List<Advertisement> list;

		count = this.countAdvertisements();
		if (count == 0)
			result = null;
		else {
			index = RandomHelper.nextInt(0, count);

			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			list = this.findAllAdvertisements(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}

}
