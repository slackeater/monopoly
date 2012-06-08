package ch.bfh.monopoly.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * This class is used to build the board
 * of the GUI
 * @author snake
 *
 */
public class BoardBuilder extends JPanel {

	private static final long serialVersionUID = 4140219449792816066L;
	
	/**
	 * Weight of tiles and corners
	 */
	private final double WEIGHT_TILE_X = 0.1;
	private final double WEIGHT_TILE_Y = 0.1;
	private final double WEIGHT_CORNER_X = 0.12;
	private final double WEIGHT_CORNER_Y = 0.12;
	
	/**
	 * The central tabbed pane where to show events
	 */
	private JTabbedPane eventPane;

	/**
	 * List of tiles
	 */
	private List<BoardTile> tilesList;
	
	/**
	 * Buttons in the first tab
	 */
	private List<JButton> btns;
	
	/**
	 * Area where to write messages
	 */
	private JTextArea txt;

	/**
	 * Resource bundle used to i18n for buttons and other elements
	 */
	private ResourceBundle res;
	
	/**
	 * Construct a new board
	 * @param eventPane the JTextArea used to draw event
	 * @param tiles the list of tiles 
	 */
	public BoardBuilder(JTextArea txt, JTabbedPane tabPane, List<BoardTile> tiles, List<JButton> btns, ResourceBundle res){
		this.txt = txt;
		this.btns = btns;
		this.eventPane = tabPane;
		this.tilesList = tiles;
		this.res = res;
		
		setLayout(new GridBagLayout());
		
		System.out.println("INSIDE BOARD BUILDER");
		centralElements();
		drawBoard();
	}

	/**
	 * Add to the layout the elements in the middle of
	 * the board (buttons and event window)
	 */
	private void centralElements(){

		/******* Event window **************/
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
		p.setBorder(BorderFactory.createEtchedBorder());

		JPanel btnPanel = new JPanel();
		
		JScrollPane scrollBtn = new JScrollPane(btnPanel);
		scrollBtn.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollBtn.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		//add the buttons to the central panel
		for(JButton btn : btns)
			btnPanel.add(btn);
	
		JScrollPane scrollEventPane = new JScrollPane(this.txt);
		scrollEventPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollEventPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		p.add(scrollEventPane);
		p.add(scrollBtn);
		
		this.eventPane.addTab(res.getString("tab-event"), p);
		
		add(this.eventPane, new GridBagConstraints(3,3, 5,5, WEIGHT_TILE_X, WEIGHT_TILE_Y, 
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

	}

	/**
	 * Initialize the board 
	 * with the relatives tiles
	 */
	private void drawBoard(){
		for(int j = 0 ; j < this.tilesList.size() ; j++){
			BoardTile tile = this.tilesList.get(j);
		
			//corner
			if(j % 10 == 0)
				add(tile, new GridBagConstraints(tile.getTileInfoX(), tile.getTileInfoY(), 1, 1, WEIGHT_CORNER_X, WEIGHT_CORNER_Y, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			//other tiles
			else
				add(tile, new GridBagConstraints(tile.getTileInfoX(), tile.getTileInfoY(), 1, 1, WEIGHT_TILE_X, WEIGHT_TILE_Y, 
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		}
	}
}
