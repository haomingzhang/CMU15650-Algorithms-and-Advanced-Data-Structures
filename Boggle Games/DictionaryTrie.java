/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *
 * This class implements the dictionary trie for the Boggle
 * application.  Objects of this class are used by the GUI
 * to handle text input.  The responses include returning
 * whether or not a word is valid.
 *
 *
 * @author
 * @date
 *****************************************************************************/
/*****************************************************************************

              IMPLEMENT THIS CLASS

*****************************************************************************/

import java.util.*;
import java.io.*;

public class DictionaryTrie implements TrieInterface {
	private int size;
	private TrieNode root = null;
	private HashSet<String> solutions;

	private class TrieNode {
		private char letter;
		private boolean isEnd;
		private TrieNode[] children;

		public TrieNode() {
			this.setLetter('0');
			this.setEnd(false);
			this.initChildren();
		}

		public TrieNode(char c, boolean b) {
			this.setLetter(c);
			this.setEnd(b);
			this.initChildren();
		}

		public char getLetter() {
			return letter;
		}

		public void setLetter(char letter) {
			this.letter = letter;
		}

		public boolean isEnd() {
			return isEnd;
		}

		public void setEnd(boolean isEnd) {
			this.isEnd = isEnd;
		}

		public TrieNode getChild(int index) {
			return this.children[index];
		}

		public void setChild(int index, TrieNode child) {
			this.children[index] = child;
		}

		public void initChildren() {
			this.children = new TrieNode[26];
			for (int i = 0; i < 26; i++) {
				children[i] = null;
			}
		}
	}

	private class BFSClass {
		public int i;
		public int j;
		private TrieNode before;
		private String path;
		private ArrayList<Integer> posPath;

		public BFSClass(int i, int j, TrieNode before, String path, ArrayList<Integer> posPath) {
			this.i = i;
			this.j = j;
			this.before = before;
			this.setPath(path);
			this.setPosPath(posPath);
		}

		public TrieNode getBefore() {
			return before;
		}

		public void setBefore(TrieNode before) {
			this.before = before;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public ArrayList<Integer> getPosPath() {
			return posPath;
		}

		public void setPosPath(ArrayList<Integer> posPath) {
			this.posPath = posPath;
		}
		
		public boolean hasPassed(int pos){
			for (int k : posPath){
				if (k == pos){
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Creates a Trie object with the given dictionary.
	 *
	 * @param dictionaryName
	 *            the name of the dictionary file
	 */
	public DictionaryTrie(String dictionaryName) {
		//System.out.println("construction called!");
		size = 0;
		root = new TrieNode();
		solutions = new HashSet<String>();
		try {
			FileReader fileReader = new FileReader(dictionaryName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.length() >= 1){
					this.add(line.toLowerCase());
				}
			}
			bufferedReader.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	/**
	 * Prunes a the set of strings possible on a given board by using BFS on the
	 * dictionary trie.
	 *
	 * @param board
	 *            the current game board
	 */
	public void boardBFS(String[] board) {
		//System.out.println("BFS called!");
		// init board
		int len = (int) Math.sqrt(board.length);
		//System.out.println("len:" + len);
		char[][] b = new char[len][len];
		//System.out.println("init board");
		int i, j;
		for (i = 0; i < len; i++) {
			for (j=0; j < len; j++){
				b[i][j] = board[i*len + j].toLowerCase().charAt(0);
			}
		}

		
		//System.out.println("start BFS");
		// start BFS from each position on the board
		for (int row = 0; row < len; row++) {
			for (int col = 0; col < len; col++) {
				Queue<BFSClass> q = new LinkedList<BFSClass>();
				
				//init used
				//System.out.println("init used");

				
				ArrayList<Integer> tmpPos = new ArrayList<Integer>();
				tmpPos.add(row * len + col);
				q.add(new BFSClass(row, col, this.root, b[row][col] + "", tmpPos));// start
																// node
				
				
				while (!q.isEmpty()) {
					BFSClass current = q.remove();
					
					TrieNode parent = current.getBefore();
					String path = current.getPath();
					char c = path.charAt(path.length() - 1);
					//System.err.println(path);
					int index = c - 'a';
					TrieNode thisNode = null;
					if ((thisNode = parent.getChild(index)) == null) {// no
																		// match
						continue;
					}

					if (thisNode.isEnd() && path.length() >= 3) {
						solutions.add(path);
					}

					for (int k = current.i - 1; k <= current.i + 1; k++) {
						for (int m = current.j - 1; m <= current.j + 1; m++) {
							if (k >= 0 && m >= 0 && k < len && m < len && !current.hasPassed(k*len+m)) {
								ArrayList<Integer> posPath = new ArrayList<Integer>(current.getPosPath());
								posPath.add(k*len+m);
								q.add(new BFSClass(k, m, thisNode, current.getPath() + b[k][m], posPath));
							}
						}
					}

				}
			}
		}

		/*
		 * for (i=0; i<len; i++){ for (j=0; j<len; j++){ int index = i * len +
		 * j; q.add(index); } }
		 */

	}

	/**
	 * Inserts a string into the trie.
	 *
	 * @param s
	 *            the string to check
	 */
	public void add(String s) {
		TrieNode current = root;
		for (int i = 0; i < s.length(); i++) {
			boolean isEnd = (i == s.length() - 1);
			char c = s.charAt(i);
			int index = c - 'a';
			if (current.getChild(index) != null) {
				current = current.getChild(index);
				if (isEnd && !current.isEnd()){
					current.setEnd(true);
					//size++;
				}
			} else {
				current.setChild(index, new TrieNode(c, isEnd));
				//if (isEnd){
				//	size++;
				//}
				current = current.getChild(index);
			}

		}
		size++;
		//System.err.println(size());
	}

	/**
	 * Checks to see if a string is in the trie. Also marks this string for
	 * later use by the valid method.
	 *
	 * @param s
	 *            the string to check
	 * @return true if the string is in the trie, false otherwise
	 */
	public boolean inDictionary(String s) {
		TrieNode current = root;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int index = c - 'a';
			current = current.getChild(index);
			if (current == null) {
				return false;
			}
		}

		if (!current.isEnd()) {
			return false;
		}

		return true;

	}

	/**
	 * Checks to see if a string is both in the trie and a valid word on the
	 * game board. This method is only guaranteed to work after calling
	 * boardBFS.
	 *
	 * @param s
	 *            the string to check
	 * @return true if the string is valid, false otherwise
	 */
	public boolean valid(String s) {
		return (solutions.contains(s));
	}

	/**
	 * Returns a collection of all the solutions for this given board. Only
	 * guaranteed to work after calling boardBFS.
	 *
	 * @return all the solutions for this board
	 */
	public ArrayList<String> allSolutions() {
		return new ArrayList<String>(solutions);
	}

	/**
	 * Returns the number of elements read into the trie from the intial
	 * dictionary.
	 *
	 * @return the size
	 */
	public int size() {
		return size;
	}
}
