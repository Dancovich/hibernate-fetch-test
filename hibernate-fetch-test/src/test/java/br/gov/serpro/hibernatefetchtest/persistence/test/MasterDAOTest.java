package br.gov.serpro.hibernatefetchtest.persistence.test;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.gov.serpro.hibernatefetchtest.domain.Master;
import br.gov.serpro.hibernatefetchtest.persistence.EntityManagerCreator;
import br.gov.serpro.hibernatefetchtest.persistence.MasterDAO;

public class MasterDAOTest {
	
	@BeforeClass
	public static void setUp(){
		MasterDAO masterDAO = new MasterDAO(EntityManagerCreator.createEntityManager());
		
		//This method will create 3 masters, each with 3 details
		masterDAO.initializeTestDatabase();
	}
	
	@Test
	public void testFetchJoin(){
		
		EntityManager manager = EntityManagerCreator.createEntityManager();
		MasterDAO masterDAO = new MasterDAO(manager);
		
		/*
		 * First we select a master that have a detail with description = "Detail-1-1", there should
		 * be only one master that matches that.
		 */
		List<Master> masters = masterDAO.selectMasterFilteringDetail("Detail-1-1");
		Assert.assertNotNull(masters);
		Assert.assertEquals(1, masters.size());
		
		/*
		 * Strange behaviour, should the master have all details here and the filter only be used to
		 * limit instances of master? Or should the master have only the detail we filtered? Current
		 * version of Hibernate only brings the detail we filtered.
		 */
		Master master = masters.get(0);
		Assert.assertNotNull(master.getDetails());
		//Assert.assertEquals(3, master.getDetails().size()); /* <---- Maybe we should expect that */
		Assert.assertEquals(1, master.getDetails().size()); /* <---- But that is what is actually happening */
		
		/*
		 * Now we select the master by ID
		 */
		Master selectedMaster = masterDAO.selectMasterByIdFetchingDetails(master.getId());
		
		/*
		 * It must have all details despite the previous filter, but it is not what's happening
		 */
		Assert.assertNotNull(selectedMaster);
		Assert.assertNotNull(selectedMaster.getDetails());
		//Assert.assertEquals(3, selectedMaster.getDetails().size()); /* We DEFINITELY should expect that */
		Assert.assertEquals(1, selectedMaster.getDetails().size()); /* But here we have a bug, this is what we get */
		
		/*
		 * Now if we clear the persistence context, detach all entities and redo the query, the correct behaviour happens.
		 */
		manager.clear();
		selectedMaster = masterDAO.selectMasterByIdFetchingDetails(master.getId());
		Assert.assertNotNull(selectedMaster);
		Assert.assertNotNull(selectedMaster.getDetails());
		Assert.assertEquals(3, selectedMaster.getDetails().size()); /* NOW it works */
	}

}
