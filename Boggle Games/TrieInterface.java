/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 * This class defines the interface to the dictionary trie for
 * the Boggle game.
 *
 *
 * @author  V. Adamchik
 * @date 	2/1/2016
 ******************************************************************************/
 /*****************************************************************************

               Do not modify this file.

 *****************************************************************************/

import java.util.*;


public interface TrieInterface {

	/**
	* Prunes the set of strings possible on a given
	* board by using BFS on the dictionary trie. Valid words on the game
	* board can fall to left, right, up, down, or on the immediate
	* diagonal from the given cube. Remember that one cannot use the
	* same cube twice in a given word.
	*
	* @param board the current game board
	*/
	public void boardBFS(String[] board);

	/**
	* Checks to see if a string is in the trie.
	*
	* @param s the string to check
	* @return true if the string is in the trie, false otherwise
	*/
	public boolean inDictionary(String s);

   /**
   * Inserts a string into the trie.
   *
   * @param s the string to check
   */
   public void add(String s);


	/**
	* Check to see if a string is both in the trie
	* and a valid word on the game board. This method is
	* only guaranteed to work after calling boardBFS.
	*
	* @param s the string to check
	* @return true if the string is valid, false otherwise
	*/
	public boolean valid(String s);

	/**
	* Returns a collection of all the solutions for this
	* given board.  Only guaranteed to work after calling
	* boardBFS.
	*
	* @return all the solutions for this board
	*/
	public ArrayList<String> allSolutions();

	/**
	* Returns the number of elements read into the trie from
	* the intial dictionary.
	*
	* @return the size
	*/
	public int size();
}
