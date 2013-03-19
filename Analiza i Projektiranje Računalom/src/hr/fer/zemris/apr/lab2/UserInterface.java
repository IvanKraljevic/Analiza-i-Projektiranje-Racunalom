package hr.fer.zemris.apr.lab2;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UserInterface {
	private JFrame frame;
	private JPanel panel;
	private JButton startB;
	private JButton browseB;
	private JPanel buttonPanel;
	private ButtonGroup algorithmGroup;
	private ButtonGroup functionGroup;

	private ArrayList<JRadioButton> algList = new ArrayList<JRadioButton>();
	private ArrayList<JRadioButton> funcList = new ArrayList<JRadioButton>();

	private Function function;
	private OptimizingMethod algorithm;

	public void setUpGUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		frame = new JFrame("Optimizing algorithms");
		
		JRadioButton hookJ = new JRadioButton("Hooke-Jeeves algorithm");
		JRadioButton box = new JRadioButton("Box algorithm");
		
		algorithmGroup = new ButtonGroup();
		algorithmGroup.add(hookJ);
		algorithmGroup.add(box);
		hookJ.setSelected(true);

		algList.add(hookJ);
		algList.add(box);

		JLabel algString = new JLabel("Odaberi algoritam:");

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(algString);
		buttonPanel.add(hookJ);
		buttonPanel.add(box);
		buttonPanel.add(new JSeparator());
		
		JLabel funcString = new JLabel("Odaberi funkciju:");
		JRadioButton f1 = new JRadioButton("f1(x,y) = 10*(x^2-y)^2+(1-x)^2");
		JRadioButton f2 = new JRadioButton("f2(x,y) = (x-4)^2 + 4(y-2)^2");
		JRadioButton f3 = new JRadioButton(
				"f3(x1,x2,x3,x4,x5) = (x1-p1)^2 + (x2-p2)^2 + ... + (x5-p5)^2 ");
		JRadioButton f4 = new JRadioButton(
				"f4(x,y) = |(x-y)*(x+y)| + (x^2+y^2)^0.5");

		functionGroup = new ButtonGroup();
		functionGroup.add(f1);
		functionGroup.add(f2);
		functionGroup.add(f3);
		functionGroup.add(f4);
		f1.setSelected(true);

		funcList.add(f1);
		funcList.add(f2);
		funcList.add(f3);
		funcList.add(f4);

		buttonPanel.add(funcString);
		buttonPanel.add(f1);
		buttonPanel.add(f2);
		buttonPanel.add(f3);
		buttonPanel.add(f4);

		startB = new JButton("Pokreni");
		startB.addActionListener(new StartButtonListener());


		buttonPanel.add(startB);

		frame.getContentPane().add(buttonPanel, BorderLayout.WEST);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButton selectedAlg = new JRadioButton();
			JRadioButton selectedFunc = new JRadioButton();
			for (JRadioButton b : algList) {
				if (b.isSelected()) {
					selectedAlg = b;
					break;
				}
			}
			for (JRadioButton b : funcList) {
				if (b.isSelected()) {
					selectedFunc = b;
					break;
				}
			}
			System.out.println(selectedAlg.getText());
			System.out.println(selectedFunc.getText());

		}

		void setFunction(String text) {

		}

		void setAlgorithm(String text) {
			if (text.startsWith("H")) {
			}
		}
	}


	public static void main(String[] args) {
		new UserInterface().setUpGUI();
	}

}
