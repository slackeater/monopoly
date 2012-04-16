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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import ch.bfh.monopoly.common.BoardController;
import ch.bfh.monopoly.common.TileListener;
import ch.bfh.monopoly.common.TileStateInfo;
import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardTile extends JPanel implements ActionListener, ItemListener, TileListener{

	private static final long serialVersionUID = 3335141445010622095L;

	private int numberOfTokens = 0;
	private int houseCount = 0;
	private boolean isHotel = false;

	private Set<Token> tokens = new HashSet<Token>();
	private TileInfo ti;
	private JPanel tab;
	private boolean displayInfo = false;
	private BoardController bc;

	private JMenuItem buyHouse;
	private JMenuItem buyHouseRow;
	private JMenuItem buyHotel;
	private JMenuItem buyHotelRow;
	private JMenuItem sellHouse;
	private JMenuItem sellHotel;
	private JMenuItem sellHouseRow;
	private JMenuItem sellHotelRow;
	private JMenuItem mortgage;
	private JMenuItem unmortgage;
	
	private JPanel color;

	
	
	/**
	 * Construct a new BoardTile
	 * @param ti the TileInfo used to passed the information
	 */
	public BoardTile(TileInfo ti, JPanel tab, BoardController bc){
		this.ti = ti;
		this.tab = tab;
		this.bc = bc;
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new GridLayout(3,1));

		color = new JPanel();
		color.setLayout(new BoxLayout(color, BoxLayout.LINE_AXIS));

		ButtonListener btnListener = new ButtonListener();

		if(ti.getGroup() != null && 
				(!ti.getGroup().equals("cornersAndTax") || !ti.getGroup().equals("Community Chest") 
						|| !ti.getGroup().equals("Chance"))){
			//we want a pop-up menu only on the properties where
			//we can build something or 
			this.addMouseListener(btnListener);
			displayInfo = true;
		}

		//check if there is a color
		if(ti.getRGB() != null){
			color.setBackground(Color.decode(ti.getRGB()));
			btnListener.addPopUp(popMenu());
		}

		add(color);

		Font f = new Font(getFont().getName(), Font.PLAIN, getFont().getSize()-1);

		JLabel name = new JLabel(ti.getName());
		name.setFont(f);

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

	/**
	 * Add the information of a tile (rent, name,costs, etc.) to 
	 * the tabbed pane
	 */
	private void addInformationOnTab(){
		if(displayInfo){
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

			JLabel price = new JLabel("Price: " + Integer.toString(ti.getPrice()));
			price.setAlignmentX(Component.CENTER_ALIGNMENT);
			price.setFont(f);

			JLabel rent = new JLabel("Rent: " + Integer.toString(ti.getRent()));
			rent.setAlignmentX(Component.CENTER_ALIGNMENT);
			rent.setFont(f);

			JLabel rent1 = new JLabel("With 1 house: " + Integer.toString(ti.getRent1house()));
			rent1.setAlignmentX(Component.CENTER_ALIGNMENT);
			rent1.setFont(f);

			JLabel rent2 = new JLabel("With 2 houses: " + Integer.toString(ti.getRent2house()));
			rent2.setAlignmentX(Component.CENTER_ALIGNMENT);
			rent2.setFont(f);

			JLabel rent3 = new JLabel("With 3 houses: " + Integer.toString(ti.getRent3house()));
			rent3.setAlignmentX(Component.CENTER_ALIGNMENT);
			rent3.setFont(f);

			JLabel rent4 = new JLabel("With 4 houses: " + Integer.toString(ti.getRent4house()));
			rent4.setAlignmentX(Component.CENTER_ALIGNMENT);
			rent4.setFont(f);

			JLabel hotel = new JLabel("With hotel: " + Integer.toString(ti.getRenthotel()));
			hotel.setAlignmentX(Component.CENTER_ALIGNMENT);
			hotel.setFont(f);

			tab.removeAll();

		
			tab.add(color);
			tab.add(price);
			tab.add(rent);
			tab.add(rent1);
			tab.add(rent2);
			tab.add(rent3);
			tab.add(rent4);
			tab.add(hotel);

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

		buyHouse = new JMenuItem("Buy house");
		buyHouse.addActionListener(this);

		buyHouseRow = new JMenuItem("Buy house row");
		buyHouseRow.addActionListener(this);

		buyHotel = new JMenuItem("Buy hotel");
		buyHotel.addActionListener(this);

		buyHotelRow = new JMenuItem("Buy hotel row");
		buyHotelRow.addActionListener(this);

		sellHouse = new JMenuItem("Sell house");
		sellHouse.addActionListener(this);

		sellHotel = new JMenuItem("Sell hotel");
		sellHotel.addActionListener(this);

		sellHouseRow = new JMenuItem("Sell house row");
		sellHouseRow.addActionListener(this);

		sellHotelRow = new JMenuItem("Sell hotel row");
		sellHotelRow.addActionListener(this);

		mortgage = new JMenuItem("Mortgage");
		mortgage.addActionListener(this);

		unmortgage = new JMenuItem("Unmortgage");
		unmortgage.addActionListener(this);

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


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(buyHouse)){
			bc.buyHouse(ti.getID());
			
//			if(houseCount < 4){
//				JPanel house = new JPanel();
//				house.setBackground(Color.RED);
//				house.setBorder(BorderFactory.createRaisedBevelBorder());
//				house.setMaximumSize(new Dimension((int)getWidth()/6, getHeight()));
//
//				color.add(house);
//
//				houseCount++;
//				revalidate();
//			}
		}
		else if(e.getSource().equals(buyHotel)){
			if(!isHotel && houseCount == 4){
				color.removeAll();
				JPanel hotel = new JPanel();
				hotel.setBackground(Color.GREEN);
				hotel.setBorder(BorderFactory.createRaisedBevelBorder());
				hotel.setMaximumSize(new Dimension((int)getWidth()/3, getHeight()));
				
				color.add(hotel);
				repaint();
				revalidate();
				isHotel = true;
				
			}
		}
	}


	@Override
	public void itemStateChanged(ItemEvent e) {}

	/**
	 * Inner class used to show the popup menu 
	 * @author snake
	 *
	 */
	class ButtonListener extends MouseAdapter{
		JPopupMenu popup;

		public void addPopUp(JPopupMenu pop){
			this.popup = pop;
		}

		public void mousePressed(MouseEvent e) {
			if(e.getButton() == MouseEvent.BUTTON1){
				addInformationOnTab();
			}
			else{
				maybeShowPopup(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger() && popup != null) {
				popup.show(e.getComponent(),
						e.getX(), e.getY());
			}
		}
	}

	@Override
	public void updateTile(TileStateInfo tsi) {
		System.out.println("ciao");
		repaint();
		revalidate();
	}
}
