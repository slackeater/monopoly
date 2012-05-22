package ch.bfh.monopoly.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.GameController;
import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.observer.PlayerListener;
import ch.bfh.monopoly.observer.PlayerStateEvent;
import ch.bfh.monopoly.observer.TileListener;
import ch.bfh.monopoly.observer.TileStateEvent;
import ch.bfh.monopoly.tile.TileInfo;

/**
 * This class represent a tile on the board
 * @author snake
 *
 */
public class BoardTile extends JPanel{

	private static final long serialVersionUID = 3335141445010622095L;

	private int numberOfTokens = 0;
	private int houseCount = 0;
	private boolean isHotel = false;

	private Set<Token> tokens = new HashSet<Token>();
	private TileInfo ti;
	private JPanel tab;
	private boolean displayInfo = false;
	private BoardController bc;
	private GameController gc;
	private ResourceBundle res;
	private String owner;
	private BoardTile[] groupMemeber = new BoardTile[2];
	private ButtonListener btnListener;

	//used when we right click on a tile
	private PerformActionMenu ac;

	//used to update the tile
	private InformationUpdate iu = new InformationUpdate();

	private JMenuItem buyHouse, buyHouseRow, buyHotel, buyHotelRow, sellHouse, sellHotel, sellHouseRow,
	sellHotelRow, mortgage, unmortgage;

	private JPanel color;

	//to check if we have clicked over a button
	private boolean buyHouseClicked, buyHotelClicked, buyHouseRowClicked,
	buyHotelRowClicked, sellHouseClicked, sellHotelClicked, sellHouseRowClicked,
	sellHotelRowClicked, mortgageClicked, unmortgageClicked;

	/**
	 * Construct a new BoardTile
	 * @param ti the TileInfo used to passed the information
	 */
	public BoardTile(TileInfo ti, JPanel tab, BoardController bc, GameController gc, ResourceBundle res){
		this.ti = ti;
		this.tab = tab;
		this.bc = bc;
		this.gc = gc;
		this.res = res;
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridLayout(3,1));

		color = new JPanel();
		color.setLayout(new BoxLayout(color, BoxLayout.LINE_AXIS));

		btnListener = new ButtonListener();
		ac = new PerformActionMenu();

		if(ti.getGroup() != null && 
				(!ti.getGroup().equals("cornersAndTax") || !ti.getGroup().equals("Community Chest") 
						|| !ti.getGroup().equals("Chance"))){
			//we want a pop-up menu only on the properties where
			//we can build something and we are the owner
			//TODO remove if for test

			this.addMouseListener(btnListener);
			bc.getSubjectForPlayer().addListener(new OwnerUpdater());
			displayInfo = true;

		}

		//check if there is a color and add the menu
		if(ti.getRGB() != null){
			color.setBackground(Color.decode(ti.getRGB()));
			btnListener.addPopUp(popMenu());
		}

		add(color);

		Font f = new Font(getFont().getName(), Font.PLAIN, getFont().getSize()-1);

		JLabel name = new JLabel(ti.getName());
		name.setFont(f);

