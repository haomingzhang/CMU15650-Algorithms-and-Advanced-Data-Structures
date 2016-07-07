/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *
 *       This class implements the dictionary trie.
 *       The trie supports all 256 ASCII characters
 *       We suggest to use a HashMap in the trie nodes to store the children
 *
 *
 * @author
 * @date
 *****************************************************************************/
 /*****************************************************************************

               DESIGN and IMPLEMENT THIS CLASS

 *****************************************************************************/


import java.util.*;

import java.io.*;


public class DictionaryTrie
{
	private int size;
	private TrieNode root = null;
	private static TrieNode curr = null;
	private static int codeCount;
	private static final int MAX_CODECOUNT = 0xffff;

	
	
	private class TrieNode {
		private byte letter;
		private int code;
		private HashMap<Byte, TrieNode> children;

		
		
		//new root
		public TrieNode() {
			DictionaryTrie.codeCount = 0;
			this.setChildren(new HashMap<Byte, TrieNode>());
			//init 0-255 dictionary
			byte b;
			for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++){
				b = (byte) i;
				//System.out.print((char)b);
				this.children.put(b, new TrieNode(b, DictionaryTrie.codeCount));
				DictionaryTrie.codeCount++;
			}
		}

		//new node
		public TrieNode(byte b, int code) {
			this.setLetter(b);
			this.setCode(code);
			this.setChildren(new HashMap<Byte, TrieNode>());
		}

		public byte getLetter() {
			return letter;
		}

		public void setLetter(byte letter) {
			this.letter = letter;
		}

		public boolean hasChild(byte b){
			return this.children.containsKey(b);
		}

		public TrieNode getChild(byte b) {
			if (this.hasChild(b)){
				return children.get(b);
			} else {
				return null;
			}
		}
		
		public TrieNode addChild(byte b, int code) {
			TrieNode child = new TrieNode(b, code);
			this.children.put(b, child);
			return child;
		}
		

		public void setChildren(HashMap<Byte, TrieNode> children) {
			this.children = children;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}


	}

	/**
	 * Creates a Trie object with the given dictionary.
	 *
	 * @param dictionaryName
	 *            the name of the dictionary file
	 */
	public DictionaryTrie() {
		//System.out.println("construction called!");
		codeCount = 0;
		size = 0;
		root = new TrieNode();
		curr = root;
	}

	/*
	 * return -1 if the next byte exists
	 * return code of current leaf if need to build a new child
	 */
	
	public int moveNext(byte b){
		if (curr.hasChild(b)){
			curr = curr.getChild(b);
			return -1;
		} else {
			curr.addChild(b, codeCount);
			codeCount++;
			this.size++;
			if (codeCount > DictionaryTrie.MAX_CODECOUNT){
				throw new IllegalArgumentException("codeword overflows!");
			}
			int code = curr.getCode();
			curr = root.getChild(b);
			return code;
		}
	}
	
	public int getCurrCode(){
		return curr.getCode();
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
