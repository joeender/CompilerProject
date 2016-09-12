import java.util.Vector;

/// I implemented my own version of the hash table to demonstrate some of the
/// concepts that I would not otherwise can with the built-in hash table.
public class HashTable 
{
	/// The hash table is a vector of vectors to imitate the behavior of a list
	/// of linked chain of Symbols.
	private Vector<Vector<Token>> table;
	private int num;
	private int tableSize;
	
	/// Initializes the hash table with an inputed number of buckets.
	public HashTable(int size)
	{
		num = 27;
		table = new Vector<Vector<Token>>();
		tableSize = size;
		for(int i = 0; i < tableSize; i++)
		{
			table.add(new Vector<Token>());
		}
	}
	
	/// Adds a Symbol (an id paired with its scope number) to the hash table.
	public void addSymbol(Token s)
	{
		int index = 0;
		index = stringToIndex(s.getId());
		table.get(index).add(s);
	}
	
	/// Turns a string to a hash number and into an index number to be added.
	public int stringToIndex(String s)
	{
		int asciiValue = 0;
		for(int i = 0; i < s.length(); i++)
		{
			asciiValue = asciiValue + (int)s.charAt(i);
		}
		return (asciiValue * num)%tableSize;
	}
	
	/// Takes a scope number and a string and finds the Symbol in the hash table,
	/// returns a null Symbol if not found.  Checks the first 8 characters of the id.
	public Token getInScope(int blockNum, String id)
	{
		for(int i = 0; i < table.get(stringToIndex(id)).size();i++)
		{
			if(id.length() >= 8)
			{
				if(table.get(stringToIndex(id)).get(i).getId().regionMatches(0,id,0,8)  && table.get(stringToIndex(id)).get(i).getBlockNum() == blockNum)
				{
					return table.get(stringToIndex(id)).get(i);
				}
			}
			else
			{
				if(table.get(stringToIndex(id)).get(i).getId().equals(id)  && table.get(stringToIndex(id)).get(i).getBlockNum() == blockNum)
				{
					return table.get(stringToIndex(id)).get(i);
				}
			}

		}
		return (Token)null;
	}
	
	/// Takes a string and goes through the hash table matching it with
	/// the id and returns a list of matching Symbols. Checks the first 8 characters of the id.
	public Token findStringInAllScopes(String id, int currentScope)
	{
		Vector<Token> foundSymbols = new Vector<Token>(0);
		for(int i = 0; i < table.size(); i++)
		{
			for(int j = 0; j < table.get(i).size();j++)
			{
				if (id.length()>=8)
				{
					if(table.get(i).get(j).getId().regionMatches(0,id,0,8) && table.get(i).get(j).getBlockNum() <= currentScope)
					{
						return (table.get(i).get(j));
					}
				}
				else
				{
					if(table.get(i).get(j).getId().equals(id) && table.get(i).get(j).getBlockNum() <= currentScope)
					{
						return (table.get(i).get(j));
					}
				}

			}
		}
		return (Token)null;
	}
	
	/// Takes a scope number and returns a list of all the Symbols
	/// that's in that scope.
	public Vector<Token> getAllInScope(int scope)
	{
		Vector<Token> foundSymbols = new Vector<Token>(0);
		for(int i = 0; i < table.size(); i++)
		{
			for(int j = 0; j < table.get(i).size();j++)
			{
				if(table.get(i).get(j).getBlockNum() == scope)
				{
					foundSymbols.add(table.get(i).get(j));
				}
			}
		}
		return foundSymbols;
	}
}


