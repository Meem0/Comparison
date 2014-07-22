import java.util.Scanner;
import java.io.PrintStream;

public class Comparer
{
	public static void main(String[] args)
	{
		String inputFileName = "Input.txt";
		String[] list = Utilities.makeList(inputFileName);
		if (list == null)
		{
			System.out.println("Error: input list called " + inputFileName + " not found.");
			System.exit(-1);
		}
		
		sort(list, 0, list.length - 1, new Saver(inputFileName));
		
		printResults(list);
	}
	
	public static void sort(String[] list, int i1, int i2, Saver saver)
	{
		String compareItem = list[i1];
		int splitIndex;
		
		if (!saver.isReading())
		{
			System.out.println("======================================================================");
			System.out.println("\tComparing " + compareItem);
			System.out.println("======================================================================\n");
			
			splitIndex = compareAndSort(list, i1, i2, saver);
			
			System.out.println("\nThat's it for " + compareItem + "!");
			System.out.println("Turns out it's your " + (splitIndex + 1)
							   + Utilities.numberOCD(splitIndex + 1) + " favourite!\n");
			
			if (splitIndex + 1 == i2)
				System.out.println("Also, " + list[i2] + " is your " + (i2 + 1)
								   + Utilities.numberOCD(i2 + 1) + " favourite!\n");
			if (splitIndex - 1 == i1)
				System.out.println("Also, " + list[i1] + " is your " + (i1 + 1)
								   + Utilities.numberOCD(i1 + 1) + " favourite!\n");
			
			switch (savePrompt())
			{
			case 1:
				saver.save();
				break;
			case 2:
				saver.save();
			case 3:
				System.exit(-1);
			}
			
			System.out.println();
		}
		else
		{
			splitIndex = sortPreferences(list, i1, saver.readNext());
		}
		
		if (splitIndex + 1 < i2)
			sort(list, splitIndex + 1, i2, saver);
		if (splitIndex - 1 > i1)
			sort(list, i1, splitIndex - 1, saver);
	}
	
	public static int compareAndSort(String[] list, int i1, int i2, Saver saver)
	{
		Boolean[] resultList = new Boolean[i2 - i1];
		int compareId = 1;
	
		for (int i = i1 + 1; i <= i2; i++)
		{
			resultList[i - i1 - 1] = choosePreferred(list, i1, i, compareId++);
		}
		
		revision(list, i1, resultList);
		
		saver.write(resultList);
		
		return sortPreferences(list, i1, resultList);
	}
	
	public static Boolean choosePreferred(String[] list, int item1, int item2, int choiceId)
	{
		System.out.println("== Choice " + choiceId + " ==");
		System.out.print("1) " + list[item1] + "\n2) " + list[item2] + "\n\tPreferred: ");
		return is1Preferred();
	}
	
	// compared item: list[i1]
	// preferences[0] -> list[i1 + 1]
	// preferences[length - 1] -> list[i2]
	public static int sortPreferences(String[] list, int i1, Boolean[] preferences)
	{
		int prefSearch = 0;
		boolean found;
	
		// loop through preferences
		for (int i = 1; i < preferences.length; i++)
		{
			// if the current item is better than compare item
			if (!preferences[i])
			{
				found = false;
				// find the first item worse than compare item (true)
				// swap it with the current item
				while (!found && prefSearch < i)
				{
					if (preferences[prefSearch])
					{
						Utilities.swap(preferences, prefSearch, i);
						Utilities.swap(list, prefSearch + i1 + 1, i + i1 + 1);
						found = true;
					}
					prefSearch++;
				}
			}
		}
		
		// in case the whole list was false
		if (prefSearch < preferences.length && !preferences[prefSearch])
		{
			prefSearch++;
		}
		
		Utilities.swap(list, i1, prefSearch + i1);
		
		return prefSearch + i1;
	}
	
	public static void revision(String[] list, int i1, Boolean[] preferences)
	{
		Scanner input = new Scanner(System.in);
		String currentInput;
		boolean done = false;
		int choice = 0;
		
		while (!done)
		{
			System.out.print("Input a choice number you want to change or a blank line to continue: ");
			currentInput = input.nextLine();
			
			if (currentInput.isEmpty()) { done = true; }
			else
			{
				try
				{
					choice = Integer.parseInt(currentInput);
					
					if (choice >= 1 && choice <= preferences.length)
					{
						System.out.println();
						preferences[choice - 1] = choosePreferred(list, i1, i1 + choice, choice);
					}
					else
						System.out.println("\tNumber has to be between 1 and "
										   + preferences.length + "\n");
				}
				catch (NumberFormatException ex) { System.out.println("\tInvalid input.\n"); }
			}
		}
	}
	
	public static int savePrompt()
	{
		int result = -1;
		Scanner input = new Scanner(System.in);
		String currentInput;
		
		while (result == -1)
		{
			System.out.println("Blank line\t- continue");
			System.out.println("s\t\t- save and continue");
			System.out.println("q\t\t- save and quit");
			System.out.println("x\t\t- exit without saving");
			System.out.print("What would you like to do? ");
			
			currentInput = input.nextLine();
			
			if 		(currentInput.isEmpty())	result = 0;
			else if (currentInput.equals("s"))	result = 1;
			else if (currentInput.equals("q"))	result = 2;
			else if (currentInput.equals("x"))	result = 3;
			else	System.out.println("Invalid input.\n");
		}
		
		return result;
	}
	
	public static boolean is1Preferred()
	{
		Scanner input = new Scanner(System.in);
		String choice;
		boolean result = false, done = false;
		
		while (!done)
		{
			choice = input.nextLine();
			
			if (choice.contains("1") && !choice.contains("2"))
			{
				result = true;
				done = true;
			}
			else if (choice.contains("2") && !choice.contains("1"))
			{
				result = false;
				done = true;
			}
			else
				System.out.print("\tInvalid input... is it 1) or 2)? ");
		}
		
		System.out.println();
		
		return result;
	}
	
	public static void printResults(String[] list)
	{
		System.out.println("======================================================================");
		System.out.println("\tThe results");
		System.out.println("======================================================================\n");
		
		PrintStream output = Utilities.makeFilePrintStream("Results.txt");
		String currentOutput;

		for (int i = 0; i < list.length; i++)
		{
			currentOutput = (i + 1) + ")\t" + list[i];
			if (output != null)
				output.println(currentOutput);
			System.out.println(currentOutput);
		}
	}
}