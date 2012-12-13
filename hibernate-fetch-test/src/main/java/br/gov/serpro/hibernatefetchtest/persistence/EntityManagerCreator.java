package br.gov.serpro.hibernatefetchtest.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerCreator {
	
	private static EntityManagerFactory factory;
	
	public static EntityManager createEntityManager(){
		if (factory==null){
			factory = Persistence.createEntityManagerFactory("hibernate-test");
		}
		
		return factory.createEntityManager();
	}

}
