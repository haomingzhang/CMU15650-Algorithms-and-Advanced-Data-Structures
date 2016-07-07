/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *
 * This is an interactive graphical application for the Boggle game.
 *
 *
 * @author	V.Adamchik
 * @date		2/2/2016
 *****************************************************************************/
 /*****************************************************************************

               There is no need to modify this file.

 *****************************************************************************/

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.io.*;
import java.awt.font.*;
import java.net.*;

public class BoggleBoard extends JPanel
{
	private String[] board = new String[16];
	private LinkedHashSet<Integer> trackOfChars;  // Each letter may be used only once
	private DictionaryTrie trie = null;
	private boolean boardSolved = false;
	private Random rand = new Random();

	private Die[] dice = new Die[] {
			new Die(new char[]{'L','R','Y','T','T','E'}),
			new Die(new char[]{'V','T','H','R','W','E'}),
			new Die(new char[]{'E','G','H','W','N','E'}),
			new Die(new char[]{'S','E','O','T','I','S'}),
			new Die(new char[]{'A','N','A','E','E','G'}),
			new Die(new char[]{'I','D','S','Y','T','T'}),
			new Die(new char[]{'O','A','T','T','O','W'}),
			new Die(new char[]{'M','T','O','I','C','U'}),
			new Die(new char[]{'A','F','P','K','F','S'}),
			new Die(new char[]{'X','L','D','E','R','I'}),
			new Die(new char[]{'H','C','P','O','A','S'}),
			new Die(new char[]{'E','N','S','I','E','U'}),
			new Die(new char[]{'Y','L','D','E','V','R'}),
			new Die(new char[]{'Z','N','R','N','H','L'}),
			new Die(new char[]{'N','M','I','Q','H','U'}),
			new Die(new char[]{'O','B','B','A','O','J'}),
	};

	private JButton[] cell = new JButton[16];

   public BoggleBoard()
   {
		trackOfChars = new LinkedHashSet<Integer>(32);

		setBackground(Color.white);
		shakeIt();

		setLayout(new GridLayout(4,4,0,0));

		for(int k = 0; k < 16; k++)
		{
			cell[k] = new JButton();
			cell[k].setHorizontalAlignment(SwingConstants.CENTER);
			cell[k].setFont(new Font("Arial", Font.PLAIN, 24));
			cell[k].setBackground(Color.white);

			cell[k].setForeground(Color.black);
			cell[k].setText(board[k]);
			cell[k].addActionListener(new MyActionListener());

			//cell[k].setBorder(BorderFactory.createLineBorder (Color.lightGray, 2));

			cell[k].setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(1,1,0,0),
			BorderFactory.createLineBorder (Color.black, 1) ) );
			add(cell[k]);
		}
	}

   public BoggleBoard(String[] board)
   {
		this.board = board;
		trackOfChars = new LinkedHashSet<Integer>(32);
		setBackground(Color.white);

		setLayout(new GridLayout(4,4,0,0));

		for(int k = 0; k < 16; k++)
		{
			cell[k] = new JButton();
			cell[k].setHorizontalAlignment(SwingConstants.CENTER);
			cell[k].setFont(new Font("Arial", Font.PLAIN, 24));
			cell[k].setBackground(Color.white);

			cell[k].setForeground(Color.black);
			cell[k].setText(board[k]);
			cell[k].addActionListener(new MyActionListener());

			//cell[k].setBorder(BorderFactory.createLineBorder (Color.lightGray, 2));

			cell[k].setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(1,1,0,0),
			BorderFactory.createLineBorder (Color.black, 1) ) );
			add(cell[k]);
		}
	}


    class MyActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent evt)
        {
            String input = evt.getActionCommand();
				for (int k = 0; k < 16; k++)
      			if (evt.getSource() == cell[k])
      			{
         			if(trackOfChars.add(k) && connected() &&
         				(!GameApplet.checkTimer.isSelected() ||
         				  Integer.parseInt(GameApplet.timer.getText())>0))
         			{
         				cell[k].setBackground(new Color(191,239,255));
         				                        //204,204,255));//255,218,185));
         				GameApplet.textArea.append(cell[k].getText());
						}
					}
        }
    }

 /**
	*  This checks if LAST two characters are adjacent
	*
	*/
	public boolean connected()
	{
		if(trackOfChars.size() < 2) return true;

		int prev = 0, last = 0, count = 0;

		for(Integer n : trackOfChars)
		{
			if (count == (trackOfChars.size()-2) ) prev = n;
			else
			if (count == (trackOfChars.size()-1) ) last = n;

			count++;
		}

		//convert prev and last to 2d coordinate
		int row = prev/4;
		int col = prev%4;
		int[] elem = {last/4, last%4};

		int[][] validMoves = {{row,col-1}, {row,col+1}, {row-1,col}, {row+1,col},
		                    {row-1,col-1}, {row-1,col+1}, {row+1,col-1}, {row+1,col+1}};


		for(int[] k : validMoves)
			if(Arrays.equals(k, elem))	return true;

		trackOfChars.remove(last);
		return false;
	}

 /**
	*  This sets up a new random board
	*
	*/
	public void shakeIt()
	{
		boardSolved = false;

		ArrayList<Integer> randomOrder = new ArrayList<Integer>();

		// Get a random dice ordering with replacement
		for (int k = 0; k < 16; k++) randomOrder.add(k);

		Collections.shuffle(randomOrder);

		// Create the dice
		for (int k = 0; k < 16; k++)
			board[k] = dice[randomOrder.get(k).intValue()].getRandomFace() + "";
	}

 /**
	*  Writes letters to the Boggle panel
	*
	*/
	public void draw()
	{
		trackOfChars = new LinkedHashSet<Integer>(32);
		for(int k = 0; k < 16; k++)
		{
			cell[k].setForeground(Color.black);
			cell[k].setBackground(Color.white);
			cell[k].setText(board[k]);
		}
		repaint();
	}


 /**
	*  Returns the current board
	*
	*/
	public String[] getBoard()
	{
		String[] tmp = new String[board.length];
		int k = 0;
		for(String str : board)
			tmp[k++] = str.toLowerCase();

		return tmp;
	}


 /**
	*  Finds all words on the board
	*
	*/
	public ArrayList<String> solve()
	{
		try
		{
			//read the dictironary to the Trie
			if(trie == null)
				trie = new DictionaryTrie("BoggleDictionary.txt");

			//find all words using the BFS traversal
			if(!boardSolved)
			{
				trie.boardBFS(getBoard());
				boardSolved = true;
			}

			//return the list of words
			return  trie.allSolutions();
		}
		catch (RuntimeException e)
		{
			return new ArrayList<String>();
		}
	}


	/*****************************************************
	 *
	 *            Die class represents each die
	 *
	 ******************************************************/


	private class Die
	{
		char[] faces;

	 /**
		* Initializes this die with the specified faces.
		*
		*/
		public Die(char[] faces)
		{
			this.faces = faces;
		}

	 /**
		* Gets a face from this die at random (simulates rolling it).
		*/
		public char getRandomFace()
		{
			return faces[rand.nextInt(faces.length)];
		}
	}
}

