import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
///  This is a Scanner that takes in a file and goes through it character by character
///  and returns a Token object that contains a token string and an token number that correspond
///  to a category of token.  The class have a file reader method which loads a line into a buffer
///  string.  The getChar() method takes a character and advances the position on the line.
///  There is more detail on the tokens that this class generates in the findToken() method.
import java.util.Vector;

public class Scanner {
	FileReader fileReader; 
	BufferedReader buffer;
	String currentLine;
	String backupLine;

	int linePos;
	
	/// Constructor.  Sets the starting position of where the gerChar() method gets
	/// the char from the currentLine String.
	public Scanner(String file) throws FileNotFoundException
	{
		fileReader = new FileReader(file); 
		buffer = new BufferedReader(fileReader);
		currentLine = "";
		backupLine = "";
		linePos = 0;
	}
	
	
	/// Returns the char from the currentLine String at index linePos.  If the current index is
	/// pass the length of the string, it calls the fillBuffer() method which fills the currentLine
	/// with the next line from the input file and resets the linePos to 0.  The fillBuffer() method
	/// will return false if there is no more line from the file which will have this method return
	/// the null char which will let the caller know that there is no more chars to get.
	public char getChar() throws IOException
	{
		char currentChar = '\u0000';
		if(linePos >= currentLine.length() || currentLine.length() == 0)
		{
			if (fillBuffer())
			{
				linePos = 0;
			}
			else
			{
				return '\u0000';
			}
		}
		
		currentChar = currentLine.charAt(linePos);
		linePos++;
		
		return currentChar;
	}
	
	/// Sets the linePos position back 1.
	public void goBack()
	{
		linePos--;
	}
	
