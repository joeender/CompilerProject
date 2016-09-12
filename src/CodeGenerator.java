/// File writer to be used in the parser.  Has the prolog code, postlog code,
/// a method to write a string, and the method that writes code that creates a new line
/// in the MIPS output.

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CodeGenerator {
	
	BufferedWriter writer;
    
	public CodeGenerator(String name) throws IOException
	{
	    writer = new BufferedWriter( new FileWriter(name));
	}
	
	/// Writes what whatever is in the input of the parameter.
	public void write(String str) throws IOException
	{
		writer.write(str);
		writer.newLine();
	}

	/// Writes the prolog.
	public void writeProlog() throws IOException
	{
		writer.write("#Prolog:");
		writer.newLine();
		writer.write(".text");
		writer.newLine();
		writer.write(".globl  main");
		writer.newLine();
		writer.write("main:");
		writer.newLine();
		writer.write("move  $fp  $sp #frame pointer will be start of active stack");
		writer.newLine();
		writer.write("la  $a0  ProgStart");
		writer.newLine();
		writer.write("li  $v0  4");
		writer.newLine();
		writer.write("syscall");
		writer.newLine();
		writer.write("#End of Prolog");
		writer.newLine();
		writer.newLine();

	}

	/// Writes the postlog.
	public void writePostlog() throws IOException
	{
		writer.newLine();
		writer.write("#Postlog:");
		writer.newLine();
		writer.write("la  $a0  ProgEnd");
		writer.newLine();
		writer.write("li  $v0  4");
		writer.newLine();
		writer.write("syscall");
		writer.newLine();
		writer.write("li  $v0  10");
		writer.newLine();
		writer.write("syscall");
		writer.newLine();
		writer.write(".data");
		writer.newLine();
		writer.write("ProgStart:  .asciiz	\"Program Start\\n\"");
		writer.newLine();
		writer.write("ProgEnd:   .asciiz	\"Program  End\\n\"");
		writer.newLine();
		writer.write("NewLine:   .asciiz	\"\\n\"");
		writer.close();
	}
	
	/// Generates the code for a new line in the output.
	public void newLine() throws IOException
	{
		writer.write("la  $a0  NewLine");
		writer.newLine();
		writer.write("li  $v0  4");
		writer.newLine();
		writer.write("syscall");
		writer.newLine();
	}
}
