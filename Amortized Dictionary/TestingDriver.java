/**
 ******************************************************************************
 *                    HOMEWORK, 15-351/650
 ******************************************************************************
 *                    Amortized Dictionary
 ******************************************************************************
 *
 * A diagnostic class for your Dictionary implementation.
 *
 * Make sure you perform much more rigorous testing!!!
 * This class alone is *NOT* sufficient.
 *
 * This class uses Java Exceptions and Reflections to
 * emulate the functionality of JUnit testing.
 *
 *****************************************************************************/


public class TestingDriver
{
	public static void simpleAddTest()
	{
		try
		{

			Dictionary<Integer> d = new Dictionary<Integer>();
			d.add(1);
			if(!d.toString().equals("1: [1]\n"))
			{
				throw new Exception();
			}
			d.add(2);
			if(!d.toString().equals("2: [1, 2]\n"))
			{
				throw new Exception();
			}
			d.add(3);
			if(!d.toString().equals("1: [3]\n2: [1, 2]\n"))
			{
				throw new Exception();
			}
			d = new Dictionary<Integer>();
			for(int n = 16; n >= 1; n--)
			{
				d.add(n);
			}
			if(!d.toString().equals("16: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]\n"))
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException("Your add(e) definitely isn't working.");
		}
	}


	public static void simpleContainsTest()
	{
		try
		{
			Dictionary<Integer> d = new Dictionary<Integer>();
			if(d.contains(1) != false)
			{
				throw new Exception();
			}
			d.add(1);
			if(d.contains(1) != true || d.contains(2) != false)
			{
				throw new Exception();
			}
			d.add(2);
			if(d.contains(1) != true || d.contains(2) != true || d.contains(3) != false)
			{
				throw new Exception();
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException("Your contains(e) definitely isn't working.");
		}
	}


	public static void simpleSizeTest()
	{
		try
		{
			Dictionary<Integer> d = new Dictionary<Integer>();
			for(int n = 1; n <= 100; n++)
			{
				if(d.size() != n-1)
				{
					throw new Exception();
				}
				d.add(n);
			}
		}
		catch(Exception e)
		{
			throw new RuntimeException("Your size() definitely isn't working.");
		}
	}


	public static void main(String[] args)
	{
		boolean passedAllTests = true;

		Class<?> c = null;
		try
		{
			c = Class.forName("TestingDriver");
		}
		catch (ClassNotFoundException e1)
		{
			System.out.println("Something is wrong with your computer ... TestingDriver.java cannot be accessed.");
		}

		for(java.lang.reflect.Method m : c.getDeclaredMethods())
		{
			if(m.getName().indexOf("Test") == -1)
			{
				continue;
			}

			try
			{
				m.invoke(null);
			}
			catch(Exception e)
			{
				System.out.println(e.getCause().getMessage());
				passedAllTests = false;
			}
		}

		System.out.println();
		if(passedAllTests)
		{
			System.out.println("Looks good!  Make sure you do more rigorous testing.");
		}
		else
		{
			System.out.println("Try to fix the above problems.");
		}

	}
}
