package ch.bfh.monopoly.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import ch.bfh.monopoly.tests.EventJPanelTest;
import ch.bfh.monopoly.tile.EventPanelFactory;
import ch.bfh.monopoly.tile.EventPanelInfo;
import ch.bfh.monopoly.tile.EventPanelSource;
import ch.bfh.monopoly.tile.Step;

public class Dice implements EventPanelSource {

	int maxValDiceOne;
	int maxValDiceTwo;
	int throwValueOne = 0;
	int throwValueTwo = 0;
	JPanel jpanel;
	JButton buttonRight = new JButton();
	JTextArea descriptionLabel = new JTextArea();
	GameClient gameClient;
	ActionListener al;
	boolean testOff;
	int attemptedRolls = 0;
	ResourceBundle rb;
	EventPanelFactory epf = new EventPanelFactory(this);

	/**
	 * Construct a dice object
	 * 
	 * @param maxValDiceOne
	 *            the maximal value for the die one
	 * @param maxValDiceTwo
	 *            the maximal value for the die two
	 */
	public Dice(int maxValDiceOne, int maxValDiceTwo) {
		this.maxValDiceOne = maxValDiceOne;
		this.maxValDiceTwo = maxValDiceTwo;
	}

	/**
	 * Construct a dice object
	 * 
	 * @param maxValDiceOne
	 *            the maximal value for the die one
	 * @param maxValDiceTwo
	 *            the maximal value for the die two
	 */
	public Dice(int maxValDiceOne, int maxValDiceTwo, GameClient gameClient,
			boolean testOff) {
		this.testOff = testOff;
		this.gameClient = gameClient;
		this.maxValDiceOne = maxValDiceOne;
		this.maxValDiceTwo = maxValDiceTwo;
		rb = ResourceBundle.getBundle("ch.bfh.monopoly.resources.tile",
				gameClient.getLoc());
		jpanel = (new EventPanelFactory()).getJPanel();
	}

	/**
	 * Throw the dice
	 * 
	 * @return int an integer representing the throw of the two dice
	 */
	public int throwDice() {
		// generates number between 1 and 12
		// the one is added because Math.random() generates
		// number between 0.0 and 1.0. But 1.0 is not included.
		// so in the case where:
		// maxValDice = 6 ; Math.random = 0.99
		// 6*0.99 = 5.94 rounded => 5 + 1 = 6
		throwValueOne = (int) (maxValDiceOne * Math.random()) + 1;
		throwValueTwo = (int) (maxValDiceTwo * Math.random()) + 1;

		return throwValueOne + throwValueTwo;
	}

	/**
	 * Get the single value of both dice
	 * 
	 * @return String a string representing the two values, for example 3, 5
	 */
	public String getDiceValues() {
		return throwValueOne + ", " + throwValueTwo;
	}

	public boolean isDoubles() {
		return throwValueOne == throwValueTwo;
	}

	private JPanel imageLogo(String name) {
		JPanel img = new JPanel();
		java.net.URL urlImg = Monopoly.class
				.getResource("/ch/bfh/monopoly/resources/" + name);
		ImageIcon logo = new ImageIcon(urlImg);
		JLabel imgLab = new JLabel(logo);
		img.add(imgLab);
		return img;
	}

	public JPanel getNewStartRoll() {
		epf.changePanel(Step.ROLL_NORMAL);
		return epf.getJPanel();
	}

	public JPanel getNewJailStart() {
		attemptedRolls = 0;
		epf.changePanel(Step.JAIL_START);
		return epf.getJPanel();
	}

	public EventPanelInfo getEventPanelInfoForStep(Step step) {
		String labelText;
		String buttonText;
		ActionListener al;

		EventPanelInfo epi;

		switch (step) {
		case ROLL_NORMAL:
			epi = getRollStartEPI(false);
			break;
		case ROLL_NORMAL2:
			epi = new EventPanelInfo();
			final int roll = throwDice();
			System.out.println("DICE CLASS rolled: " + roll);
			buttonRight = new JButton();

			labelText = rb.getString("youRolled") + getDiceValues() + " "
					+ rb.getString("advance") + " " + roll + " "
					+ rb.getString("spaces");
			buttonText = rb.getString("continueButton");
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					buttonRight.setEnabled(false);
					int prevPos = gameClient.getCurrentPlayer().getPosition();
					gameClient.advancePlayerNSpaces(roll, testOff);
					// gameClient.sendTransactionSuccesToGUI(true);
					int curPos = gameClient.getCurrentPlayer().getPosition();
					System.out.println("PrevPos" + prevPos + "  CurPos"
							+ curPos);
					epf.disableAfterClick();
				}
			};
			epi.setText(labelText);
			epi.addActionListener(al);
			epi.addButtonText(buttonText);
			break;
		case JAIL_START:
			epi = getJailStartEPI();
			break;
		case JAIL_PAY:
			gameClient.getOutOfJailByPayment(testOff);
			epi = getRollStartEPI(true);
			break;
		case JAIL_CARD:
			gameClient.getOutOfJailByCard(testOff);
			epi = getRollStartEPI(true);
			break;
		case JAIL_ROLL:
			epi = new EventPanelInfo();

