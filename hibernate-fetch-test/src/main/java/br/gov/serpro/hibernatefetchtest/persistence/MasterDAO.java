package br.gov.serpro.hibernatefetchtest.persistence;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.gov.serpro.hibernatefetchtest.domain.Detail;
import br.gov.serpro.hibernatefetchtest.domain.Master;

public class MasterDAO {

	private EntityManager entityManager;
	
	public MasterDAO(EntityManager manager){
		this.entityManager = manager;
	}

	/**
	 * Creates some data to test the bug. We will have 3 masters, each with
	 * three details.
	 */
	public void initializeTestDatabase() {
		entityManager.getTransaction().begin();
		for (int i = 1; i <= 3; i++) {
			Master master = new Master();
			master.setId((long) i);
			master.setDescription("Master" + i);

			List<Detail> details = new ArrayList<Detail>();
			for (int j = 1; j <= 3; j++) {
				Detail detail = new Detail();
				detail.setId((long) (i*10)+j);
				detail.setDescription("Detail-" + i + "-" +j);

				details.add(detail);
			}

			master.setDetails(details);

			// We have cascade=ALL, so here we are also persisting details
			entityManager.persist(master);
		}
		entityManager.getTransaction().commit();
	}
	
	/**
	 * Select all instances of {@link Master} that have a detail whose 
	 * description matches the param. Uses JOIN FETCH to eagerly fetch
	 * each master's details.
	 */
	public List<Master> selectMasterFilteringDetail(String detailDescription) {
		StringBuffer jpql = new StringBuffer()
				.append("SELECT master FROM Master master ")
				.append("JOIN FETCH master.details details ")
				.append("WHERE details.description=:description ");

		TypedQuery<Master> query = entityManager.createQuery(jpql.toString(),
				Master.class);
		query.setParameter("description", detailDescription);

		return query.getResultList();
	}

	/**
	 * Select one instance of {@link Master} by ID, but uses JOIN FETCH on the
	 * master's collection of details to ensure it is eager fetched during
	 * select.
	 * 
	 */
	public Master selectMasterByIdFetchingDetails(Long id) {
		StringBuffer jpql = new StringBuffer()
				.append("SELECT master FROM Master master ")
				.append("JOIN FETCH master.details ")
				.append("WHERE master.id=:id");

		TypedQuery<Master> query = entityManager.createQuery(jpql.toString(),
				Master.class);
		query.setParameter("id", id);

		return query.getSingleResult();
	}

}
