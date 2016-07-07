/**
 ******************************************************************************
 *                    HOMEWORK     15-351/650      COMPRESSION
 ******************************************************************************
 *
 *   This implements the Huffman decoding algorithm
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


public class HuffmanDecode
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

	/**
	*  Reads-in the input file, decodes it and writes to the output file
	*/
	public void decode(BitReader reader, BitWriter writer)
	{
		if (reader.length() == 0) return;

		HuffmanTree(reader);
		int fileBytes = 0;
		try{ fileBytes = reader.readInt(); } catch(EOFException e) {}

		for (int i = 0; i < fileBytes; i++)
			writer.writeByte((byte) decode(reader));

		writer.flush();
	}


	/**
	* Reads the header from a compressed file and builds the Huffman tree for decoding.
	*
	*/
	public void HuffmanTree(BitReader br)
	{
		root = traverseBuild(br);
		
	}
	
	public HuffmanNode traverseBuild (BitReader br){
		if (br.readBit() == PARENT){//parent
			//System.out.print(PARENT);
			HuffmanNode l = traverseBuild(br);
			HuffmanNode r = traverseBuild(br);
			return new HuffmanNode(l, r);
		} else {//leaf
			byte ch = (byte) br.readByte();
			//System.out.print("0" + (char)ch);
			return new HuffmanNode(ch, 0);
		}
		
	}

	/**
	 * This method reads bits from the reader and traverse the Huffman tree
	 * to get the value stored in a leaf.
	 */
	public Byte decode (BitReader r)
	{
		int bit;
		HuffmanNode curr = root;
		while (!curr.isLeaf()){
			bit = r.readBit();
			switch(bit){
				case LEFT:
					curr = curr.getLeft();
					break;
				case RIGHT:
					curr = curr.getRight();
					break;
			}
		}
		//System.out.print((char)curr.getValue());
		return curr.getValue();
		
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
