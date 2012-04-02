package ch.bfh.monopoly.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import ch.bfh.monopoly.common.Token;
import ch.bfh.monopoly.tile.TileInfo;

public class BoardTile extends JPanel implements ActionListener, ItemListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335141445010622095L;
	private int numberOfTokens = 0;
	private Set<Token> tokens = new HashSet<Token>();
	private TileInfo ti;
	
	/**
	 * Construct a new BoardTile
	 * @param ti the TileInfo used to passed the information
	 */
	public BoardTile(TileInfo ti){
		this.ti = ti;
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BorderLayout());
	
		JPanel color = new JPanel();
		
		ButtonListener btnListener = new ButtonListener();
		
		if(ti.getGroup() != null && 
		(!ti.getGroup().equals("cornersAndTax") || !ti.getGroup().equals("Community Chest") 
		|| !ti.getGroup().equals("Chance"))){
			//we want a pop-up menu only on the properties where
			//we can build something or 
			this.addMouseListener(btnListener);
		}
		
		//check if there is a color
		if(ti.getRGB() != null){
			color.setBackground(Color.decode(ti.getRGB()));
			btnListener.addPopUp(popMenu());
		}
		
		add(color, BorderLayout.NORTH);
		
		JLabel name = new JLabel(ti.getName());
		add(name, BorderLayout.CENTER);
		
		
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
	 * Creates a popup menu for this tile
	 * @return a JPopupMenu with the actions possible for this tile
	 */
	private JPopupMenu popMenu(){
		JPopupMenu pop = new JPopupMenu();
		
		JMenuItem buyHouse = new JMenuItem("Buy house");
		buyHouse.addActionListener(this);
		
		JMenuItem buyHouseRow = new JMenuItem("Buy house row");
		buyHouseRow.addActionListener(this);
		
		JMenuItem buyHotel = new JMenuItem("Buy hotel");
		buyHotel.addActionListener(this);
		
		JMenuItem buyHotelRow = new JMenuItem("Buy hotel row");
		buyHotelRow.addActionListener(this);
		
		pop.add(buyHouse);
		pop.add(buyHouseRow);
		pop.add(buyHotel);
		pop.add(buyHotelRow);
		
		return pop;
	}
	
	
	/**
	 * Draw the tokens on this tile
	 */
	@Override 
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;

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
		// TODO Auto-generated method stub
		
	}


	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	class ButtonListener extends MouseAdapter{
		JPopupMenu popup;
		 
		public void addPopUp(JPopupMenu pop){
			this.popup = pop;
		}
		
        public void mousePressed(MouseEvent e) {
        	if(e.getButton() == MouseEvent.BUTTON1){
        		System.out.println("leftbtn");
        	}
            maybeShowPopup(e);
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
	
}
