package com.game.risk.core;

import com.game.risk.core.util.LoggingUtil;
import com.game.risk.model.Country;
import com.game.risk.model.Player;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * Test Class for StartUp Phase.
 *
 * @author sohrab_singh
 * @author Vida Abdollahi
 * @author Sarthak
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggingUtil.class, StartUpPhase.class })
public class StartUpPhaseTest {

	/** MapFileReader object. */
	private MapFileReader mapFileReader;

	/** StartUpPhase object. */
	private StartUpPhase startUpPhase;

	/** Number of Players playing the game. */
	private int numberOfPlayers;

	/** Player 1 to create mock object. */
	private Player p1;

	/** Player 2 to create mock object. */
	private Player p2;

	/** ArrayList to hold Player objects. */
	private ArrayList<Player> list1;

	/** Country object to create mock object. */
	private Country c1;

	/** The c 2. */
	private Country c2;

	/** The list 2. */
	private ArrayList<Country> list2;

	/**
	 * Setup the initial objects.
	 *
	 * @throws Exception
	 *             the exception
	 */

	@Before
	public void setUp() throws Exception {
		mapFileReader = new MapFileReader(new File(
				"//Users//sohrab_singh//Documents//workspace-sts-3.9.0.RELEASE//RiskGame//src//test//resources//Canada.map"));
		startUpPhase = new StartUpPhase(mapFileReader, 2, null);
		numberOfPlayers = 2;
		p1 = new Player();
		p1.setPlayerName("Vida");
		p2 = new Player();
		p2.setPlayerName("Shubhangi");
		list1 = new ArrayList<Player>();
		list1.add(p1);
		list1.add(p2);
		startUpPhase.setPlayersList(list1);
		list2 = new ArrayList<>();
		list2.add(c1);
		list2.add(c2);
		p1.setCountriesOwned(list2);
		PowerMockito.mockStatic(LoggingUtil.class);
		PowerMockito.doNothing().when(LoggingUtil.class);
	}

	/**
	 * Test method for getting player list.
	 *
	 * @return the player list
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getPlayerList() throws Exception {

		assertEquals(list1.size(), startUpPhase.getPlayerList().size());

	}

	/**
	 * Test method for assigning initial armies to each country.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void assignInitialArmiesToCountries() throws Exception {
		startUpPhase.assignInitialArmiesToCountries();
		assertEquals(0, p1.getNumberOfArmies());

	}

	/**
	 * Test method for allocating armies to players.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void allocateArmiesToPlayers() throws Exception {
		startUpPhase.allocateArmiesToPlayers();
		assertNotEquals(45, p1.getNumberOfArmies());
	}

	/**
	 * Test method for Domination Percentage.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void DominationPercentage() throws Exception {
		startUpPhase.populateDominationPercentage();
		assertNotEquals(10, p1.getCurrentDominationPercentage());
	}

	/**
	 * Test method for getting number of players.
	 *
	 * @return the count of players
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void getCountOfPlayers() throws Exception {

		startUpPhase.setNumberOfPlayers(2);
		assertEquals(2, startUpPhase.getNumberOfPlayers());
	}

	/**
	 * Get the Country object.
	 *
	 * @return the c1
	 */
	public Country getC1() {
		return c1;
	}

	/**
	 * Set the Country object.
	 *
	 * @param c1
	 *            Country object
	 */
	public void setC1(Country c1) {
		this.c1 = c1;
	}

	/**
	 * Set the number of players playing the game.
	 *
	 * @param numberOfPlayers
	 *            num of players
	 */
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	/**
	 * Get the number of Players.
	 *
	 * @return an int variable indicating number of players
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

}
