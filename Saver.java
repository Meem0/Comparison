import java.util.LinkedList;
import java.util.Iterator;
import java.util.Scanner;
import java.io.PrintStream;

public class Saver
{
	public Saver()
	{
		_inputFileName = "Input.txt";
		init();
	}
	
	public Saver(String inputFileName)
	{
		_inputFileName = inputFileName;
		init();
	}
	
	private void init()
	{
		_lastSave = Utilities.makeList(WRITE_NAME);
		
		// code for this execution's input; to be written when save is called
		_writeCode = getSaveCode(Utilities.makeFileScanner(_inputFileName));
		
		initializeRead();
		_write = new LinkedList<Boolean[]>();
	}
	
	public Boolean[] readNext()
	{
		return _read.pollFirst();
	}
	
	public Boolean isReading()
	{
		return _read != null && !_read.isEmpty();
	}
	
	public void write(Boolean[] input)
	{
		Boolean[] listCopy = new Boolean[input.length];
		
		for (int i = 0; i < input.length; i++)
			listCopy[i] = input[i];
		
		_write.add(listCopy);
	}
	
	public void save()
	{
		PrintStream output = Utilities.makeFilePrintStream(WRITE_NAME);
		Iterator<Boolean[]> itr = _write.iterator();
		Boolean[] current;
		
		output.println(getSaveCode(Utilities.makeFileScanner(_inputFileName)));
		
		if (_lastSave != null)
		{
			for (int i = 1; i < _lastSave.length; i++)
				output.println(_lastSave[i]);
		}
		
		while (itr.hasNext())
		{
			current = itr.next();
			
			for (int i = 0; i < current.length; i++)
			{
				if (current[i] == true) output.print('1');
				else output.print('0');
			}
			output.println();
		}
	}
	
	private void initializeRead()
	{
		// previous execution's save file
		Scanner input = Utilities.makeFileScanner(WRITE_NAME);
		
		if (input == null) return;
		
		String currentLine;
		Boolean[] lineContents;
		
		if (Integer.parseInt(input.nextLine()) == _writeCode)
		{
			if (!Utilities.inputYesNo("Load save file?"))
				return;
		
			_read = new LinkedList<Boolean[]>();
			while (input.hasNextLine())
			{
				currentLine = input.nextLine();
				lineContents = new Boolean[currentLine.length()];
				
				for (int i = 0; i < lineContents.length; i++)
				{
					if (currentLine.charAt(i) == '0')
						lineContents[i] = false;
					else
						lineContents[i] = true;
				}
				
				_read.add(lineContents);
			}
		}
		else
		{
			System.out.println("Found a save file, but it was created with a different input list.");
		}
	}
	
	private int getSaveCode(Scanner input)
	{
		String currentLine;
		int lineCount = 1, i, result = 0, lineResult;
		
		while (input.hasNext())
		{
			currentLine = input.nextLine();
			lineResult = 0;
			
			for (i = 0; i < currentLine.length(); i++)
				lineResult += (int)(currentLine.charAt(i));
			result += lineResult % lineCount++;
		}
		
		return result;
	}

	private LinkedList<Boolean[]> _read;
	String _inputFileName;
	
	private LinkedList<Boolean[]> _write;
	static final String WRITE_NAME = "Save.dat";
	int _writeCode;
	private String[] _lastSave;
}