	///  Calls the getChar() method and runs it through the switch statements, which acts as the
	///  DFA for the language that I created.
	public Token findToken() throws IOException
	{
		String buildToken = "";
		char currentChar;
		int state = 0;
		while(true)
		{
			///  Gets the char to run through the switch statement.
			currentChar = getChar();
			
			///  If the current char is the null char, returns the a Token with a number of 
			///  0 which lets the caller know that there are no more tokens to get.
			if(currentChar == '\u0000')
			{
				return new Token(buildToken,0,0);
			}
			
			///  The DFA as a switch statement.
			switch(state)
			{
				
			///  The start state.
				case 0:
					
					/// Starts identifier or keyword which starts with a letter
					/// and continues with letters and/or digits after
					if(Character.isLetter(currentChar))
					{
						buildToken += currentChar;
						state = 1;
					}
					/// Starts an integer
					else if(Character.isDigit(currentChar))
					{
						buildToken += currentChar;
						state = 2;
					}
					/// Left index bracket
					else if(currentChar == '[')
					{
						buildToken += currentChar;
						return new Token(buildToken,3,0);

					}
					/// Right index bracket
					else if(currentChar == ']')
					{
						buildToken += currentChar;
						return new Token(buildToken, 4,0);

					}
					/// Left brace bracket
					else if(currentChar == '{')
					{
						buildToken += currentChar;
						return new Token(buildToken, 5,0);

					}
					/// Right brace bracket
					else if(currentChar == '}')
					{
						buildToken += currentChar;
						return new Token(buildToken, 6,0);
					}
					/// Left parenthesis
					else if(currentChar == '(')
					{
						buildToken += currentChar;
						return new Token(buildToken, 11,0);
					}
					/// Right parenthesis
					else if(currentChar == ')')
					{
						buildToken += currentChar;
						return new Token(buildToken, 12,0);
					}
					/// End statement semi-colon
					else if(currentChar == ';')
					{
						buildToken += currentChar;
						return new Token(buildToken, 7,0);

					}
					/// Assignment operator
					else if(currentChar == '=')
					{
						buildToken += currentChar;
						state = 3;
					}
					/// Math operators + and -
					else if(currentChar == '+' || currentChar == '-')
					{
						buildToken += currentChar;
						return new Token(buildToken, 9,0);

					}
					/// Math operators * and %
					else if(currentChar == '*' || currentChar == '%')
					{
						buildToken += currentChar;
						return new Token(buildToken, 26,0);

					}
					/// Logical operators
					else if(currentChar == '>' || currentChar == '<')
					{
						buildToken += currentChar;
						return new Token(buildToken, 10,0);

					}
					else if(currentChar == ',')
					{
						buildToken += currentChar;
						return new Token(buildToken, 25,0);

					}
					/// Equal Logical operator: '=='
					else if(currentChar == '=')
					{
						buildToken += currentChar;
						state = 3;
					}
					/// Ignore space
					else if(Character.isSpaceChar(currentChar) || currentChar == '\t')
					{
						state = 0;
					}
					/// Starts a comment.
					else if(currentChar == '/')
					{
						state = 5;
					}
					/// Starts a '||' operator.
					else if(currentChar == '|')
					{
						buildToken += currentChar;
						state = 4;
					}
					/// Starts a '&&' operator.
					else if(currentChar == '&')
					{
						buildToken += currentChar;
						state = 7;
					}
					/// Any symbol not recognized as part of the language will produce an token
					/// with an error number.
					else
					{
						buildToken += currentChar;
						System.out.println("Error! '" + currentChar + "' is not a recognized character.");
						return new Token(buildToken, -1,0);
					}
					break;
				case 1:
					/// Continues writing the identity or keyword token.
					if(Character.isLetter(currentChar) || Character.isDigit(currentChar))
					{
						buildToken += currentChar;
						state = 1;
						if(detectNewLine())
						{
							if(buildToken.contentEquals("MAIN"))
							{
								return new Token(buildToken, 13,0);
							}
							else if(buildToken.contentEquals("FUNCTION"))
							{
								return new Token(buildToken, 14,0);
							}
							else if(buildToken.contentEquals("INTEGER"))
							{
								return new Token(buildToken, 15,0);
							}
							else if(buildToken.contentEquals("BOOL"))
							{
								return new Token(buildToken, 16,0);
							}
							else if(buildToken.contentEquals("ARRAY"))
							{
								return new Token(buildToken, 17,0);
							}
							else if(buildToken.contentEquals("FALSE"))
							{
								return new Token(buildToken, 18,0);
							}
							else if(buildToken.contentEquals("TRUE"))
							{
								return new Token(buildToken, 19,0);
							}
							else if(buildToken.contentEquals("IF"))
							{
								return new Token(buildToken, 20,0);
							}
							else if(buildToken.contentEquals("WHILE"))
							{
								return new Token(buildToken, 21,0);
							}
							else if(buildToken.contentEquals("RETURN"))
							{
								return new Token(buildToken, 22,0);
							}
							else if(buildToken.contentEquals("PRINT"))
							{
								return new Token(buildToken, 23,0);
							}
							/// id Token
							else
							{
								return new Token(buildToken, 1,0);
							}						}
					}
					/// Finishes an identity or keyword.
					else
					{
						goBack();
						
						if(buildToken.contentEquals("MAIN"))
						{
							return new Token(buildToken, 13,0);
						}
						else if(buildToken.contentEquals("FUNCTION"))
						{
							return new Token(buildToken, 14,0);
						}
						else if(buildToken.contentEquals("INTEGER"))
						{
							return new Token(buildToken, 15,0);
						}
						else if(buildToken.contentEquals("BOOL"))
						{
							return new Token(buildToken, 16,0);
						}
						else if(buildToken.contentEquals("ARRAY"))
						{
							return new Token(buildToken, 17,0);
						}
						else if(buildToken.contentEquals("FALSE"))
						{
							return new Token(buildToken, 18,0);
						}
						else if(buildToken.contentEquals("TRUE"))
						{
							return new Token(buildToken, 19,0);
						}
						else if(buildToken.contentEquals("IF"))
						{
							return new Token(buildToken, 20,0);
						}
						else if(buildToken.contentEquals("WHILE"))
						{
							return new Token(buildToken, 21,0);
						}
						else if(buildToken.contentEquals("RETURN"))
						{
							return new Token(buildToken, 22,0);
						}
						else if(buildToken.contentEquals("PRINT"))
						{
							return new Token(buildToken, 23,0);
						}
						/// id Token
						else
						{
							return new Token(buildToken, 1,0);
						}
					}
					break;
				case 2:
					/// Continues writing the integer token.
					if(Character.isDigit(currentChar))
					{
						buildToken += currentChar;
						state = 2;
						if(detectNewLine())
						{
							return new Token(buildToken, 2,0);
						}
					}
					/// Finish writing the integer token.
					else
					{
						goBack();
						return new Token(buildToken, 2,0);
					}
					break;
					
					/// Finishes the '==' operator token.
				case 3:
					if(currentChar == '=')
					{
						buildToken += currentChar;
						return new Token(buildToken, 10,0);
					}
					/// Finishes the '=' assignment token.
					else
					{
						goBack();
						return new Token(buildToken, 8,0);
					}
					///Finishes the '||' operator token.
				case 4:
					if(currentChar == '|')
					{
						buildToken += currentChar;
						return new Token(buildToken, 24,0);
					}
					/// Single '|' found, not a recognized token.
					else
					{
						goBack();
						return new Token(buildToken, -1,0);
					}
					///Finishes the '&&' operator token.
				case 7:
					if(currentChar == '&')
					{
						buildToken += currentChar;

						return new Token(buildToken, 24,0);
					}
					/// Single '&' found, not a recognized token.
					else
					{
						goBack();
						return new Token(buildToken, -1,0);
					}
					///  Confirms that there is a comment and ignores the rest of the line.
				case 5:
					if(currentChar == '/')
					{
						state = 6;
						if(detectNewLine())
						{
							state = 0;
						}
					}
					///  '/' operator found.
					else
					{
						goBack();
						return new Token(buildToken, -1,0);
					}
					///  Loops through the rest of the comments until the end of the line.
					///  Then, goes back to the start state.
				case 6:
					state = 6;
					if(detectNewLine())
					{
						state = 0;
					}
			}
		}
	}
	
	
	///  This method fills the currentLine String from the next line from the 
	///  input file.  If there is no more lines, returns false and closes the file.
	public Boolean fillBuffer() throws IOException
	{
		backupLine = currentLine;
		if ((currentLine = buffer.readLine()) != null)
		{
			/// This checks if the current line is empty, if so get the next one.
			while(currentLine.length() == 0)
			{
				/// Checks if the next line in file exists.
				if ((currentLine = buffer.readLine()) == null)
				{
					return false;
				}
			}
			return true;
		}
		else
		{
			buffer.close();
			return false;
		}
	}
	
	///  Returns true if the linePos is at the last index of currentLine String.
	public Boolean detectNewLine()
	{
		if(linePos - 1 == currentLine.length() - 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
