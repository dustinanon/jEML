package org.jeml.translator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Translator {
	private enum READER_STATE {
		Visibility, Declaration, MethodCall, Block
	};
	
	private static READER_STATE rstate_ = READER_STATE.Visibility;
	
	public static void translateFile(File input, File output) throws FileNotFoundException {
		FileReader freader = new FileReader(input);
		BufferedReader reader = new BufferedReader(freader);
		
		String line = null;
		final StringBuilder outstring = new StringBuilder();
		try {
			while((line = reader.readLine()) != null) {
				translate(line, outstring);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void translate(final String line, final StringBuilder output) {
		final int length = line.length();
		
		int c = 0;
		char chr = '\0';
		while (c < length) {
			chr = line.charAt(c);
			
			switch (rstate_) {
				/** translate visibility statements */
				case Visibility: {
					translateVisibilityStatements(chr, output);
					c++;
					
					break;
				}
				
				case Declaration: {
					switch (chr) {
						/** reserved for wildcard declaration 
						 * 	which will be used for things like:
						 * 	
						 * 	* map = ConcurrentHashMap<String, Vector<String>>();
						 * 
						 * 			to
						 * 
						 * 	ConcurrentHashMap<String, Vector<String>> map =
						 * 						ConcurrentHashMap<String, Vector<String>>();
						 */
						case '*': {
							break;
						}
						
						
					}
					break;
				}
			}
		}
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
			output.append(chr);
			rstate_ = READER_STATE.Declaration;
		}
	}
}
