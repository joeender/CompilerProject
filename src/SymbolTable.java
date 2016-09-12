import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;
import java.util.Vector;

/// Main SymbolTable with INSERT, FIND_in_CURRENT_SCOPE_1,
/// FIND_IN_ALL_SCOPES_2, and DISPLAY functions for the driver 
/// to use.
public class SymbolTable {
	
	/// Initializes necessary variables.
	private int blockCounter;
	private Stack<Integer> blockStack;
	private HashTable table;
	
	/// Default constructor.
	public SymbolTable()
	{
		blockCounter = 0;
		blockStack = new Stack<Integer>();
		table = new HashTable(30);
		blockStack.push(blockCounter);

	};
	
	/// Constructor that takes a filename to load and a bucket size for the 
	/// hash table.
	public SymbolTable(String f, int tableSize) throws IOException
	{
		blockCounter = 0;
		blockStack = new Stack<Integer>();
		table = new HashTable(tableSize);
		blockStack.push(blockCounter);
	}
	
	/// Starts a new block.
	public void startNewBlock()
	{
		blockCounter++;
		blockStack.push(blockCounter);
	}
	
	/// Ends a block.
	public void endCurrentBlock()
	{
		blockStack.pop();
	}
	
	/// Use in parser to find if an id has already been declared.
	public Boolean declaration(Token t)
	{
		if(FIND_in_CURRENT_SCOPE_1((int)blockStack.peek(), t.getId()) == (Token)null)
		{
			INSERT((int)blockStack.peek(), t.getId(), t.getTokenNumber());
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/// Finds an id in a given scope.
	public Token FIND_in_CURRENT_SCOPE_1(int blockNum, String s)
	{
		return table.getInScope(blockNum, s);
	}
	
	/// Returns a list of matching ids in all scopes.
	public Token FIND_IN_ALL_SCOPES_2(String s)
	{
		return table.findStringInAllScopes(s, blockStack.peek());
	}
	
	/// Inserts an id to a given scope.
	public void INSERT(int blockNum, String s, int idNumber)
	{
		table.addSymbol(new Token(s,idNumber,blockNum));
	}
	
	/// Goes through the hash table and each scope printing out all of the
	/// ids.
	public void DISPLAY()
	{
		String displayString = "";
		Vector<Token> tempArray;
		for(int i = 0; i <= blockCounter; i++)
		{
			tempArray = table.getAllInScope(i);
			if (!tempArray.isEmpty())
			{
				displayString = "Scope " + i + ": \n";
				//System.out.println("Scope " + i + ": ");
				for(int j = 0; j < tempArray.size(); j++)		
				{
					displayString = displayString + tempArray.get(j).getId() + "\n";
				}
				System.out.println(displayString);
				displayString = "";
			}
		}
	}
	
	
	/// Returns the highest block number (0 is a block number).
	public int giveBlockNum()
	{
		return blockStack.peek();
	}
	
	/// Parses a string into an with delimiters being spaces and new lines.
	private String[] parseLine(String line)
	{
		String[] parsedArray = null;
		String delims = "(\\s+)|(\\n)";
		parsedArray = line.split(delims);
		return parsedArray;
	}

}
