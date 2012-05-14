package ch.bfh.monopoly.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;


/**
 * This class represent the JPanel with the information of the user
 * like balance, color and terrains, and jail card
 * @author snake
 * TODO constants for magic number
 */
public class PlayerInfo extends JPanel{

	private static final long serialVersionUID = 1312492036490100077L;
	
	private static final int MYTERRAIN_PANEL_SIZE = 20;
	private static final int PLAYER_LABEL_SPACE = 4;

	private JLabel playerInfo = new JLabel();
	JPanel terrainUp = new JPanel();
	JPanel terrainDown = new JPanel();
	private List<SmallTerrain> smlTer = new ArrayList<SmallTerrain>();
	
	private boolean toggleVisibility = false;
	private int playerIndex;
	
	private PlayerUpdate plU = new PlayerUpdate();
	private BoardController bc;

	/**
	 * Construct a PlayerInfo
	 * @param c the color of this player
	 */
	public PlayerInfo(int playerIndex, BoardController bc){
		this.playerIndex = playerIndex;
		this.bc = bc;
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//playerInfo.setText(p.getName() + "    " + p.getAccount());
		playerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		//playerInfo.setForeground(c);
		
		//create a new mouse listener
		LabelClick lblClick = new LabelClick();
		playerInfo.addMouseListener(lblClick);

		terrainUp.setLayout(new BoxLayout(terrainUp, BoxLayout.LINE_AXIS));
		terrainDown.setLayout(new BoxLayout(terrainDown, BoxLayout.LINE_AXIS));
		hideTerrains();

		//draw the small panels 
		drawSmallTerrainPanel();
		
		add(playerInfo);
		add(terrainUp);
		add(terrainDown);
		add(Box.createRigidArea(new Dimension(0, PLAYER_LABEL_SPACE)));
	}

	/**
	 * Hide the terrains
	 */
	private void hideTerrains(){
		this.terrainDown.setVisible(false);
		this.terrainUp.setVisible(false);
	}


	/**
	 * Show the terrains
	 */
	public void showTerrains(){
		this.terrainDown.setVisible(true);
		this.terrainUp.setVisible(true);
	}

	/**
	 * Initialize the terrain and give to each of them the 
	 * ID of the corresponding tile on the board
	 */
	private void initSmallTerrainList(){
		SmallTerrain rr1 = null, rr2 = null, rr3 = null, rr4 = null;
		SmallTerrain utility1 = null, utility2 = null;
		
		for(int  j = 0 ; j < MonopolyGUI.TILE_NUMBER ; j++){
			//from 5 to 35 rail road
			if(j == 5)
				rr1 = new SmallTerrain(j, Color.BLACK);
			else if(j == 15)
				rr2 = new SmallTerrain(j, Color.BLACK);
			else if(j == 25)
				rr3 = new SmallTerrain(j, Color.BLACK);
			else if(j == 35)
				rr4 = new SmallTerrain(j, Color.BLACK);
			//from 12 to 28 utilities
			else if(j == 12)
				utility1 = new SmallTerrain(j, Color.DARK_GRAY);
			else if(j == 28)
				utility2 = new SmallTerrain(j, Color.DARK_GRAY);
			//else normal terrain
			else if(j != 0 && j != 2 &&  j != 4 && j != 7 && j != 10 && j != 17 && j != 20
				&& j != 22 && j != 30 && j != 33 && j != 36 && j != 38){
					
				this.smlTer.add(new SmallTerrain(j, Color.decode(this.bc.getTileInfoById(j).getRGB())));	
			}
		}
		
		//add the railroad
		this.smlTer.add(rr1);
		this.smlTer.add(rr2);
		this.smlTer.add(rr3);
		this.smlTer.add(rr4);
	
		//add the utility
		this.smlTer.add(utility1);
		this.smlTer.add(utility2);
	}
	
	/**
	 * Draw the panel under the label with
	 * the small terrains
	 */
	private void drawSmallTerrainPanel(){
		//initialize the small terrain list
		initSmallTerrainList();
		
		for(int i = 0 ; i < this.smlTer.size() ; i++){
			
			//first line
			if(i < 14){
				terrainUp.add(this.smlTer.get(i));

				//fillers
				if(i == 1 || i == 4 || i == 7 || i == 10)
					terrainUp.add(drawFiller());
			}
			//second line
			else if(i >= 14){
				terrainDown.add(this.smlTer.get(i));

				//fillers
				if(i == 16 || i == 19 || i == 21 || i == 25)
					terrainDown.add(drawFiller());		
			}		
		}
	}
	
	/**
	 * Used to draw a gray filler 
	 * @return
	 */
	private JPanel drawFiller(){
		JPanel filler = new JPanel();
		filler.setMaximumSize(new Dimension(MYTERRAIN_PANEL_SIZE,MYTERRAIN_PANEL_SIZE));
		filler.setBorder(BorderFactory.createEtchedBorder());
		filler.setBackground(Color.GRAY);
		return filler;
	}
	
	/**
	 * This inner class represent the small terrain in the viewer
	 * @author snake
	 * TODO check if the field ID is useful
	 */
	private class SmallTerrain extends JPanel{
		private static final long serialVersionUID = 7182696401752617070L;
		private int id;
		private Color backgroundColor;
		
		public SmallTerrain(int id, Color backgroundColor){
			setMaximumSize(new Dimension(MYTERRAIN_PANEL_SIZE,MYTERRAIN_PANEL_SIZE));
			setBorder(BorderFactory.createEtchedBorder());
			setBackground(Color.WHITE);
			this.id = id;
			this.backgroundColor = backgroundColor;
		}
	
		/**
		 * Set the background color accrodingly to the color 
		 * assigned in the constructor
		 */
		public void setBackgroundColor(){
			setBackground(this.backgroundColor);
		}
		
		public void resetBackgroundColor(){
			setBackground(Color.WHITE);
		}
	}
	
	/**
	 * This inner class represent the mouse listener used
	 * to show / hide the panel with the terrains
	 * @author snake
	 *
	 */
	private class LabelClick implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {
			if(!toggleVisibility){
				showTerrains();
				toggleVisibility = true;
			}
			else if(toggleVisibility){
				hideTerrains();
				toggleVisibility = false;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	/**
	 * This inner class represent the listener of this component
	 * @author snake
	 *
	 */
	class PlayerUpdate implements PlayerListener{

		@Override
		public void updatePlayer(ArrayList<PlayerStateEvent> playerStates) {
			
				String name = playerStates.get(playerIndex).getName();
				int account = playerStates.get(playerIndex).getAccount();
				Color c = playerStates.get(playerIndex).getT().getColor();
				
				boolean[] smallTerrainState = playerStates.get(playerIndex).getTerrains();
				
				System.out.println("Length " + smallTerrainState.length);
				
				//TODO we have 40 tiles
//				//color the panel with the correct color
				for(int j = 0 ; j < smallTerrainState.length ; j++){
//					//if the terrain is not owned, the panel is white
//					if(!smallTerrainState[j])
//						smlTer.get(j).resetBackgroundColor();
//					//otherwise, draw the correct color
//					else
//						smlTer.get(j).setBackgroundColor();
				}
				
				playerInfo.setText(name + "  " + account);
				playerInfo.setForeground(c);
				repaint();
				revalidate();
		}
	}
	
	/**
	 * Get the PlayerListener 
	 * @return PlayerListener
	 * 				the PlayerListener of this component
	 */
	public PlayerListener getPlayerListener(){
		return this.plU;
	}
	
}
