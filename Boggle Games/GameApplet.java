/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *
 * This is an interactive graphical application for the Boggle game.
 *
 *
 * @author	V.Adamchik
 * @date		2/1/2016
 *****************************************************************************/
 /*****************************************************************************

               There is no need to modify this file.

 *****************************************************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.util.*;
import java.net.*;
import java.io.*;


public class GameApplet extends JApplet implements ActionListener, Runnable
{
	private final int TIMER = 180; // sec
   public static JTextArea textArea;
	public static JFormattedTextField timer;
	public static JCheckBox checkTimer;

   private JTextArea textArea2;
   private boolean sortFlag = true, sortFlag2 = true;
	private Thread clockThread;
	private JProgressBar progressBar;
	private JFormattedTextField score;
	private JButton newBoard, nextWord, autoSolve, sortHuman, sortComp;
	private HashSet<String> foundWords;  // Found word must be distinct
	private HashSet<String> dictionary;
   private BoggleBoard board;


   public void init()
   {

		// Reads the dictionary and store it in the hash table
		readDictionary();

      setSize(550, 350);
		foundWords = new HashSet<String>();


   		/* Main panel. It fits all componenets */
		JPanel main = new JPanel();
		main.setBackground(Color.white);
		main.setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(1,1,0,0),
			BorderFactory.createLineBorder (Color.black, 2) ) );
		main.setLayout (new BoxLayout(main, BoxLayout.X_AXIS));



			/* Create the menu bar and the menus */
		JMenuBar menubar = new JMenuBar();
		menubar.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(1,1,0,0),
			BorderFactory.createLineBorder (Color.black, 2) ) );
		//menubar.setBorder(BorderFactory.createRaisedBevelBorder());

		menubar.setBackground(Color.white);
		setJMenuBar(menubar);
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setFont(new Font("Arial", Font.PLAIN, 18));
		menubar.add(helpMenu);



			/* Create menu items for "Help" */
		JMenuItem instr = new JMenuItem("Instructions");
		instr.setAccelerator( KeyStroke.getKeyStroke("H") );
		instr.addActionListener(this);
		helpMenu.add(instr);


			/* Create the score and timer windows. */
   	JPanel timerPanel = new JPanel();
		timerPanel.setBackground(Color.white);
		timerPanel.setBorder(BorderFactory.createEmptyBorder(1,2,1,2));

		score = new JFormattedTextField();
		score.setValue("0");
		score.setFont(new Font("Arial", Font.BOLD, 14));
		score.setColumns(2);
		score.setForeground(Color.blue);
		score.setBackground(Color.white);
		score.setEditable(false);
		JLabel label1 = new JLabel("Score: ");
		label1.setFont(new Font("Arial", Font.PLAIN, 18));
		label1.setLabelFor(score);
		timerPanel.add(label1);
		timerPanel.add(score);


		timer = new JFormattedTextField();
		timer.setValue(TIMER);
		timer.setFont(new Font("Arial", Font.BOLD, 14));
		timer.setColumns(2);
		timer.setForeground(Color.blue);
		timer.setBackground(Color.white);
		timer.setEditable(false);
		JLabel label2 = new JLabel("Timer: ");
		label2.setFont(new Font("Arial", Font.PLAIN, 18));
		label2.setLabelFor(timer);
      checkTimer = new JCheckBox("");
		checkTimer.setBackground(Color.white);
		checkTimer.setForeground(Color.blue);
      checkTimer.setSelected(true);
      checkTimer.addItemListener(new CheckBoxListener());

		timerPanel.add(label2);
 		timerPanel.add(timer);
      timerPanel.add(checkTimer);


		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setBorderPainted(true);
		progressBar.setBackground(new Color(191,239,255));
		progressBar.setForeground(new Color(186,186,186));


   	timerPanel.add(new JLabel("  "));
		timerPanel.add(progressBar);

		menubar.add(timerPanel);



   		/* Create the board and buttons window. */
		JPanel buttonAndboard = new JPanel();
		buttonAndboard.setBackground(Color.white);
		buttonAndboard.setLayout (new BoxLayout(buttonAndboard, BoxLayout.Y_AXIS));

		// create buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));
		buttonPanel.setBackground(Color.white);
		buttonPanel.setLayout (new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		autoSolve = new JButton("Solve");
		newBoard = new JButton("New Board");
		nextWord = new JButton("Next Word");

		autoSolve.addActionListener(this);
		nextWord.addActionListener(this);
		newBoard.addActionListener(this);
		buttonPanel.add(autoSolve);
		buttonPanel.add(newBoard);
		buttonPanel.add(nextWord);

   		/* Game board */
   	//String[] testBoard = {"J","T","L","T","A","D","O","E","L","N","O","O","U","A","L","F"};
		//board = new BoggleBoard(testBoard);
		board = new BoggleBoard();
		board.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(30,30,10,30),
					BorderFactory.createLineBorder (Color.black, 2) ) );

		buttonAndboard.add(board);
		buttonAndboard.add(buttonPanel);


   		/* Create the textArea and sort buttons . */
		JPanel textAndsort = new JPanel();
		textAndsort.setBackground(Color.white);
		textAndsort.setLayout (new BoxLayout(textAndsort, BoxLayout.Y_AXIS));



   		/* HUMAN and COMPUTER panel */
		JPanel sortPanel = new JPanel();
		sortPanel.setBorder(BorderFactory.createEmptyBorder(5,15,10,5));
		sortPanel.setBackground(Color.white);
		sortPanel.setLayout (new BoxLayout(sortPanel, BoxLayout.X_AXIS));

		sortHuman = new JButton("Human");
		sortComp = new JButton("Computer");
		sortPanel.add(sortHuman);
   	sortPanel.add(new JLabel("           "));
		sortPanel.add(sortComp);
		sortHuman.addActionListener(this);
		sortComp.addActionListener(this);


  		/* found WORDS panel */
		JPanel textPanel = new JPanel();
		textPanel.setBorder(BorderFactory.createEmptyBorder(5,5,10,5));
		textPanel.setBackground(Color.white);
		textPanel.setLayout (new GridLayout(1,2,8,20));

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.PLAIN, 18));
		textArea.setBackground(Color.white);


		JScrollPane areaScrollPane = new JScrollPane(textArea);
		areaScrollPane.setPreferredSize(new Dimension(100, 10));
		areaScrollPane.setBackground(Color.white);
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(1,1,20,1),
					BorderFactory.createLineBorder (Color.black, 2) ) );


		textPanel.add(areaScrollPane);

		textArea2 = new JTextArea();
		textArea2.setEditable(false);
		textArea2.setFont(new Font("Arial", Font.PLAIN, 18));
		textArea2.setBackground(Color.white);

		JScrollPane areaScrollPane2 = new JScrollPane(textArea2);
		areaScrollPane2.setPreferredSize(new Dimension(100, 10));
		areaScrollPane2.setBackground(Color.white);
		areaScrollPane2.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createEmptyBorder(1,1,20,1),
					BorderFactory.createLineBorder (Color.black, 2) ) );

		textPanel.add(areaScrollPane2);

		textAndsort.add(sortPanel);
		textAndsort.add(textPanel);

		main.add(buttonAndboard);
		main.add(textAndsort);

		getContentPane().add(main);
   }


 /**
	*   Actions handler
	*
	*/
   public void actionPerformed(ActionEvent e)
   {
		String command = e.getActionCommand();

		if(command.equals("New Board"))
		{
			start();
			board.shakeIt();
			textArea.setText("");
			textArea2.setText("");
			timer.setValue(TIMER);
			progressBar.setValue(0);
			score.setValue("0");
			board.draw();
		}
		else if(command.equals("Next Word") && Integer.parseInt(timer.getText())>=0)
		{
			try
			{
				int line = Integer.parseInt(score.getText());

				String word = textArea.getText().substring(textArea.getLineStartOffset(line),
				                      textArea.getLineEndOffset(line));

				if(word.length() > 2 && dictionary.contains(word) && foundWords.add(word))
				{
					score.setText((line + 1) + "");
					textArea.append("\n");
				}
				else
				{
					textArea.replaceRange("",textArea.getLineStartOffset(line),
				                      textArea.getLineEndOffset(line));
					textArea.setCaretPosition(textArea.getLineStartOffset(line));
				}

			}
			catch(BadLocationException ex)
			{
			}
			board.draw();
		}
		else if (command.equals("Solve"))
		{
			ArrayList<String> sol = board.solve();
			textArea2.setText("");

			if(sol.size() == 0)
			{
				textArea2.append("no solutions");
				textArea2.append("\n");
			}
			else
			for(String str: sol)
			{
				textArea2.append(str);
				textArea2.append("\n");
			}
			//score.setText(sol.size() + "");

			progressBar.setValue(0);
			timer.setValue(0);

			board.draw();

		}
		else if (command.equals("Human"))
		{
			String[] words = textArea.getText().split("\n");
			if(sortFlag)
				Arrays.sort(words);
			else
				sortByLexLength(words);

			sortFlag = !sortFlag;

			textArea.setText("");

			for(String str: words)
			{
				textArea.append(str);
				textArea.append("\n");
			}
		}
		else if (command.equals("Computer"))
		{
			String[] words = textArea2.getText().split("\n");
			if(sortFlag2)
				Arrays.sort(words);
			else
				sortByLexLength(words);

			sortFlag2 = !sortFlag2;

			textArea2.setText("");

			for(String str: words)
			{
				textArea2.append(str);
				textArea2.append("\n");
			}
			board.draw();
		}
		else if (command.equals("Instructions"))
		{
			JOptionPane.showMessageDialog(null,
			    "The word must be at least three letters long.\nThe word must be a common  English word.\nLetters in the word must be connected horizontally, vertically, or diagonally.\nEach letter may be used only once in a given word.");
		}

	}

	private void readDictionary()
	{
		dictionary = new HashSet<String>();

		Scanner in = null;
		try
		{
			URL url = new URL(getCodeBase(), "BoggleDictionary.txt");
			InputStream inStream = url.openConnection().getInputStream();

			in = new Scanner(inStream);

			while (in.hasNext())	 dictionary.add(in.next().toUpperCase());
		}
		catch (IOException ex)
		{
			System.exit(0);
		}

		if(in != null) in.close();
	}

	public void start()   //start this applet
	{
		if (clockThread == null)
		{
			clockThread = new Thread(this, "Clock");
			clockThread.start();
		}
	}

	public void run()   //this runs a thread
	{
		// loop terminates when clockThread is set to null in stop()
		while (clockThread != null)
		{
			repaint();
			try
			{
				clockThread.sleep(1000);
				if(checkTimer.isSelected())
				{
					int sec = Integer.parseInt(timer.getText());

					int progress = (int) (100 - ((sec-1)*100./TIMER));

					progressBar.setValue(progress);

					if(sec <= 0)
						clockThread = null;
					else
						timer.setValue(sec-1);
				}

			}
			catch (InterruptedException e)
			{}
		}
	}

	public void stop()   //stop this applet
	{
		clockThread = null;
		repaint();
	}

	private class CheckBoxListener implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			if (e.getSource() == checkTimer)
				if(checkTimer.isSelected())
				{
					progressBar.setValue(0);
					timer.setValue(TIMER);
				}
				else
				{
					progressBar.setValue(0);
					timer.setValue(0);
				}
		}
	}


  /**
   *  Sorts the hand by value.
   *
   */
   @SuppressWarnings("unchecked")
   private void sortByLexLength(String[] data)
   {
      Arrays.sort(data, new LexLengthSort());
   }

	private class LexLengthSort implements java.util.Comparator
	{
		public int compare(Object x, Object y)
		{
			int a = ((String) x).length();
			int b = ((String) y).length();

			if(a != b)
				return a - b;
			else
				return ((String) x).compareTo((String) y);
		}
	}

}