			final int rollValue = throwDice();
			if (isDoubles()) {
				labelText = rb.getString("youRolled") + " " + getDiceValues()
						+ " " + rb.getString("outOfJail");
				buttonText = "ok";
				al = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("JailStatus:"
								+ gameClient.getCurrentPlayer().isInJail());
						gameClient.getOutOfJailByRoll(testOff);
						System.out.println("JailStatus:"
								+ gameClient.getCurrentPlayer().isInJail());
						epf.changePanel(Step.ROLL_NORMAL);
					}
				};
				epi.setText(labelText);
				epi.addActionListener(al);
				epi.addButtonText(buttonText);
			} else {
				if (attemptedRolls > 2)
					epi = rollFailureEPI();
				else {
					epi = getJailStartEPI();
				}
			}
			break;
		default:
			epi = new EventPanelInfo();
			labelText = "No case defined";
			buttonText = "ok";
			al = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				}
			};
			epi.setText(labelText);
			epi.addActionListener(al);
			epi.addButtonText(buttonText);
			break;
		}
		return epi;
	}

	public EventPanelInfo rollFailureEPI() {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi = new EventPanelInfo();
		
		epi = new EventPanelInfo();
		labelText = rb.getString("rollFailure");

		buttonText = "ok";
		al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.sendTransactionSuccesToGUI(testOff);
				epf.disableAfterClick();
			}
		};
		epi.setText(labelText);
		epi.addActionListener(al);
		epi.addButtonText(buttonText);
		return epi;
	}

	public EventPanelInfo getRollStartEPI(boolean freed) {
		String labelText;
		String buttonText;
		ActionListener al;
		EventPanelInfo epi = new EventPanelInfo();

		if (freed)
			labelText = rb.getString("freed");
		else
			labelText = rb.getString("rollDescription");

		buttonText = rb.getString("roll");
		al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				epf.changePanel(Step.ROLL_NORMAL2);
			}
		};
		epi.setText(labelText);
		epi.addActionListener(al);
		epi.addButtonText(buttonText);
		return epi;
	}

	public EventPanelInfo getJailStartEPI() {
		String labelText;

		EventPanelInfo epi = new EventPanelInfo();
		if (attemptedRolls > 0) {
			labelText = rb.getString("youRolled") + getDiceValues() + " "
					+ rb.getString("rollAgain") + " " + (3 - attemptedRolls)
					+ " " + rb.getString("triesRemaining");
		} else
			labelText = rb.getString("rollDescription");

		ActionListener pay = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.getOutOfJailByPayment(testOff);
				epf.changePanel(Step.JAIL_PAY);
				gameClient.sendTransactionSuccesToGUI(testOff);
			}
		};

		ActionListener card = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.getOutOfJailByCard(testOff);
				epf.changePanel(Step.JAIL_CARD);
				gameClient.sendTransactionSuccesToGUI(testOff);
			}
		};

		ActionListener rollJail = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				attemptedRolls++;
				epf.changePanel(Step.JAIL_ROLL);
				System.out.println("attempted Rolls: " + attemptedRolls);
			}
		};

		String buttonTextPay = rb.getString("pay");
		String buttonTextCard = rb.getString("card");
		String buttonTextRoll = rb.getString("roll");

		epi.setText(labelText);
		epi.addActionListener(pay);
		epi.addActionListener(card);
		epi.addActionListener(rollJail);
		epi.addButtonText(buttonTextPay);
		epi.addButtonText(buttonTextCard);
		epi.addButtonText(buttonTextRoll);
		return epi;
	}

	public JPanel getJailStartTurnPanel(boolean freed) {
		jpanel = new EventPanelFactory().getJPanel();
		attemptedRolls = 0;
		al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				attemptedRolls++;
				jailRollSecondStep();
				System.out.println("attempted Rolls: " + attemptedRolls);
			}
		};

		ActionListener pay = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.getOutOfJailByPayment(testOff);
				gameClient.sendTransactionSuccesToGUI(testOff);
			}
		};

		ActionListener card = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gameClient.getOutOfJailByCard(testOff);
				gameClient.sendTransactionSuccesToGUI(testOff);
			}
		};
		JButton buttonPay = new JButton(rb.getString("pay"));
		buttonPay.addActionListener(pay);
		JButton buttonCard = new JButton(rb.getString("card"));
		buttonCard.addActionListener(card);
		buttonRight.setText("Roll");
		buttonRight.addActionListener(al);

		descriptionLabel.setText(rb.getString("inJail"));

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(buttonPay);
		buttonPanel.add(buttonCard);
		buttonPanel.add(buttonRight);

		jpanel.add(descriptionLabel, BorderLayout.NORTH);
		jpanel.add(buttonPanel, BorderLayout.SOUTH);
		return jpanel;
	}

	public void jailRollSecondStep() {
		buttonRight.setText("ok");
		final int roll = throwDice();
		if (isDoubles()) {
			descriptionLabel.setText(rb.getString("youRolled")
					+ getDiceValues() + rb.getString("outOfJail"));
			buttonRight.removeActionListener(al);
			al = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("JailStatus:"
							+ gameClient.getCurrentPlayer().isInJail());
					gameClient.getOutOfJailByRoll(testOff);
					gameClient.sendTransactionSuccesToGUI(testOff);
					System.out.println("JailStatus:"
							+ gameClient.getCurrentPlayer().isInJail());
					buttonRight.removeActionListener(al);
					jpanel = gameClient.getStartTurnPanel(testOff);
				}
			};
			buttonRight.addActionListener(al);
		} else {
			if (attemptedRolls > 2)
				rolledUnsuccessfully();
			descriptionLabel.setText(rb.getString("youRolled")
					+ getDiceValues() + " " + rb.getString("rollAgain") + " "
					+ (3 - attemptedRolls) + " "
					+ rb.getString("triesRemaining"));

		}
	}

	public void rolledUnsuccessfully() {
		jpanel.remove(buttonRight);
		jpanel.add(new JLabel("rolledUnsuccessfully"), BorderLayout.CENTER);
		descriptionLabel.setText(rb.getString("youRolled") + getDiceValues()
				+ " " + (3 - attemptedRolls) + " "
				+ rb.getString("triesRemaining") + rb.getString("stayJail"));
		gameClient.sendTransactionSuccesToGUI(testOff);
	}

}
