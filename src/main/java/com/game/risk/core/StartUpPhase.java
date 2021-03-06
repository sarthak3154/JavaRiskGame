package com.game.risk.core;

import com.game.risk.core.util.LoggingUtil;
import com.game.risk.model.Country;
import com.game.risk.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to implement Startup Phase.
 *
 * @author Vida Abdollahi
 * @author sohrab_singh
 * @author Sarthak
 * @author shubangi_sheel
 */
public class StartUpPhase {

	/** Map file parser. */
	private MapFileReader mapFileReader;

	/** Player List. */
	private ArrayList<Player> playersList;

	/** Number of players. */
	private int numberOfPlayers;

	/** Minimum number of players. */
	private static final int MINIMUM_NUMBER_PLAYERS = 2;

	/** Maximum number of players. */
	private static final int MAXIMUM_NUMBER_PLAYERS = 6;

	/**
	 * Startup Phase Constructor.
	 *
	 * @param mapFileReader
	 *            reference to the map file parser object
	 * @param numberOfPlayers
	 *            number of players that we need to start the game
	 * @param reader
	 *            buffer reader
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public StartUpPhase(MapFileReader mapFileReader, int numberOfPlayers, BufferedReader reader) throws IOException {
		this.mapFileReader = mapFileReader;
		this.numberOfPlayers = numberOfPlayers;
		playersList = new ArrayList<Player>();

		System.out.println("Enter the Name of the Player(s)");
		for (int i = 0; i < numberOfPlayers; i++) {
			Player player = new Player();
			String playerName = null;

			if (reader != null && (playerName = reader.readLine()) != null) {
				player.setPlayerName(playerName);
			}
			playersList.add(player);
			if (player.getPlayerName() != null) {
				LoggingUtil.logMessage(player.getPlayerName() + " joined the game");
			}

		}
	}

	/**
	 * Instantiates a new start up phase.
	 *
	 * @param mapFileReader
	 *            the map file reader
	 * @param computerPlayers
	 *            the computer players
	 */
	public StartUpPhase(MapFileReader mapFileReader, List<String> computerPlayers) {
		this.mapFileReader = mapFileReader;
		this.numberOfPlayers = computerPlayers.size();
		playersList = new ArrayList<Player>();
		for (String name : computerPlayers) {
			Player player = new Player();
			player.setPlayerName(name);
			playersList.add(player);
			if (player.getPlayerName() != null) {
				LoggingUtil.logMessage(player.getPlayerName() + " joined the game");
			}
		}
	}

	/**
	 * Get the PlayerList.
	 *
	 * @return playerLists List of Players
	 */
	public ArrayList<Player> getPlayerList() {
		return playersList;
	}

	/**
	 * Set the player list.
	 * 
	 * @param playersList
	 *            Set the PlayerList
	 */
	public void setPlayersList(ArrayList<Player> playersList) {
		this.playersList = playersList;
	}

	/**
	 * Assign countries randomly to players.
	 */
	public void assignCountries() {

		int i = 0;
		for (String key : mapFileReader.getCountriesHashMap().keySet()) {
			i = i % playersList.size();
			Player player = playersList.get(i);
			player.addCountry(mapFileReader.getCountriesHashMap().get(key));
			mapFileReader.getCountriesHashMap().get(key).setPlayerName(player.getPlayerName());
			i++;
			LoggingUtil.logMessage(key + " country has been assigned to " + player.getPlayerName());
		}
	}

	/**
	 * Allocate initial armies to the players according to the players number.
	 * 
	 */
	public void allocateArmiesToPlayers() {

		for (Player player : playersList) {
			switch (numberOfPlayers) {

			case 2:
				player.setNumberOfArmies(40);
				break;
			case 3:
				player.setNumberOfArmies(35);
				break;
			case 4:
				player.setNumberOfArmies(30);
				break;
			case 5:
				player.setNumberOfArmies(25);
				break;
			case 6:
				player.setNumberOfArmies(20);
				break;
			default:
				System.out.println("Number of players is more than" + MAXIMUM_NUMBER_PLAYERS);
				LoggingUtil.logMessage("Number of players is more than " + MAXIMUM_NUMBER_PLAYERS);
				break;
			}
			if (player.getPlayerName() != null) {
				LoggingUtil.logMessage(player.getPlayerName() + " got " + player.getNumberOfArmies() + " armies");
			}
		}

	}

	/**
	 * Each country belonged to a player must have at least one army.
	 */
	public void assignInitialArmiesToCountries() {
		for (String key : mapFileReader.getCountriesHashMap().keySet()) {

			mapFileReader.getCountriesHashMap().get(key).setCurrentNumberOfArmies(1);
		}
		for (Player player : playersList) {
			int number = player.getNumberOfArmies();
			if (number - player.getCountriesOwned().size() < 0) {
				player.setNumberOfArmies(0);
			} else {
				player.setNumberOfArmies(number - player.getCountriesOwned().size());
			}
		}
		LoggingUtil.logMessage("Initial Armies has been assigned to countries by player");
	}

	/**
	 * Populate current domination percentage to players.
	 */
	public void populateDominationPercentage() {
		for (Player player : playersList) {
			int countriesOwned = player.getNumberOfCountriesOwned();
			player.setCurrentDominationPercentage(
					(double) countriesOwned / (mapFileReader.getCountriesHashMap().size()));
			LoggingUtil.logMessage("Current Domination Percentage of " + player.getPlayerName() + " is"
					+ player.getCurrentDominationPercentage() * 100);
		}
	}

	/**
	 * Get the Number of Players.
	 * 
	 * @return the numberOfPlayers
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * Set the Number of Players.
	 * 
	 * @param numberOfPlayers
	 *            the numberOfPlayers to set
	 */
	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}

	/**
	 * Allocate Remaining armies to countries.
	 *
	 * @throws NumberFormatException
	 *             the number format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void allocateRemainingArmiesToCountries() throws NumberFormatException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int i = 0;
		while (i < playersList.size()) {
			Player player = playersList.get(i);
			System.out.println(":: Player " + (i + 1) + " ::");
			for (Country country : player.getCountriesOwned()) {
				if (player.getNumberOfArmies() > 0) {
					System.out.println(
							"How many armies do you want to assign to your country " + country.getCountryName() + " ?");
					System.out.println("Current number of armies of " + country.getCountryName() + " is "
							+ country.getCurrentNumberOfArmies() + " | Available armies : "
							+ player.getNumberOfArmies());
					int armies = Integer.parseInt(reader.readLine());
					player.assignArmiesToCountries(country, armies);
				} else {
					break;
				}
			}
			i++;
		}
	}

	/**
	 * Get minimum no of players allowed to play the game.
	 * 
	 * @return minimum no of players defined
	 */
	public static int getMinimumNumberPlayers() {
		return MINIMUM_NUMBER_PLAYERS;
	}

}
