import java.io.FileNotFoundException;

/**
 ******************************************************************************
 *                    HOMEWORK     15-351/650       COMPRESSION
 ******************************************************************************
 *
 *   The main class for the Huffman compression algorithm
 *
 *
 *****************************************************************************/


public class HuffmanMainDriver
{
	public static void main(String[] args) throws FileNotFoundException
	{
		HuffmanEncode hc = new HuffmanEncode();
		BitReader reader = new BitReader("HuffmanMainDriver.java");
		BitWriter writer = new BitWriter("out.dat");
		//System.out.println("Encode:");
		hc.encode(reader, writer);

		System.out.println();
		//System.out.println("Decode:");
		HuffmanDecode hd = new HuffmanDecode();
		reader = new BitReader("out.dat");
		writer = new BitWriter("out.txt");

		hd.decode(reader, writer);
	}
}