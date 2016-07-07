/**
 ******************************************************************************
 *                    HOMEWORK, 15-351/650
 ******************************************************************************
 *                    Amortized Dictionary
 ******************************************************************************
 *
 * Implementation of an Amortized Array-Based Dictionary data structure.
 *
 * This data structure supports duplicates and does *NOT* support storage of
 * null references.
 *
 * Notes:
 * 		-It is *highly* recommended that you begin by reading over all the methods,
 *       all the comments, and all the code that has already been written for you.
 *
 * 		-the specifications provided are to help you understand what the methods
 *       are supposed to accomplish.
 * 		-See the lab documentation & recitation notes for implementation details.
 *
 *
 * User ID(s): Haoming Zhang
 *
 *****************************************************************************/


import static java.util.Arrays.binarySearch;

import java.lang.reflect.Array;
import java.util.Arrays;


public class Dictionary<E extends Comparable<E>>  implements DictionaryInterface<E>
{
	/*
	 * Keeps track of the number of elements in the dictionary.
	 * Take a look at the implementation of size()
	 */
	private int size;
	/*
	 * The head reference to the linked list of Nodes.
	 * Take a look at the Node class.
	 */
	private Node head;

	/**
	 * Creates an empty dictionary.
	 */
	public Dictionary()
	{
		size = 0;
		head = null;
	}

	/**
	 * Adds item to the dictionary, thus making contains(item) true.
	 * Increments size to ensure size() is correct.
	 */
	public void add(E item)
	{
		if(item == null)
		{
			throw new NullPointerException("Error passing null object to add");
		}

		/*
		 * Your code goes here...
		 */
		Comparable[] newArray = new Comparable[1];
		newArray[0] = item;
		Node newHead = new Node(newArray, this.head);
		this.size++;
		this.head = newHead;
		this.mergeDown();
	}

	/**
	 * Starting with the smallest array, mergeDown() merges arrays of the same size together until
	 * all the arrays have different size.
	 *
	 * This is very useful for implementing add(item)!
	 */
	private void mergeDown()
	{
		/*
		 * Your code goes here...
		 */
		Node it = null;
		for (it = this.head; (it != null) && (it.getNext() != null);){
			Comparable[] array1 = it.getArray();
			Comparable[] array2 = it.getNext().getArray();
			if (array1.length == array2.length){// same length
				Comparable[] array3 = new Comparable[2 * array1.length];
				// merge two sorted array
				int index1 = 0;
				int index2 = 0;
				int index3 = 0;

				while ((index1 < array1.length) && (index2 < array2.length)){

					if (array1[index1].compareTo(array2[index2]) <= 0){//array1[m]<=array2[n]
						array3[index3] = array1[index1];
						index3++;
						index1++;
					} else {////array1[m]>array2[n]
						array3[index3] = array2[index2];
						index3++;
						index2++;
					}
				}

				while (index1 < array1.length){
					array3[index3] = array1[index1];
					index1++;
					index3++;
				}

				while (index2 < array2.length){
					array3[index3] = array2[index2];
					index2++;
					index3++;
				}

				it.setArray(array3);
				it.setNext(it.getNext().getNext());
			} else {
				it = it.getNext();
			}
		}

	}


	/**
	 * Returns true if the dictionary contains an element equal to item, otherwise- false.
	 * Use the method contains() in the Node class.
	 */
	public boolean contains(E item)
	{
		if(item == null)
		{
			throw new NullPointerException("Error passing null object to contain");
		}

		/*
		 * Your code goes here...
		 */

		Node it = null;
		for (it = this.head; it != null; it = it.getNext()){
			if (it.contains(item)){
				return true;
			}
		}

		return false;
	}


	/**
	 * Returns the size of the dictionary.
	 */
	public int size()
	{
		return size;
	}

	/**
	 * Returns a helpful string representation of the dictionary.
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		Node tmp = head;
		while(tmp != null)
		{
			sb.append( tmp.array.length + ": ");
			sb.append(tmp.toString());
			sb.append("\n");
			tmp = tmp.getNext();
		}
		return sb.toString();
	}



	/**
	 * Implementation of the underlying array-based data structure.
	 *
	 * You may add additional methods.
	 */
	@SuppressWarnings("unchecked")
	private static class Node
	{
		private Comparable[] array;
		private Node next;

		/**
		 * Creates an Node with the specified parameters.
		 */
		public Node(Comparable[] array, Node next)
		{
			this.array = array;
			this.next = next;
		}


		/**
		 * Returns	true, if there is an element in the array equal to item
		 * 			false, otherwise
		 */
		public boolean contains(Comparable item) {
			/*
			 * Your code goes here...
			 */
			int pos = Arrays.binarySearch(this.array, item);
			if ((pos >= 0) && (pos < this.array.length)) {
				if (this.array[pos].compareTo(item) == 0){
					return true;
				}
			}
			return false;
		}

		/**
		 * Returns a useful representation of this Node.  (Note how this is used by Dictionary's toString()).
		 */
		public String toString()
		{
			return java.util.Arrays.toString(array);
		}

		public Node getNext(){
			return this.next;
		}

		public void setArray(Comparable[] array){
			this.array = array;
		}

		public void setNext(Node next){
			this.next = next;
		}

		public Comparable[] getArray(){
			return this.array;
		}
	}

}


