import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Utilities
{
	public static String[] makeList(String fileName)
	{
		Scanner fileScanner = makeFileScanner(fileName);
		if (fileScanner == null)
		{
			return null;
		}
		
		String[] list = new String[countEntries(makeFileScanner(fileName))];
		
		String current;
		int index = 0;
		
		while (fileScanner.hasNext())
		{
			current = fileScanner.nextLine();
			
			if (!current.isEmpty())
				list[index++] = current;
		}
		
		return list;
	}
	
	public static int countEntries(Scanner input)
	{
		int entries = 0;
		
		while (input.hasNext())
		{
			if (!input.nextLine().isEmpty())
				entries++;
		}
		
		return entries;
	}
	
	public static Scanner makeFileScanner(String fileName)
	{
		try 	{ return new Scanner(new File(fileName)); }
		catch 	(FileNotFoundException ex) { return null; }
	}
	
	public static PrintStream makeFilePrintStream(String fileName)
	{
		try
		{
			return new PrintStream(new File(fileName));
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Error: couldn't create output file named " + fileName);
		}
		
		return null;
	}
	
	public static String numberOCD(int x)
	{
		String suffix;
		
		if (x < 4 || x > 20)
		{
			switch (x % 10)
			{
			case 1:
				suffix = "st";
				break;
			case 2:
				suffix = "nd";
				break;
			case 3:
				suffix = "rd";
				break;
			default:
				suffix = "th";
			}
		}
		else
			suffix = "th";
		
		return suffix;
	}
	
	public static boolean inputYesNo(String prompt)
	{
		boolean done = false, result = false;
		Scanner input = new Scanner(System.in);
		String currentInput;
		
		while (!done)
		{
			System.out.print(prompt + " (y/n) ");
			currentInput = input.nextLine();
			
			if (currentInput.equals("y") || currentInput.equals("Y"))
			{
				done = true;
				result = true;
			}
			else if (currentInput.equals("n") || currentInput.equals("N"))
			{
				done = true;
				result = false;
			}
			else
				System.out.println("\tInvalid input.");
		}
		
		return result;
	}
	
	public static void swap(String[] list, int a, int b)
	{
		String temp = list[b];
		list[b] = list[a];
		list[a] = temp;
	}
	
	public static void swap(Boolean[] list, int a, int b)
	{
		Boolean temp = list[b];
		list[b] = list[a];
		list[a] = temp;
	}
	
	public static void printList(String[] list)
	{
		System.out.println("\nList:");
		for (int i = 0; i < list.length; i++)
		{
			System.out.println(list[i]);
		}
	}
	
	public static void printList(Boolean[] list)
	{
		System.out.println("\nList:");
		for (int i = 0; i < list.length; i++)
		{
			if (list[i])
				System.out.println("true");
			else
				System.out.println("false");
		}
	}
}