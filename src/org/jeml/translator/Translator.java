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
		while (c < length) {
			switch (rstate_) {
				case Visibility:
					switch (line.charAt(c)) {
						/** private */
						case '-':
							output.append("private ");
							break;
							
						/** public */
						case '+':
							output.append("public ");
							break;
							
						/** protected */
						case '#':
							output.append("protected ");
							break;
							
						/** static */
						case '^':
							output.append("static ");
							break;
							
						/** final */
						case '!':
							output.append("final ");
							break;
						
						/** this isn't a visibility declaration, advance the state */
						default:
							output.append("private ");
							output.append(line.charAt(c));
							rstate_ = READER_STATE.Declaration;
					}
					
					c++;
					
					break;
			}
		}
	}
}