		setMaximumSize(new Dimension(75, 75));
		add(name);
	}


	/**
	 * Get the X coordinate of this tile
	 * @return an int that correspond to the X coordinate
	 */
	public int getTileInfoX(){
		return ti.getCoordX();
	}

	/**
	 * Get the Y coordinate of this tile
	 * @return an int that correspond to the Y coordinate
	 */
	public int getTileInfoY(){
		return ti.getCoordY();
	}

	/**
	 * Remove a token from this tile
	 * @param index the index of the token to be removed
	 */
	public void removeToken(Token t){
		this.tokens.remove(t);
	}

	/**
	 * Add a token to this tile
	 * @param t the token to be added
	 */
	public void addToken(Token t){
		this.tokens.add(t);
	}

	public void setGroupMember(BoardTile[] btMember){

		this.groupMemeber = btMember;

		//TODO REMOVE ONLY FOR TEST
		//		if(btMember[0] != null && btMember[1] != null){
		//		
		//			System.out.println("IM THE TILE : " + ti.getName());
		//			System.out.println("=>> " + btMember[0].getNameTile());
		//			System.out.println("=>> " + btMember[1].getNameTile());
		//		}
		//		else if(btMember[0] != null && btMember[1] == null){
		//			System.out.println("IM THE TILE : " + ti.getName());
		//			System.out.println("=>> " + btMember[0].getNameTile());
		//		}

	}

	/**
	 * TODO => TEMP TEMP TEMP ONLY FOR TESTING THE GROUP MEMBER
	 */
	public String getNameTile(){
		return ti.getName();
	}

	/**
	 * Add the information of a tile (rent, name,costs, etc.) to 
	 * the tabbed pane
	 */
	private void addInformationOnTab(){
		if(displayInfo){
			//clean the panel
			tab.removeAll();

			Font f = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
			Font f2 = new Font(getFont().getName(), Font.BOLD, getFont().getSize());

			JLabel name = new JLabel(ti.getName());
			name.setAlignmentX(Component.CENTER_ALIGNMENT);
			name.setFont(f2);

			JPanel color = new JPanel();

			color.setBorder(BorderFactory.createEtchedBorder());
			color.setMaximumSize(new Dimension(tab.getWidth(), getHeight()/3));

			if(ti.getRGB() != null)
				color.setBackground(Color.decode(ti.getRGB()));
			else
				color.setBackground(Color.WHITE);

			color.add(name);

			JLabel price = new JLabel(res.getString("label-price") + Integer.toString(ti.getPrice()));
			price.setAlignmentX(Component.CENTER_ALIGNMENT);
			price.setFont(f);

			tab.add(color);
			tab.add(price);	

			if(ti.getRent() != -1){
				JLabel rent = new JLabel(res.getString("label-rent") + Integer.toString(ti.getRent()));
				rent.setAlignmentX(Component.CENTER_ALIGNMENT);
				rent.setFont(f);

				tab.add(rent);

				JLabel rent1 = new JLabel(res.getString("label-onehouse") + Integer.toString(ti.getRent1house()));
				rent1.setAlignmentX(Component.CENTER_ALIGNMENT);
				rent1.setFont(f);

				JLabel rent2 = new JLabel(res.getString("label-twohouses") + Integer.toString(ti.getRent2house()));
				rent2.setAlignmentX(Component.CENTER_ALIGNMENT);
				rent2.setFont(f);

				JLabel rent3 = new JLabel(res.getString("label-threehouses") + Integer.toString(ti.getRent3house()));
				rent3.setAlignmentX(Component.CENTER_ALIGNMENT);
				rent3.setFont(f);

				JLabel rent4 = new JLabel(res.getString("label-fourhouses") + Integer.toString(ti.getRent4house()));
				rent4.setAlignmentX(Component.CENTER_ALIGNMENT);
				rent4.setFont(f);

				JLabel hotel = new JLabel(res.getString("label-hotel") + Integer.toString(ti.getRenthotel()));
				hotel.setAlignmentX(Component.CENTER_ALIGNMENT);
				hotel.setFont(f);

				tab.add(rent1);
				tab.add(rent2);
				tab.add(rent3);
				tab.add(rent4);
				tab.add(hotel);
			}

			JLabel mortgage = new JLabel(res.getString("label-mortgagevalue") + Integer.toString(ti.getMortgageValue()));
			mortgage.setAlignmentX(Component.CENTER_ALIGNMENT);
			mortgage.setFont(f);

			tab.add(mortgage);

			tab.revalidate();
			tab.repaint();
		}
	}

	/**
	 * Creates a popup menu for this tile
	 * @return a JPopupMenu with the actions possible for this tile
	 */
	private JPopupMenu popMenu(){
		JPopupMenu pop = new JPopupMenu();

		buyHouse = new JMenuItem(res.getString("label-buyhouse"));
		buyHouse.addActionListener(ac);

		buyHouseRow = new JMenuItem(res.getString("label-buyhouserow"));
		buyHouseRow.addActionListener(ac);

		buyHotel = new JMenuItem(res.getString("label-buyhotel"));
		buyHotel.addActionListener(ac);

		buyHotelRow = new JMenuItem(res.getString("label-buyhotelrow"));
		buyHotelRow.addActionListener(ac);

		sellHouse = new JMenuItem(res.getString("label-sellhouse"));
		sellHouse.addActionListener(ac);

		sellHotel = new JMenuItem(res.getString("label-sellhotel"));
		sellHotel.addActionListener(ac);

		sellHouseRow = new JMenuItem(res.getString("label-sellhouserow"));
		sellHouseRow.addActionListener(ac);

		sellHotelRow = new JMenuItem(res.getString("label-sellhotelrow"));
		sellHotelRow.addActionListener(ac);

		mortgage = new JMenuItem(res.getString("label-mortgage"));
		mortgage.addActionListener(ac);

		unmortgage = new JMenuItem(res.getString("label-unmortgage"));
		unmortgage.addActionListener(ac);

		pop.add(buyHouse);
		pop.add(buyHouseRow);
		pop.add(buyHotel);
		pop.add(buyHotelRow);
		pop.addSeparator();
		pop.add(sellHouse);
		pop.add(sellHouseRow);
		pop.add(sellHotel);
		pop.add(sellHotelRow);
		pop.addSeparator();
		pop.add(mortgage);
		pop.add(unmortgage);

		return pop;
	}

	/**
	 * Show tile's information in card's box
	 */
	public void showCard(){
		addInformationOnTab();
	}


	/**
	 * Draw the tokens on this tile
	 */
	@Override 
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

		this.numberOfTokens = tokens.size();

		if(this.numberOfTokens >= 1 && this.numberOfTokens <= 8){
			for(int i = 0 ; i < this.numberOfTokens ; i++){
				Iterator<Token> itr = this.tokens.iterator();

				while(itr.hasNext()){
					Token t = itr.next();
					g2.setColor(t.getColor());
					g2.fillOval((int)(getWidth()*t.getXRatio()), (int)(getHeight()*t.getYRatio()), (int)(getHeight()*0.25), (int)(getHeight()*0.25));
				}
			}
		}
	}

	/**
	 * Draw an house
	 * @return JPanel
	 * 			a JPanel representing an house
	 */
	private JPanel drawHouse(){
		JPanel building = new JPanel();
		building.setBorder(BorderFactory.createRaisedBevelBorder());
		building.setBackground(Color.RED);
		building.setMaximumSize(new Dimension((int)getWidth()/6, getHeight()));
		return building;
	}

	/**
	 * Draw an hotel
	 * @return JPanel
	 * 			a JPanel representing an hotel
	 */
	private JPanel drawHotel(){
		JPanel building = new JPanel();
		building.setBorder(BorderFactory.createRaisedBevelBorder());
		building.setBackground(Color.GREEN);
		building.setMaximumSize(new Dimension((int)getWidth()/3, getHeight()));
		return building;
	}

	/**
	 * Draw a building
	 * @param type boolean true == hotel ; false == house
	 */
	public void drawBuilding(boolean type){
		//if true is hotel
		if(type && !isHotel && houseCount == 4){
			color.removeAll();
			//we have drawn an hotel
			isHotel = true;
			color.add(drawHotel());
		}
		else if(!type && houseCount < 4){
			houseCount++;
			color.add(drawHouse());
		}

		repaint();
		revalidate();
	}

	/**
	 * Remove a building from this tile
	 * @param type boolean
	 * 					a boolean representing the type of building
	 * 					true == hotel, false = house
	 */
	public void removeBuilding(boolean type){
		//remove house
		if(!type && houseCount > 0 && houseCount <= 4){
			houseCount--;
			color.remove(0);
		}
		//remove hotel
		else if(type && isHotel && houseCount == 4){
			color.remove(0);
			color.add(drawHouse());
			color.add(drawHouse());
			color.add(drawHouse());
			color.add(drawHouse());
			isHotel = false;
		}

		repaint();
		revalidate();
	}

	/**
	 * Change the color of the background to show that is mortgaged
	 */
	private void mortgagePanel(){
		System.out.println("INSIDE MORTGAGA PENAL");
		color.setBackground(Color.BLACK);
		repaint();
		revalidate();
	}

	/**
	 * Unmortgage the terrain by change the color to 
	 * the initial one
	 */
	private void unmortgagePanel(){
		color.setBackground(Color.decode(ti.getRGB()));
		repaint();
		revalidate();
	}

	/**
	 * Inner class used to show the popup menu 
	 * @author snake, shrevek
	 */
	private class ButtonListener extends MouseAdapter{
		JPopupMenu popup;
		boolean owner = false;

		public void addPopUp(JPopupMenu pop){
			this.popup = pop;
		}

		public void mousePressed(MouseEvent e) {
			//left click
			System.out.println(e.getButton() + " CONTROL DOWN: " + e.isControlDown());
			if(e.getButton() == MouseEvent.BUTTON1 && !e.isControlDown()){ 
				addInformationOnTab();
			}
			//right click, isControlDown is for a macintosh personal computer
			else if(e.getButton() == MouseEvent.BUTTON3 || (e.isControlDown() && e.getButton() == 1)){
				if(owner){
					showPopup(e);
				}
			}
		}

		public void setOwner(){
			owner = true;
		}

		private void showPopup(MouseEvent e) {
			if (e.isPopupTrigger() && popup != null) {
				popup.show(e.getComponent(),
						e.getX(), e.getY());
			}
		}
	}

	/**
	 * Inner class used to manage the mouse click on the menu
	 * @author snake, shrevek
	 * 
	 */
	private class PerformActionMenu implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

			//reset the buttons
			buyHouseClicked = false;
			buyHotelClicked = false;
			buyHouseRowClicked = false;
			buyHotelRowClicked = false;
			sellHouseClicked = false;
			sellHotelClicked = false;
			sellHouseRowClicked = false;
			sellHotelRowClicked = false;
			mortgageClicked = false;
			unmortgageClicked = false;

			if(e.getSource().equals(buyHouse)){
				buyHouseClicked = true;
				gc.buyHouse(ti.getTileId());
			}
			else if(e.getSource().equals(buyHotel)){
				buyHotelClicked = true;
				gc.buyHotel(ti.getTileId());
			}
			else if(e.getSource().equals(buyHouseRow)){
				buyHouseRowClicked = true;
				//TODO
			}
			else if(e.getSource().equals(buyHotelRow)){
				buyHotelRowClicked = true;
				//TODO
			}
			else if(e.getSource().equals(sellHouse)){
				sellHouseClicked = true;
				gc.sellHouse(ti.getTileId());
			}
			else if(e.getSource().equals(sellHotel)){
				sellHotelClicked = true;
				gc.sellHotel(ti.getTileId());
			}
			else if(e.getSource().equals(sellHouseRow)){
				sellHouseRowClicked = true;
				//TODO
			}
			else if(e.getSource().equals(sellHotelRow)){
				sellHotelRowClicked = true;
				//TODO
			}
			else if(e.getSource().equals(mortgage)){
				mortgageClicked = true;
				System.out.println("INSIDE MORTGAGE");
				gc.toggleMortgageStatus(ti.getTileId());
			}
			else if(e.getSource().equals(unmortgage)){
				unmortgageClicked = true;
				gc.toggleMortgageStatus(ti.getTileId());
			}

		}	
	}

	/**
	 * Inner class used to update the information on this tile
	 * @author snake, shrevek
	 */
	private class InformationUpdate implements TileListener{
		@Override
		public void updateTile(TileStateEvent tsi) {

			if(buyHouseClicked){
				drawBuilding(false);
			}
			else if(buyHotelClicked){
				drawBuilding(true);
			}
			else if(buyHouseRowClicked){
				//TODO
			}
			else if(buyHotelRowClicked){
				//TODO
			}
			else if(sellHouseClicked){
				removeBuilding(false);
			}
			else if(sellHotelClicked){
				removeBuilding(true);
			}
			else if(sellHouseRowClicked){
				//TODO
			}
			else if(sellHotelRowClicked){
				//TODO
			}
			else if(mortgageClicked){
				System.out.println("MORTGAGE");
				mortgagePanel();
			}
			else if(unmortgageClicked){
				unmortgagePanel();
			}
		}
	}

	class OwnerUpdater implements PlayerListener{
		@Override
		public void updatePlayer(ArrayList<PlayerStateEvent> playerStates) {
			if(ti.getTileId() != -1 && 
					bc.getTileInfoById(ti.getTileId()).getOwner() != null && bc.getTileInfoById(ti.getTileId()).getOwner().equals(gc.getLocalPlayerName())){
				btnListener.setOwner();
			}
		}	
	}

	/**
	 * This method is called by an external
	 * class to update the information on this tile
	 * @param tsi
	 */
	public void updateTile(TileStateEvent tse){
		iu.updateTile(tse);
	}

	public TileListener getTileListener(){
		return this.iu;
	}




}
