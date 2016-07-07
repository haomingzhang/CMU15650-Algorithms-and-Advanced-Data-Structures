/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *                     Dictionary interface
 ******************************************************************************
 *****************************************************************************

               Do not modify this file.

 *****************************************************************************/

public interface DictionaryInterface<AnyType extends Comparable<AnyType>>
{
	/**
	 * Adds item to the dictionary.
	 */
	public void add(AnyType item);


	/**
	 * Returns true if the dictionary contains an element equal to e,
	 * otherwise - false;
	 */
	public boolean contains(AnyType item);
}
