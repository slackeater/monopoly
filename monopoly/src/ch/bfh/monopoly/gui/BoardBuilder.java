package ch.bfh.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ch.bfh.monopoly.common.Chance;
import ch.bfh.monopoly.common.GameClient;
import ch.bfh.monopoly.common.NonProperty;
import ch.bfh.monopoly.common.Terrain;

public class BoardBuilder extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4140219449792816066L;
	private JTextArea eventPane;

	/*this list will be used for computation of dice throw
	 * instead of monopolyTiles[][]. It is more comfortable
	 * and we can use the get(index) method to easily set
	 * the Tile of new tokens
	 */
	private List<JPanel> tilesForDice = new ArrayList<JPanel>();

	/**
	 * Construct a new board
	 * @param eventPane the JTextArea used to draw event
	 */
	public BoardBuilder(JTextArea eventPane){
		this.eventPane = eventPane;
		setLayout(new GridBagLayout());
	}

	/**
	 * Add to the layout the elements in the middle of
	 * the board (buttons and event window)
	 */
	private void centralElements(){

		/******* event window **************/

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));

		JPanel notify = new JPanel();
		notify.setLayout(new BoxLayout(notify, BoxLayout.PAGE_AXIS));
		notify.setBorder(BorderFactory.createEtchedBorder());

		JButton buy = new JButton("Buy");
		JButton auction = new JButton("Auction");

		JPanel btnPanel = new JPanel();
		btnPanel.add(buy);
		btnPanel.add(auction);

		JScrollPane eventPane = new JScrollPane(this.eventPane);
		eventPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		eventPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JLabel title = new JLabel("Events");

		notify.add(title);
		notify.add(eventPane);
		notify.add(btnPanel);

		p.add(notify, BorderLayout.NORTH);
		p.setOpaque(false);
		add(p, new GridBagConstraints(1, 1, 9,9, 0.1, 0.1, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		/****************************************/
		/****************************************/

		/******* Community chest card **************/

		JButton community = new JButton("Community");
		JPanel commP = new JPanel();
		commP.add(community);
		commP.setLayout(new GridLayout(1,1));
		add(commP, new GridBagConstraints(2, 2, 1, 1, 0.1, 0.1, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		/****************************************/
		/****************************************/

		/******* Chance chest card **************/

		JButton chance = new JButton("Chance");
		JPanel chancePan = new JPanel();
		chancePan.add(chance);
		chancePan.setLayout(new GridLayout(1,1));
		add(chancePan, new GridBagConstraints(8, 8, 1, 1, 0.1, 0.1, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		/****************************************/
		/****************************************/
	}

	/**
	 * Initialize the four sides of the board 
	 * with the relatives tiles
	 */
	private void drawBoard(){
		//bottom line
		int j = 0;

		JPanel start = new NonProperty();
		start.add(new JLabel("<-- Go"));
		start.setBorder(BorderFactory.createEtchedBorder());

		add(start, new GridBagConstraints(10, 10, 1, 1, 0.12, 0.12, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		tilesForDice.add(start);

		for(j = 8; j >= 0 ; j++){
			//first community chest
			if(j == 8){
				JPanel comm = new Chance("Community", new GameClient());
				comm.add(new JLabel("Community"));
				comm.setBorder(BorderFactory.createEtchedBorder());
				add(comm, new GridBagConstraints(8, 10, 1, 1, 0.1, 0.1, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			//income tax
			else if(j == 6){
				JPanel nonP = new NonProperty();
				nonP.add(new JLabel("<html>Income<br>Tax</html>"));
	
				add(nonP, new GridBagConstraints(6, 10, 1, 1, 0.1, 0.1, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			//first chance card
			else if(j == 3){
				JPanel chance = new NonProperty();
				chance.add(new JLabel("<html>Free<br>Parking</html>"));
	
				add(chance, new GridBagConstraints(3, 10, 1, 1, 0.12, 0.12, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			else{
				JPanel terrain = new Terrain();
				terrain.add(new JLabel("Terrain"));
				
				add(terrain, new GridBagConstraints(j, 10, 1, 1, 0.12, 0.12, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
		}


		for(int j = 10 ; j >= 0 ; j--){ //columns
			for(int i = 10 ; i >= 0 ; i--){ //rows

				//free parking
				if(j == 0 && i == 0){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("<html>Free<br>Parking</html>"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
				//jail
				else if(j == 0 && i == 10){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("Jail"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
				//start 
				else if(j == 10 && i == 10){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("<-- Go"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());

					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
				//go to jail
				else if(j == 10 && i == 0){
					monopolyTiles[j][i] = new NonProperty();
					monopolyTiles[j][i].add(new JLabel("Go to jail"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.12, 0.12, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
				//chance cards
				else if ((j == 2 && i == 0) || (j == 10 && i == 6) || (j == 2 && i == 10)){
					monopolyTiles[j][i] = new Chance("Chance", new GameClient());
					monopolyTiles[j][i].add(new JLabel("Chance"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
				//community chest
				else if ((j == 10 && i == 3) || (j == 8 && i == 10) || (j == 0 && i == 3)){
					//TO CHANGE TO new Community
					monopolyTiles[j][i] = new Chance("Community", new GameClient());
					monopolyTiles[j][i].add(new JLabel("Community"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
				else if(j == 0 || i == 0 || j == 10 || i == 10){
					monopolyTiles[j][i] = new Terrain();
					monopolyTiles[j][i].add(new JLabel("Tile"));
					monopolyTiles[j][i].setBorder(BorderFactory.createEtchedBorder());
					add(monopolyTiles[j][i], new GridBagConstraints(j, i, 1, 1, 0.1, 0.1, 
							GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					tilesForDice.add(monopolyTiles[j][i]);
				}
			}
		}

	}
}
