
///  This is the Token object class that contains the token String
///  and the token number as an int.
public class Token {
	
	String token;
	int tokenNumber;
	int blockNum;
	String type;
	int location;
	Boolean var;
	int size;

	/// Default constructor.
	Token()
	{
		token = "";
		tokenNumber = 0;
		blockNum = 0;
		type = "i";
		size = 0;
		var = false;
		location  = 0;
	}
	
	///  Constructor.
	Token(String t, int n, int b)
	{
		token = t;
		tokenNumber = n;
		blockNum = b;
		type = "i";
		size = 0;
		var = false;

	}
	
	/// Set info
	public void setInfo(String t, int s, Boolean v)
	{
		type = t;
		size = s;
		var = v;
	}
	
	/// Returns location
	public void setLocation(int l)
	{
		location = l;
	}
	
	///  Returns the token string.
	public String getId()
	{
		return token;
	}
	
	///  Returns the token number.
	public int getTokenNumber()
	{
		return tokenNumber;
	}
	
	/// Returns the block number.
	public int getBlockNum()
	{
		return blockNum;
	}
	
	/// Returns type
	public String getType()
	{
		return type;
	}
	
	/// Returns location
	public int getLocation()
	{
		return location;
	}

	/// Returns true if it is a variable
	public Boolean checkVar()
	{
		return var;
	}
}
