/**
 ******************************************************************************
 *                    HOMEWORK     115-351/650       COMPRESSION
 ******************************************************************************
 *
 *   This implements the LZW algorithm
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


public class LZW
{

	/** The number of bits in each LZW code */
   public static final int BIT_WIDTH = 16;

	/** The maximum number of codes inthe dictionary */
   public static final int MAX_SIZE = (1 << BIT_WIDTH) - 1;
   
   private static DictionaryTrie trie = new DictionaryTrie();

   private static HashMap<Integer, byte[]> map = new HashMap<Integer, byte[]>();
   
   private static int codeCount = 0;
   /**
   *  Reads-in the input file, compresses it and writes to the output file
   */
   public void encode(BitReader in, BitWriter out)
	{
		byte ch;
		int code;
		while ((ch = (byte)in.readByte()) >= 0){
			//System.err.print((char)ch);
			code = trie.moveNext(ch);
			if (code >= 0) {
				//System.err.println(code);
				out.writeBits(code, BIT_WIDTH);
			}
		}
		//System.err.println(trie.getCurrCode());
		out.writeBits(trie.getCurrCode(), BIT_WIDTH);

		// don't forget to flush
		out.flush();
	}

   /**
   *  Reads-in the input file, decodes it and writes to the output file
   */
	public void decode(BitReader in, BitWriter out)
	{
		//initialize
		codeCount = 0;
		for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++){
			byte[] array = new byte[1];
			array[0] = (byte)i;
			map.put(codeCount, array);
			codeCount++;
		}
		
		int code;
		
		byte[] last = null;
		while ((code = in.readBits(BIT_WIDTH)) >= 0){
			if (map.containsKey(code)){
				byte[] array = map.get(code);
				if (last != null){
					byte[] newEntry = new byte[last.length+1];
					for (int i=0; i < last.length; i++){
						newEntry[i] = last[i];
					}
					newEntry[last.length] = array[0];
					map.put(codeCount, newEntry);
					codeCount++;
				}
				last = array;
				
				//debug
				/*
				for (byte b : array){
					System.err.print((char)b);
				}
				*/
				out.writeBytes(array);
			} else {
				if (code != codeCount){//error
					System.err.println("Code found: " + code);
					System.err.println("code count: " + codeCount);
					System.exit(1);
				}
				byte[] newEntry = new byte[last.length+1];
				for (int i=0; i < last.length; i++){
					newEntry[i] = last[i];
				}
				newEntry[last.length] = last[0];
				map.put(codeCount, newEntry);
				codeCount++;
				
				//debug
				/*
				for (byte b : newEntry){
					System.err.print((char)b);
				}
				*/
				last = newEntry;
				out.writeBytes(newEntry);
			}
			
		}

		// don't forget to flush!
		out.flush();
	}

   public static void main(String[] args) throws FileNotFoundException
   {
	  
      BitReader reader = new BitReader("LZW.java");
      BitWriter writer = new BitWriter("out.dat");
      LZW lzw = new LZW();
      lzw.encode(reader, writer);
      //System.err.println();
      reader = new BitReader("out.dat");
      writer = new BitWriter("out.java");
      lzw.decode(reader, writer);
   }
}
