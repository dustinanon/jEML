package org.jeml.translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Translator {
	private static enum READER_STATE {
		Visibility, Declaration, MethodCall, Block, CommentBlock
	}
	
	private static READER_STATE 		rstate_ 	= READER_STATE.Visibility;
	
	public static void translateFile(File input, File output) throws FileNotFoundException {
		FileReader freader = new FileReader(input);
		BufferedReader reader = new BufferedReader(freader);
		
		String line = null;
		final StringBuilder outstring = new StringBuilder();
		try {
			while((line = reader.readLine()) != null) {
				translate(line, outstring);
				outstring.append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void translate(final String line, final StringBuilder output) {
		final int length = line.length();
		
		int c = 0;
		char chr = '\0';
		
		
		
		/** loop through each character in the line
		 * 	for translation
		 */
		while (c < length) {
			chr = line.charAt(c);
			
			/** make a check for comments */
			if (chr == '/') {
				final char nextchr = line.charAt(c + 1);
				if (nextchr == '/') {
					/** we have a full line comment, so just add the rest of the comment and return */
					output.append(line.substring(c));
					break;
				} else if (nextchr == '*') {
					/** we just started a comment block, so read until we find the end sequence */
					rstate_ = READER_STATE.CommentBlock;
				}
			}
				
			
			switch (rstate_) {
				/** translate visibility statements */
				case Visibility: {
					translateVisibilityStatements(chr, output);
					break;
				}
				
				/** translate declaration statements */
				case Declaration: {
					translateDeclarationStatements(line.substring(c), output);
					break;
				}
				
				/** read the comment block until we find the end sequence */
				case CommentBlock: {
					/** since we're in a comment block, just go ahead and add the character */
					output.append(chr);
					
					/** check for the first char of the end sequence */
					if (chr == '*') {
						final char nextchr = line.charAt(c + 1);
						/** check for the second char of the end sequence */
						if (nextchr == '/') {
							output.append(nextchr);
							rstate_ = READER_STATE.Visibility;
						}
					}
					
					break;
				}
			}
			
			c++;
		}
	}

	private static enum DECLARATION_TYPE {
		Class, Method, Variable
	}
	
	private static enum DECLARATION_STATE {
		Type, Name, Arguments, Addendums //addendums? lol, what are those called in general? basically throwing exceptions and extensions/implementations
	}
	
	private static DECLARATION_STATE 	decstate_ 	= DECLARATION_STATE.Type;
	private static DECLARATION_TYPE		dectype_ 	= DECLARATION_TYPE.Class; //assume that the first declaration we find will be a class?
	
	private static void translateDeclarationStatements(String string,
			StringBuilder output) {
		
		switch (decstate_) {
			case Type: {
				break;
			}
			
			case Name: {
				break;
			}
			
			case Arguments: {
				break;
			}
			
			case Addendums: {
				break;
			}
		}
		
		
		//TODO: Change the parsing to implement the wildcard... need to figure out an elegant way to handle the rest of the string.
//		switch (string) {
//			/** reserved for wildcard declaration 
//			 * 	which will be used for things like:
//			 * 	
//			 * 	* map = ConcurrentHashMap<String, Vector<String>>();
//			 * 
//			 * 			to
//			 * 
//			 * 	ConcurrentHashMap<String, Vector<String>> map =
//			 * 						ConcurrentHashMap<String, Vector<String>>();
//			 */
//			case '*': {
//				break;
//			}
//		}
	}

	private static void translateVisibilityStatements(char chr, StringBuilder output) {
		switch (chr) {
			/** private */
			case '-': {
				output.append("private ");
				break;
			}
			
			/** public */
			case '+': {
				output.append("public ");
				break;
			}
			
			/** protected */
			case '#': {
				output.append("protected ");
				break;
			}
			
			/** static */
			case '^': {
				output.append("static ");
				break;
			}
				
			/** final */
			case '!': {
				output.append("final ");
				break;
			}
			
			/** ignore spaces for now, we'll consider:
			 * 	!^+  ==  ! ^ +
			 */
			case ' ': {
				break;
			}
				
			/** this isn't a visibility declaration, advance the state */
			default: {
				//output.append(chr);
				//translateDeclarationStatements(line.substring(c), output);
				rstate_ = READER_STATE.Declaration;
			}
		}
	}
}
