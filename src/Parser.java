/// CS6110 Parser for JZLANGUAGE 

/// Basic program structure:
/// INTEGER funct1(INTEGER num1){ num1 = 0; RETURN num;} // Function Declaration
/// BOOL funct2(INTEGER num1){ num1 = 0; RETURN num;} // Function Declaration
/// MAIN{
///		INTEGER num2;					/// Integer declaration
/// 	BOOL b;							/// Bool declaration
///		ARRAY INTEGER array1[10];		/// Integer array declaration.  Must give size in digits and must have a size
///		ARRAY BOOL array2[10];			/// Bool array declaration. Same restrictions as integer array;
///     num2 = 5;						/// Sample assignments:
///     num2 = funct1(num2);
///     array1[num2] = 4;
///     b = FALSE;
//		IF(TRUE || (FALSE == b)) {INTEGER num3 = 0} 	/// If statement
///		WHILE(num2 < 10){num2 = num2 + 1}			/// While statement
///		PRINT num2;						/// Print statement
///		PRINT TRUE;						/// Printing bools will return 1 for true and 0 for false.
///}
/// Math operators:
///	+ addition
/// - subtraction
/// * multiplication
/// % division
///
/// Boolean operators:
/// < less than
/// > more than
/// == is
/// || or
/// && and
///
/// LIMITATIONS:
/// Declarations and assignments have to be two different statements.
/// No floats, only integers in this language.  Division will round down.
/// Arrays must be declared with a size and the size must be a digit.
/// Functions cannot call other functions

/// 5/25/2016
/// ADDITIONS:  Code generation for Boolean operators, TRUE, FALSE, IF and WHILE statements,
/// ARRAYS, functions, and RETURN statements.
/// NOTE LIMITATIONS:  Array size have to be declared using digits. Functions cannot call other
/// functions.  Arrays can be declared in the parameters of the function, but whole arrays cannot
/// be passed into a function.

