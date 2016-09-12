/// Jialiang Zhong

/// This main driver for the parser which takes input code and generates MIPS code.
/// It it uses a scanner which creates tokens that allows the parser to check for 
/// proper grammar and takes ID declarations and places them in a symbol table.


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;


public class main {
	public static void main(String[] args) throws IOException {
		Parser parser = new Parser("test.txt");
	}
}
