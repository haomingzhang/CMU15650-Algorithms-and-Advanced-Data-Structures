
/**
 ******************************************************************************
 *                    HOMEWORK, 15-351/650
 ******************************************************************************
 *                    Skip List
 ******************************************************************************
 *
 * Implementation of a skip list, introduced by William Pugh in his 1990 paper.
 *
 *
 * User ID(s):haomingz
 *
 *****************************************************************************/

import java.util.*;

public class Dictionary<E extends Comparable<E>> implements DictionaryInterface<E> {
	private Node<E> head;
	private int maxLevel;
	private int size;
	private Random rand;

	public static final boolean HEAD = true;

	/**
	 * Creates an empty dictionary and starts a random generator.
	 */
	public Dictionary() {
		size = 0;
		maxLevel = 0;
		rand = new Random();
		head = new Node<E>(null);
	}

	/**
	 * Creates an empty dictionary and starts a random generator.
	 */
	public Dictionary(int seed) {
		size = 0;
		maxLevel = 0;
		rand = new Random(seed);
		head = new Node<E>(null);
	}

	public class Node<E extends Comparable<E>> {
		private E data;
		private ArrayList<Node<E>> next;

		public Node(E data) {
			this.setData(data);
			this.setNext(new ArrayList<Node<E>>());
		}

		public E getData() {
			return data;
		}

		public void setData(E data) {
			this.data = data;
		}

		public ArrayList<Node<E>> getNext() {
			return next;
		}

		public void setNext(ArrayList<Node<E>> next) {
			this.next = next;
		}

		public int getNextNum() {
			return this.next.size();
		}

		public void delNext(int index) {
			this.next.remove(index);
		}
	}

	private ArrayList<Node<E>> search(E item) {
		ArrayList<Node<E>> pathNodes = new ArrayList<Node<E>>();
		int currLevel = this.maxLevel - 1;
		Node<E> node = this.head;
		Node<E> prev_node = this.head;
		// boolean found = false;// if true, node is the element, if false,
		// insert
		// after prev_node
		for (; currLevel >= 0; currLevel--) {
			node = prev_node;
			// System.out.print("currLevel: " + currLevel);
			// prev_node < item && node >= item
			while ((node != null) && (node.getData() == null || node.getData().compareTo(item) < 0)) {
				// System.out.print(" Node: " + node.getData() + " ");
				prev_node = node;
				node = node.getNext().get(currLevel);
			}
			/*
			 * if (node.getData().compareTo(item) == 0) { found = true; break; }
			 */
			pathNodes.add(0, prev_node);

		}
		return pathNodes;
	}

	@Override
	public void insert(E item) {
		int currLevel;
		ArrayList<Node<E>> pathNodes = null;
		ArrayList<Node<E>> prev_next = null;
		Node<E> node = new Node<E>(item);

		if (this.maxLevel == 0) {// first node
			this.maxLevel = 1;
			node.getNext().add(null);
			this.head.getNext().add(node);
		} else {
			// search
			pathNodes = this.search(item);
			// insert to lowest level
			currLevel = 0;
			prev_next = pathNodes.get(currLevel).getNext();
			node.getNext().add(prev_next.get(currLevel));
			prev_next.set(currLevel, node);
		}

		// recursively insert
		for (currLevel = 1; rand.nextBoolean(); currLevel++) {
			if (currLevel < maxLevel) {// use the existed precedent
				prev_next = pathNodes.get(currLevel).getNext();
				node.getNext().add(prev_next.get(currLevel));
				prev_next.set(currLevel, node);
			} else { // increase the maxLevel and append to the head
				if (currLevel != this.maxLevel) {
					System.err.println("currLevel != this.maxLevel");
					System.err.println("currLevel: " + currLevel + " maxLevel: " + maxLevel);
					System.exit(1);
				}

				maxLevel++;

				head.getNext().add(node);
				// System.out.println(head.getNextNum());
				node.getNext().add(null);
				// System.out.println(node.getNextNum());
			}
		}

		this.size++;
		// System.out.println("size: " + size + " maxLevel: " + maxLevel + "
		// item: " + item);

	}

	@Override
	public boolean contains(E item) {
		if (this.size == 0){
			return false;
		}
		Node<E> node = this.search(item).get(0).getNext().get(0);
		if (node == null) {
			return false;
		}
		return (node.getData().compareTo(item) == 0);
	}

	@Override
	public boolean delete(E item) {
		if (this.size == 0){
			return false;
		}
		ArrayList<Node<E>> pathNodes = this.search(item);
		Node<E> prev = pathNodes.get(0);
		Node<E> node = prev.getNext().get(0);
		if (node == null || node.getData().compareTo(item) != 0) {// nonexistent
			return false;
		}
		for (int currLevel = node.getNextNum() - 1; currLevel >= 0; currLevel--) {
			Node<E> next = node.getNext().get(currLevel);
			pathNodes.get(currLevel).getNext().set(currLevel, next);
			if (head.getNext().get(currLevel) == null) {// reduce level
				head.delNext(currLevel);
				this.maxLevel--;
			}
		}
		this.size--;
		return true;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public int maxLevel() {
		return this.maxLevel;
	}

	@Override
	public String toString() {
		if (this.size == 0){
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Node<E> node = head.getNext().get(0);
		while (node != null) {
			sb.append("(" + node.getData().toString() + "," + node.getNextNum() + ")");
			node = node.getNext().get(0);
		}
		return sb.toString();
	}

	/******************************************************************************
	 * Testing *
	 ******************************************************************************/

	public static void main(String[] args) {
		/* This will print the same dictionary on each run */
		Dictionary<Integer> testList = new Dictionary<Integer>(15);
		for (int k = 1; k <= 10; k++) {
			testList.insert(k);
		}
		System.out.print(testList.size() + " ");
		System.out.print(testList.maxLevel() + " ");
		System.out.println(testList);// print (value, level)
		int[] testK = { 5, 8, 1, 10, 4, 3, 6, 7, 9, 2};
		for (int k : testK) {
			testList.delete(k);
			System.out.print(testList.size() + " ");
			System.out.print(testList.maxLevel() + " ");
			System.out.println(testList);
		}
		for (int k : testK) {
			testList.insert(k);
			System.out.print(testList.size() + " ");
			System.out.print(testList.maxLevel() + " ");
			System.out.println(testList);
		}
		/*
		 * 10 8 (1,3)(2,1)(3,2)(4,1)(5,1)(6,2)(7,2)(8,3)(9,1)(10,8)
		 */
	}

}