///  This is the parser for my language, this class holds an instance of the 
///  scanner that feeds the rules functions a token to check if it is part of the grammar.
///  The stringSofar variable takes each token and creates a string to be displayed.
///	 This is to show where the grammar stopped working.

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class Parser {
	/// Instance of the code writer.
	CodeGenerator codeGen;
	
	Scanner scanner;
	Token currentToken;
	Token symbolPtr;
	String stringSofar;
	SymbolTable table;
	
	/// Symbol variables to be saved during declaration
	int currentOff;
	int arrayOffAssign;
	int functionLocation;
	
	
	//Location and Type as global rather than passing it through parameters.
	int location;
	String type;
	String symbol;
	
	//Label Variables
	int numIFs;
	int numWHILEs;

	//Parameter Counter;
	int numParams;
	
	/// This String builds into the parse tree as it adds the rule number to the end of it.
	/// This will print if the parse was successful.
	String parseTree;
	public Parser(String file) throws FileNotFoundException, IOException
	{
		codeGen = new CodeGenerator("code1.s");
		scanner = new Scanner(file);
		table = new SymbolTable();
		currentToken = new Token();
		stringSofar = "";
		parseTree = "";
		/// Symbol Info
		currentOff = 0;
		arrayOffAssign = 0;
		functionLocation = 0;
		
		///
		location = 0;
		type = "";
		symbol = "";
		
		//Label Variables
		numIFs = 0;
		numWHILEs = 0;
		
		// Number of Parameters;
		numParams = 0;
		
		nextToken();
		runScanner();
	}
	
	/// Main method that runs the parser.
	public void runScanner() throws IOException{
		/// Starts the grammar rules.
		parseTree += 1 + ",";
		Program();
		
		/// If the last token is 0 (meaning it's the end of the file, print success.)
		if(currentToken.getTokenNumber() == 0)
		{
			System.out.println(parseTree.substring(0, parseTree.length() - 1));
		}
		/// If something is wrong, print the inputs so far before the error happened.
		else
		{
			System.out.println(stringSofar);
		}
		table.DISPLAY();
	}
	
	/// 1 Program := morefunctiondef ‘MAIN’ ‘{’ morestatements ‘}’
	private void Program() throws IOException{
		
		codeGen.writeProlog();
		/// Generate code to have the value 0 in an offset.  Used as a default value for
		/// index location.
		codeGen.write("li  $t0  0");
		codeGen.write("sw  $t0  " + locationToString(currentOff));
		/// Main label, since the functions are created first, MIPS will skip to the MAIN label
		/// passing the function instructions.
		codeGen.write("j  MAIN");

		currentOff += 4;
		
		morefunctiondef();
		codeGen.write("MAIN:");

		/// Checks for MAIN
		if(currentToken.getTokenNumber() == 13)
			nextToken();
		else
			error("1a");
		/// Checks for {
		if(currentToken.getTokenNumber() == 5)
			nextToken();
		else
			error("1b");
		
		morestatments();
		
		/// Checks for }
		if(currentToken.getTokenNumber() == 6){
			nextToken();
			/// Check if end of program also is end of file.
			if(currentToken.getTokenNumber() != 0){
				System.out.println("Error! Tokens found past end of program.");
				error("1c");
			}
			codeGen.writePostlog();
		}
		else
			error("1d");
	}
	/// 2 functiondef := type idnonterm ‘(’ functionparams ‘)’ ‘{’ morestatements ‘}’
	private Boolean functiondef() throws IOException{
		/// Type is either Bool or Integer
		if(type()){
			idnontermdec();
			
			codeGen.write(symbolPtr.getId() + ":");
			
			/// Checks for (
			if(currentToken.getTokenNumber() == 11){
				nextToken();
				table.startNewBlock();
			}
			else
				error("2a");
			
			functionparams();

			numParams = 0;
			
			/// Checks for )
			if(currentToken.getTokenNumber() == 12)
				nextToken();
			else
				error("2b");
			/// Checks for {
			if(currentToken.getTokenNumber() == 5)
				nextToken();
			else
				error("2c");
			
			morestatments();
			
			/// Checks for }
			if(currentToken.getTokenNumber() == 6){
				nextToken();
				table.endCurrentBlock();
				
			}
			else
				error("2d");
			return true;
		}
		return false;
	}
	
	/// 3,4 morefunctiondef := <empty> | functiondef morefunctiondef
	private void morefunctiondef() throws IOException{
		/// Rule 4
		addToParse("4");
		if (functiondef()){
			morefunctiondef();
		}
		/// Rule 3
		else{
			removeLastRule(2);
			addToParse("3");
		}
	}
	
	/// 5,6 functionparams := <empty> | declareparam morefunctionparams
	private void functionparams() throws IOException{
		/// Rule 6
		addToParse("6");
		if(declareparam()){
			morefunctionparams();
		}
		/// Rule 5
		else{
			removeLastRule(2);
			addToParse("5");
		}
	}
	
	/// 7,8 morefunctionparams := <empty> | ‘,’ declareparam morefunctionparams
	private void morefunctionparams() throws IOException{
		/// Rule 8
		addToParse("8");
		/// Checks for ,
		if(currentToken.getTokenNumber() == 25){
			nextToken();
			declareparam();
			morefunctionparams();
		}
		/// Rule 7
		else{
			removeLastRule(2);
			addToParse("7");
		}
	}
	
	/// 9 declareparam := type idnontermdec
	private Boolean declareparam() throws IOException{
		
		int localArray;
		if (type()){
			
			if(!idnontermdec()){
				error("9");
			}
			
			localArray = arrayOffAssign;
			
			/// Code Gen To Assign Values to declared 

				codeGen.write("li  $t3  4");				
				codeGen.write("lw  $t1  " + locationToString(localArray));
				codeGen.write("mult  $t3  $t1");
				codeGen.write("mflo  $t1");
				codeGen.write("la  $t2  " + locationToString(location));

				codeGen.write("sub $t1  $t2  $t1");
				codeGen.write("sw  " + "$s"+ numParams + "  0($t1)");
				
				arrayOffAssign = 0;
				numParams++;
			
			return true;
		}
		else
			return false;
	}
	
	/// 10 declare := type arraycheckdec;
	private Boolean declare() throws IOException{
		if (type()){
			arraycheckdec();
			if(currentToken.getTokenNumber() == 7)		/// Check for ;
				nextToken();
			else
				error("10");
			return true;
		}
		else
			return false;
	}
	/// 11,12 arraycheckdec := idnontermdec | ‘ARRAY’ idnontermdec ‘[’ digit ‘]’
	private void arraycheckdec() throws IOException{
		/// Rule 12
		/// Checks for ARRAY
		int size = 0;
		if(currentToken.getTokenNumber() == 17){
			addToParse("12");
			nextToken();
			if(!idnontermdec())
				error("11a");	
			/// Checks for [
			if(currentToken.getTokenNumber() == 3){
				nextToken();
			}
			else
				error("11b");
			
			if(!(digit()))
				error("11c");
			
			size = Integer.parseInt(symbol) - 1;
			currentOff += 4 * size;
			
			/// Checks for ]
			if(currentToken.getTokenNumber() == 4){
				nextToken();
			}
			else
				error("11d");
			
		}
		/// Rule 11
		else
		{
			addToParse("11");
			idnontermdec ();		
		}
	}
	
	/// 13 assign := arraycheckassign ‘=’ express ‘;’
	private Boolean assign() throws IOException{
		String localType;
		Token leftToken;
		int localArray;;
		int localLocation;
		
		if (arraycheckassign())
		{
			///
			localType = symbolPtr.getType();
			leftToken = symbolPtr;
			localArray = arrayOffAssign;
			localLocation = location;
			
			/// Checks for =
			if(currentToken.getTokenNumber() == 8){
				nextToken();
			}
			else
				error("13a");
			
			if(!express())
				error("13b");
			
			/// Checks for ;
			if(currentToken.getTokenNumber() == 7){
				nextToken();
			}
			else
				error("13c");
			
			/// Code Gen for assignment, regular variables have a index offset of 0.
			/// Arrays assignment have values assigned to their base offset plus their
			/// offset amount of the index number.
			if(localType.contains(type))
			{
				/// Multiply the index number by 4
				codeGen.write("li  $t3  4");				
				codeGen.write("lw  $t1  " + locationToString(localArray));
				codeGen.write("mult  $t3  $t1");
				codeGen.write("mflo  $t1");
				/// Get base array/variable offset
				codeGen.write("la  $t2  " + locationToString(localLocation));
				/// Adjust for index value
				codeGen.write("sub $t1  $t2  $t1");
				/// Save value into that address
				codeGen.write("lw  $t0  " + locationToString(location));
				codeGen.write("sw  $t0  0($t1)");
				
				arrayOffAssign = 0;

			}
			else
			{
				System.out.println(leftToken.getId() + " and " + symbolPtr.getId() + " have mismatch types.");
			}
			
			return true;
		}
		else
			return false;
	}
	
	/// 14 arraycheckassign := idnontermuse arraycheckassign1
	private Boolean arraycheckassign() throws IOException{
		if(idnontermuse())
		{
			arraycheckassign1();
			return true;
		}
		else
			return false;
	}
	
	/// 15,16 arraycheckassign1 := <empty> | ‘[’ express ‘]’
	private void arraycheckassign1() throws IOException{
		int baseLocation = location;
		///Rule 16
		/// Checks for [
		if(currentToken.getTokenNumber() == 3){
			addToParse("16");
			nextToken();
			if(!express())
				error("16a");

			arrayOffAssign = location;
			location = baseLocation;
			/// Checks for ]
			if(currentToken.getTokenNumber() == 4){
				nextToken();
			}
			else
				error("16b");
		}
		/// Rule 15
		else
			addToParse("15");
	}
	
	/// 17 express := term express1
	private Boolean express() throws IOException{
		if(term())
		{
			express1();
			return true;
		}
		else
			return false;
	}
	
	/// 18,19,20,21 express1 := <empty> |‘+’ term express1 | ‘-’ term express1 | boolop2 term express1
	private void express1() throws IOException{
		/// Rule 19 and 20
		/// Checks for + or -
		int localLocation = location;
		
		String localType = type;
		String operator;
		operator = currentToken.getId();

		if(currentToken.getTokenNumber() == 9){
			addToParse("19|20");
			nextToken();
			
			if(!(type.contains(localType)))
				System.out.println("Type operands wrong type for +, - operation.");
			
			if(!term())
				error("19|20");
			
			///Code Gen ADDITION and SUBTRACTION			
			if (operator.contains("+"))
				operator = "add";
			else
				operator = "sub";

			codeGen.write("lw  $t0  " + locationToString(localLocation));
			codeGen.write("lw  $t1  " + locationToString(location));
			codeGen.write(operator + "  $t2  $t0  $t1");
			codeGen.write("sw  $t2  " + locationToString(currentOff));
			
			location = currentOff;
			currentOff += 4;
			
			express1();
		}
		else if (boolop2()){
			addToParse("21");

			if(!term())
				error("21");
			
			///Code Gen BOOL operators	&& and ||	
			if (operator.contains("&&")){
				codeGen.write("lw  $t0  " + locationToString(localLocation));
				codeGen.write("lw  $t1  " + locationToString(location));
				codeGen.write("and  $t2  $t0  $t1");
				codeGen.write("sw  $t2  " + locationToString(currentOff));
			}
			else if (operator.contains("||")){
				codeGen.write("lw  $t0  " + locationToString(localLocation));
				codeGen.write("lw  $t1  " + locationToString(location));
				codeGen.write("or  $t2  $t1  $t0");
				codeGen.write("sw  $t2  " + locationToString(currentOff));
			}
			
			location = currentOff;
			type = "b";
			currentOff += 4;
			
			express1();
		}
		else
			addToParse("18");
	}
	/// 22 term := factor term1
	private Boolean term() throws IOException{
		if(factor()){
			term1();
			return true;
		}
		else
			return false;
	}
	
	/// 23,24,25,62 term1 := <empty> |‘*’ factor term1 | ‘%’ factor term1 | boolop1 factor term1
	private void term1() throws IOException{
		/// Rule 24 and 25
		int localLocation = location;
		String operator;
		operator = currentToken.getId();

		/// Checks for * or %
		if(currentToken.getTokenNumber() == 26){
			addToParse("24|25");
			nextToken();
			
			if(!factor())
				error("24|25");
			
			///Code Gen MULTIPLY and DIVIDE			
			if (operator.contains("*"))
				operator = "mult";
			else
				operator = "div";
			
			codeGen.write("lw  $t0  " + locationToString(localLocation));
			codeGen.write("lw  $t1  " + locationToString(location));
			codeGen.write(operator + "  $t0  $t1");
			codeGen.write("mflo  $t2");
			codeGen.write("sw  $t2  " + locationToString(currentOff));

			location = currentOff;
			currentOff += 4;
			term1();
		}
		else if (boolop1()){
			addToParse("61");
			
			if(!factor())
				error("61");
			
			///Code Gen BOOL operators			
			if (operator.contains("<")){
				codeGen.write("lw  $t0  " + locationToString(localLocation));
				codeGen.write("lw  $t1  " + locationToString(location));
				codeGen.write("slt  $t2  $t0  $t1");
				codeGen.write("sw  $t2  " + locationToString(currentOff));
			}
			else if (operator.contains(">")){
				codeGen.write("lw  $t0  " + locationToString(localLocation));
				codeGen.write("lw  $t1  " + locationToString(location));
				codeGen.write("slt  $t2  $t1  $t0");
				codeGen.write("sw  $t2  " + locationToString(currentOff));
			}
			else if (operator.contains("==")){
				codeGen.write("lw  $t0  " + locationToString(localLocation));
				codeGen.write("lw  $t1  " + locationToString(location));
				codeGen.write("seq  $t2  $t1  $t0");
				codeGen.write("sw  $t2  " + locationToString(currentOff));
			}
			
			location = currentOff;
			type = "b";
			currentOff += 4;

			term1();
		}
		/// Rule 23
		else
			addToParse("23");
	}
	
   ///26,27,28,29 factor := ‘(’ express ‘)’ | idcheck | digit | booltruefalse
	private Boolean factor() throws IOException{
		/// Check for (				/// Rule 26
		if(currentToken.getTokenNumber() == 11){
			addToParse("26");
			nextToken();
			if(!express())
				error("26");
			/// Check for )
			if(currentToken.getTokenNumber() == 12){
				nextToken();
			}
			else
				error("26");
			return true;
		}
		else if (idcheck()){ 		/// Rule 27
			addToParse("27");
			return true;
		}
		else if (digit()){			/// Rule 28
		
			addToParse("28");

			return true;
		}
		else if (booltruefalse()){	/// Rule 29
		
			addToParse("29");
			return true;
		}
		else
			return false;
	}
	
	/// 30,31 booltruefalse := ‘TRUE’ | ‘FALSE’
	private Boolean booltruefalse() throws IOException{
			///	Rule 30			Check for TRUE
		int bool;
		if(currentToken.getTokenNumber() == 19){
			bool = 1;
			addToParse("30");			
		}	///	Rule 31		 	Check for FALSE
		else if(currentToken.getTokenNumber() == 18){
			bool = 0;
			addToParse("31");
		}
		else
			return false;
		
		type = "b";
		location = currentOff;
		
		/// Save the bool value 0 or 1 in an offset.
		codeGen.write("li  $t0  " + bool);
		codeGen.write("sw  $t0  " + locationToString(location));
		currentOff += 4;
		
		nextToken();
		return true;
	}
	
	/// 32,33,34,35,36 boolop1  := ‘<’ | ‘>’ | ‘==’ 
	private Boolean boolop1() throws IOException{
		/// Check for bool operations.
		if(currentToken.getTokenNumber() == 10){
			nextToken();
			return true;
		}
		return false;
	}
	
	/// 35,36 boolop2  := ‘&&’ | ‘||’
	private Boolean boolop2() throws IOException{
		/// Check for bool operations.
		if(currentToken.getTokenNumber() == 24){
			nextToken();
			return true;
		}
		return false;
	}

	
	/// 37 idcheck := idnonterm idcheck1
	private Boolean idcheck() throws IOException{
		if(idnontermuse()){
			idcheck1();
			return true;
		}
		else
			return false;
	}
	
	/// 38,39,40 idcheck1 := <empty> | ‘[’ express ‘]’ | ‘(’ arguments ‘)’
	private void idcheck1() throws IOException{
			/// Rule 39		 Checks for [
		
		int localLocation = symbolPtr.getLocation();
		String idName = symbolPtr.getId();
		String localType = symbolPtr.getType();
		int arrayIndex;
		if(currentToken.getTokenNumber() == 3){
			addToParse("39");
			nextToken();
			if(!express())
				error("39");
			
			///Using ID as an array
			//indexLocation = location;
			arrayIndex = location;
			
			/// Multiply the index number by 4
			codeGen.write("li  $t3  4");				
			codeGen.write("lw  $t1  " + locationToString(arrayIndex));
			codeGen.write("mult  $t3  $t1");
			codeGen.write("mflo  $t1");
			/// Set array base address off by index 
			codeGen.write("la  $t2  " + locationToString(localLocation));
			codeGen.write("sub  $t1  $t2  $t1");
			/// Save the value in the index in a offset.
			codeGen.write("lw  $t4  0($t1)");
			codeGen.write("sw  $t4  " + locationToString(currentOff));

			location = currentOff;
			type = localType;
			currentOff += 4;
			
			/// Checks for ]
			if(currentToken.getTokenNumber() == 4){
				nextToken();
			}
			else
				error("39");
		}
			/// Rule 40		Checks for (
		else if(currentToken.getTokenNumber() == 11){
			addToParse("40");
			nextToken();
			arguments();
			
			functionLocation = localLocation;
			/// Code Gen to Jump to function, RETURN statement will return to address after
			/// the jal instruction to take the value saved in 4($sp) and save it in a new offset
			codeGen.write("jal  " + idName);
			codeGen.write("lw  $t0  4($sp)");
			codeGen.write("sw  $t0  " + locationToString(currentOff));
			
			location = currentOff;
			type = localType;
			currentOff += 4;

			/// Checks for )
			if(currentToken.getTokenNumber() == 12){
				nextToken();
			}
			else
				error("40");
		}	/// Rule 38
		else
			addToParse("38");
	}
	
	/// 41,42 arguments := <empty> | express morearguments
	private void arguments() throws IOException{
		if(express()){	/// Rule 42
			addToParse("42");
			
			/// Code Gen saves argument values to $s registers
			codeGen.write("lw  $s" + numParams + "  " + locationToString(location));
			numParams++;
			
			morearguments();
			numParams = 0;
			
		}
		else
			addToParse("41");
	}
	
	/// 43,44 morearguments := <empty> |‘,’ express morearguments
	private void morearguments() throws IOException{
		/// Rule 44			Check for ,
		if(currentToken.getTokenNumber() == 25){
			addToParse("44");
			nextToken();
			if(!express()){
				error("44");
			}
			
			/// Code Gen saves argument values to $s registers
			codeGen.write("lw  $s" + numParams + "  " + locationToString(location));
			numParams++;
			morearguments();
		}
		else
			addToParse("43");
	}
	
	/// 45 conditional := whileif ‘(’ express ‘)’ ‘{’ morestatements ‘}’
	private Boolean conditional() throws IOException{
		String whileORif;
		int localnumIFs = numIFs;
		int localnumWHILEs = numWHILEs;

		if(whileif())
		{
			whileORif = currentToken.getId();
			nextToken();

			if(currentToken.getTokenNumber() == 11){			/// Check for (		
				nextToken();
				
				/// Code Gen for while, sets up while label for looping
				if(whileORif.contains("WHILE")){
					codeGen.write("beginWHILE" + localnumWHILEs + ":");
					numWHILEs++;
				}
				
				if(!express())
					error("45");
				
				if(!type.contains("b"))
					System.out.println("Non-bool in conditional statement.");
				
				/// Test for if, if bool is 0 jump to end of if.
				if(whileORif.contains("IF"))
				{
					codeGen.write("lw  $t0  " + locationToString(location));
					codeGen.write("beqz  $t0  endIF" + localnumIFs);
					numIFs++;

				}
				
				/// Test for while, if bool is 0 jump to end of while
				if(whileORif.contains("WHILE"))
				{
					codeGen.write("lw  $t0  " + locationToString(location));
					codeGen.write("beqz  $t0  endWHILE" + localnumWHILEs);
				}
				
				if(currentToken.getTokenNumber() == 12){		/// Check for )
					nextToken();
				}
				else
					error("45a");
			}
			if(currentToken.getTokenNumber() == 5)				/// Checks for {
				nextToken();
			else
				error("45b");
			
			morestatments();
			
			/// Skip label for if statement.
			if(whileORif.contains("IF"))
			{
				codeGen.write("endIF" + localnumIFs + ":");
			}
			
			/// Loop statement if while bool is true.  Also the end label if bool is false
			if(whileORif.contains("WHILE"))
			{
				codeGen.write("j beginWHILE" + localnumWHILEs);
				codeGen.write("endWHILE" + localnumWHILEs + ":");
			}
			
			if(currentToken.getTokenNumber() == 6)				/// Checks for }
				nextToken();
			else
				error("45c");
			return true;
		}
		else
			return false;
	}
	
	/// 46,47 whileif : = ‘IF’ | ‘WHILE’
	private Boolean whileif() throws IOException{
		/// Rule 46
		if(currentToken.getTokenNumber() == 20){				/// Checks for IF
			return true;
		}		/// Rule 47
		else if(currentToken.getTokenNumber() == 21){			/// Checks for WHILE
			return true;
		}
		else
			return false;
	}
	
	/// 48 returnstate := ‘RETURN’ express ‘;’
	private Boolean returnstate() throws IOException{
		if(currentToken.getTokenNumber() == 22){				/// Checks for RETURN
			nextToken();
			if(!express())
				error("48");
			
			///Code Gen saves expression value onto stack and jumps back to function caller
			codeGen.write("lw  $t0  " + locationToString(location));
			codeGen.write("sw  $t0  4($sp)");
			codeGen.write("jr  $ra");
			
			if(currentToken.getTokenNumber() == 7)				/// Checks for ;
				nextToken();
			else
				error("48");
			return true;
		}
		else
			return false;
	}
	
	/// 49 printstate := ‘PRINT’ express ‘;’
	private Boolean printstate() throws IOException{
		if(currentToken.getTokenNumber() == 23){				/// Checks for PRINT
			nextToken();
			if(!express())
				error("49");
			if(currentToken.getTokenNumber() == 7)				/// Checks for ;
				nextToken();
			else
				error("49");
			
			///Code Gen to print in MIPS
			codeGen.write("lw  $a0  " + locationToString(location));
			codeGen.write("li  $v0  1");
			codeGen.write("syscall");
			codeGen.newLine();
			
			return true;
		}
		else
			return false;
	}
	
	/// 50,51,52,53,54 statement := assign | declare |conditional | returnstate | printstate
	private Boolean statement() throws IOException{
		if(assign())
			addToParse("50");
		else if(declare())
			addToParse("51");
		else if(conditional())
			addToParse("52");
		else if(returnstate())
			addToParse("53");
		else if(printstate())
			addToParse("54");
		else
			return false;
		return true;
	}
	
	/// 55,56 type : ‘BOOL’ | ‘INTEGER’
	private Boolean type() throws IOException{
		/// Rule 55 Check for BOOL
		if(currentToken.getTokenNumber() == 16){
			addToParse("55");
			type = "b";

			nextToken();
			return true;
		}
		/// Rule 56 Check for INTEGER
		else if(currentToken.getTokenNumber() == 15){
			addToParse("56");
			type = "i";
			nextToken();
			return true;
		}
		else{
			return false;
		}
	}
	
	/// 57 digit := DIGITTOK
	private Boolean digit() throws IOException{
		if(currentToken.getTokenNumber() == 2){	/// Check for digits			
			/// Location and Type to pass into expression functions
			type = "i";
			location = currentOff;
			symbol = currentToken.getId();
			
			codeGen.write("li  $t0  " + currentToken.getId());
			codeGen.write("sw  $t0  " + locationToString(location));
			currentOff += 4;
			
			nextToken();
			return true;
		}
		else
			return false;
	}
	
	/// 58 idnontermdec  := IDTOK
	private Boolean idnontermdec() throws IOException{
		/// Checks for ID
		if(currentToken.getTokenNumber() == 1){

			if(!table.declaration(currentToken))
			{
				System.out.println(currentToken.getId() + " has already been declared in this scope.");
			}
			/// Set additional INFO
			symbolPtr = table.FIND_in_CURRENT_SCOPE_1(table.giveBlockNum(), currentToken.getId());
			symbolPtr.setInfo(type, 0, true);
			symbolPtr.setLocation(currentOff);
			
			type = symbolPtr.getType();
			location = symbolPtr.getLocation();
			
			currentOff += 4;
			nextToken();
			return true;
		}
		else
			return false;	
	}
	
	/// 59 idnontermuse  := IDTOK
	private Boolean idnontermuse() throws IOException{
		/// Checks for ID
		if(currentToken.getTokenNumber() == 1){
			if(table.FIND_IN_ALL_SCOPES_2(currentToken.getId()) == null){
				System.out.println(currentToken.getId() + " has not been declared");
			}
			symbolPtr = table.FIND_in_CURRENT_SCOPE_1(table.giveBlockNum(), currentToken.getId());
			
			/// Location and Type to pass into expression functions
			location = symbolPtr.getLocation();
			type = symbolPtr.getType();
			
			nextToken();
			return true;
		}
		else
			return false;	
	}
	
	/// 60,61 morestatements := <empty> | statement morestatements
	private void morestatments() throws IOException{
		addToParse("61");
		if(statement()){
			morestatments();
		}
		else
			removeLastRule(3);
			addToParse("60");
	}
	
	/// Gets the next token
	private void nextToken() throws IOException{
		currentToken = scanner.findToken();
		stringSofar += currentToken.getId() + " ";
	}

	
	private void addToParse(String number){
		parseTree += number + ",";
	}
	
	private void removeLastRule(int length)
	{
		parseTree = parseTree.substring(0, parseTree.length() - length);
	}
	
	private void error(String rule){
		System.out.println(stringSofar);
		System.out.println("Error at token: " + currentToken.getId() + " in Rule: " + rule);
		System.out.println(parseTree.substring(0, parseTree.length() - 1));
		System.exit(-1);
	}
	
	private String locationToString(int l)
	{
		return "-" + Integer.toString(l) + "($fp)";
	}
}
