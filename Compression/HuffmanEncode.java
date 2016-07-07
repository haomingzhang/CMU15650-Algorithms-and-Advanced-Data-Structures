/**
 ******************************************************************************
 *                    HOMEWORK     15-351/650      COMPRESSION
 ******************************************************************************
 *
 *   This implements the Huffman encoding algorithm
 *
 *
 * @author
 * @date
 *****************************************************************************/
 /*****************************************************************************

                         IMPLEMENT THIS CLASS

 *****************************************************************************/

import java.io.*;
import java.util.*;


public class HuffmanEncode
{
	/** Code bit for a leaf node in file-based tree representation */
   public static final int LEAF = 0;

	/** Code bit for a parent node in file-based tree representation */
   public static final int PARENT = 1;

	/** Code bit for the left child in the file-based tree representation */
   public static final int LEFT = 0;

	/** Code bit for the right child in the file-based tree representation */
   public static final int RIGHT = 1;

	/** the root of the Huffman tree    */
	private HuffmanNode root;

	/** it stores chars and their frequences    */
	private Map<Byte, Integer> freqMap;

	/** it stores bytes and the encoding */
	private Map<Byte, int[]> encoding;

	/**
	*  Reads-in the input file, compresses it and writes to the output file
	*/
	public void encode(BitReader reader, BitWriter writer)
	{
		int fileBytes = reader.length();
		if (fileBytes == 0) return;

		countFrequencies(reader);
		HuffmanTree(freqMap);
		try{ writeHeader(writer); } catch(IOException e) {}
		writer.writeInt(fileBytes);

		reader.reset();

		for (int i = 0; i < fileBytes; i++)
			encode((byte) reader.readByte(), writer);

		writer.flush();
	}


	/**
	* This method takes a item and writes the corresponding codeword.
	* The bits <tt>LEFT</tt> and <tt>RIGHT</tt> are written so that
	* if one takes that path in the Huffman tree they will get to the
	* leaf node representing <tt>item</tt>.
	*/
	public void encode(Byte item, BitWriter writer)
	{
		int[] code = encoding.get(item);
		for (int bit : code){
			writer.writeBit(bit);
		}
		
	}


	/**
	*  Calculates frequences of each character from the ASCII table
	*/
	public Map<Byte, Integer> countFrequencies(BitReader reader)
	{
		this.freqMap = new HashMap<Byte, Integer>();
		int c;
		while ((c = reader.readByte()) >= 0){
			byte ch = (byte) c;
			//System.out.print((char)ch);
			if (freqMap.containsKey(ch)){
				freqMap.put(ch, freqMap.get(ch) + 1);
			} else {
				freqMap.put(ch, 1);
			}
			
		}
		
		return freqMap;
		
	}

	/**
	* Takes a list of (Byte, Frequency) pairs (here represented as a map)
	* and builds a tree for encoding the data items using the Huffman
	* algorithm.
	*/
	public void HuffmanTree (Map<Byte, Integer> map)
	{
		PriorityQueue<HuffmanNode> q = new PriorityQueue<HuffmanNode>();
		//add all entries
		for(Map.Entry<Byte, Integer> entry : map.entrySet()){
			//System.out.println(((char)(byte)entry.getKey()) + ": " + entry.getValue());
			HuffmanNode node = new HuffmanNode(entry.getKey(),entry.getValue());
			q.add(node);
		}
		
		while (q.size() > 1){
			HuffmanNode left = q.remove();
			HuffmanNode right = q.remove();
			HuffmanNode parent = new HuffmanNode(left, right);
			q.add(parent);
		}
		this.root = q.remove();
		
		//traverse the tree
		this.encoding = new HashMap<Byte, int[]> ();
		traverseTree(root, new ArrayList<Integer>());
		
		//debug
		/*
		for(Map.Entry<Byte, int[]> entry : this.encoding.entrySet()){
			System.out.print(((char)(byte)entry.getKey()) + ": ");
			for (int k : entry.getValue()){
				System.out.print(k);
			}
			System.out.println();
		}
		*/
	}

	private void traverseTree(HuffmanNode curr, ArrayList<Integer> path){
		
		if (curr.isLeaf()){
			int[] code = new int[path.size()];
			int i = 0;
			for (int p : path){
				code[i] = p;
				i++;
			}
			this.encoding.put(curr.getValue(), code);
		} else {
			ArrayList<Integer> leftPath = new ArrayList<Integer>(path);
			ArrayList<Integer> rightPath = new ArrayList<Integer>(path);
			leftPath.add(LEFT);
			rightPath.add(RIGHT);
			traverseTree(curr.getLeft(), leftPath);
			traverseTree(curr.getRight(), rightPath);
		}
	}
	
	/**
	* Writes the Huffman tree into a compressed file.
	*
	* The format for the tree is defined recursively. To write
	* the entire tree, you start with the root. When the node
	* is a leaf node, you write the bit <tt>LEAF</tt>
	* and then call the <tt>writeByte</tt> to write the node value.
	* Otherwise, you write the bit <tt>PARENT</tt>, then
	* go to the left and right nodes.
	*/
	public void writeHeader(BitWriter writer) throws IOException
	{
		
		traverseWrite(this.root, writer);
	}
	
	private void traverseWrite(HuffmanNode curr, BitWriter writer){
		
		if (curr.isLeaf()){
			//System.out.print(LEAF);
			//System.out.print((char)curr.getValue());
			writer.writeBit(LEAF);
			writer.writeByte(curr.getValue());
		} else {

			//System.out.print(PARENT);
			//System.out.print((char)curr.getValue());
			writer.writeBit(PARENT);
			traverseWrite(curr.getLeft(), writer);
			traverseWrite(curr.getRight(), writer);
		}
	}

	/**
	 * For testing purposes
	 * DO NOT CHANGE!
	 */
	public HuffmanNode getCodeTreeRoot()
	{
		return root;
	}

}
