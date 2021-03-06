package com.game.risk.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.game.risk.RiskGameDriver;
import com.game.risk.core.MapEditor;
import com.game.risk.core.MapFileReader;
import com.game.risk.core.util.LoggingUtil;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * View for the user to choose from loading a map file or creating a new map.
 *
 * @author Sarthak
 * @author sohrab_singh
 */
public class WelcomeScreenView extends JFrame implements MouseListener {

	/** Serial Version UID. */
	private static final long serialVersionUID = -5774732089402932902L;

	/** Map File Parser. */
	private MapFileReader parser;

	/**
	 * Map Editor View to create a new Map or make changes to the existing map file.
	 */
	private MapEditor view;

	/** JPanel object to. */
	private JPanel contentPane;

	/**
	 * JButton object to load the existing Map file via JFileChooser, store the data
	 * and edit the map.
	 */
	private JButton btnLoad;

	/**
	 * JButton object to create a new Map calling the MapEditorView on mouse clicked
	 * action.
	 */
	private JButton btnNewMap;

	/** JButton object to load a previously saved game. */
	private JButton btnLoadSaved;

	/** JButton object to start tournament mode game. */
	private JButton btnStartTournamentMode;

	/** Variable to read from the file. */
	private FileInputStream input;

	/** JFileChooser object to select a file. */
	private JFileChooser fileChooser;

	/**
	 * Create the frame.
	 */
	public WelcomeScreenView() {
		initializeView();
		// Attach Mouse Listeners to the JButton objects
		btnLoad.addMouseListener(this);
		btnNewMap.addMouseListener(this);
		btnLoadSaved.addMouseListener(this);
		btnStartTournamentMode.addMouseListener(this);
	}

	/**
	 * Initialize view.
	 */
	private void initializeView() {
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 450);
		// Initialize JPanel contentPane to hold the JLabel and JButton elements
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(20, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblWelcome = new JLabel("Risk Game");
		lblWelcome.setBounds(0, 13, 432, 33);
		lblWelcome.setVerticalAlignment(SwingConstants.TOP);
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblWelcome.setForeground(Color.WHITE);
		contentPane.add(lblWelcome);

		btnLoad = new JButton("Load Map File");
		btnLoad.setBounds(135, 109, 166, 47);
		contentPane.add(btnLoad);

		JLabel lblInfoText = new JLabel("Start with selecting a Map File, or Creating a new Map");
		lblInfoText.setHorizontalAlignment(SwingConstants.CENTER);
		lblInfoText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblInfoText.setForeground(Color.LIGHT_GRAY);
		lblInfoText.setBackground(Color.BLACK);
		lblInfoText.setBounds(0, 46, 432, 33);
		contentPane.add(lblInfoText);

		btnNewMap = new JButton("Create a New Map");
		btnNewMap.setBounds(135, 169, 166, 47);
		contentPane.add(btnNewMap);

		btnLoadSaved = new JButton("Load a saved Game");
		btnLoadSaved.setBounds(135, 229, 166, 47);
		contentPane.add(btnLoadSaved);

		btnStartTournamentMode = new JButton("Start Tournament");
		btnStartTournamentMode.setBounds(135, 289, 166, 47);
		contentPane.add(btnStartTournamentMode);
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		boolean isSaved = false;
		if (event.getComponent() == btnLoad) {
			setVisible(false);
			fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			fileChooser.setDialogTitle("Choose a Map File");
			fileChooser.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Map File Extensions", "map", "MAP");
			fileChooser.addChoosableFileFilter(filter);
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getAbsolutePath();
				System.out.println("Path: " + filename);
				try {
					parser = new MapFileReader(new File(filename));
					parser.readFile();
					if (!parser.checkFileValidation()) {
						System.out.println("Invalid File Selected!");
						LoggingUtil.logMessage("Invalid File Selected!");
						return;
					}
					view = new MapEditor(parser);
					isSaved = view.readMapEditor(false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (event.getComponent() == btnNewMap) {
			// Implementing the mouse clicked event for btnNewMap
			setVisible(false);
			parser = new MapFileReader();
			view = new MapEditor(parser);
			try {
				isSaved = view.readMapEditor(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (event.getComponent() == btnLoadSaved) {
			// Implementing load saved game functionality
			fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Risk Game Saved Files", "rgs");
			fileChooser.setFileFilter(filter);

			if (fileChooser.showOpenDialog(getRootPane()) == JFileChooser.APPROVE_OPTION) {
				try {
					input = new FileInputStream(fileChooser.getSelectedFile());
					RiskGameDriver.startLoadedGame(input);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			try {
				RiskGameDriver.startTournamentMode();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (isSaved) {
			new PlayerSelectionView(parser).setVisible(true);
		}
	}

	/**
	 * Get Map File Parser.
	 *
	 * @return parser MapFileReader object
	 */
	public MapFileReader getParser() {
		return parser;
	}

	/**
	 * Set the MapFileReader object to the private class variable.
	 *
	 * @param parser
	 *            MapFileReader object
	 */
	public void setParser(MapFileReader parser) {
		this.parser = parser;
	}

	/**
	 * Get the Map Editor View object.
	 *
	 * @return MapEditorView object reference
	 */
	public MapEditor getView() {
		return view;
	}

	/**
	 * Set the MapEditorView object to the private class variable.
	 *
	 * @param view
	 *            MapEditorView object reference
	 */
	public void setView(MapEditor view) {
		this.view = view;
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
