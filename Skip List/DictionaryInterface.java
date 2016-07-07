/**
 ******************************************************************************
 *                    HOMEWORK  15-351/650
 ******************************************************************************
 *                     Skip List Interface
 ******************************************************************************
 *****************************************************************************

               Do not modify this file.

 *****************************************************************************/

public interface DictionaryInterface<AnyType extends Comparable<AnyType>>
{
	/**
	 * Adds an item to the dictionary.
	 * The method allows duplicates.
	 */
	public void insert(AnyType item);


	/**
	 * Returns true if the dictionary contains an element,
	 * otherwise - false;
	 */
	public boolean contains(AnyType item);


	/**
	 * Deletes an item from the dictionary and returns true
	 * In case of duplicates, it deletes just one of them.
	 * if an item is not in the dictionary it returns false.
	 */
	public boolean delete(AnyType item);


	/**
	 * Returns the size of the dictionary.
	 */
	public int size();

	/**
	 * Returns the max level of the dictionary, counting the original one.
	 */
	public int maxLevel();

}
