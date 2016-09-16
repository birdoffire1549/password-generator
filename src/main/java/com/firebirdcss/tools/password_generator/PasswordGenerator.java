package com.firebirdcss.tools.password_generator;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.activation.DataHandler;
import javax.swing.JOptionPane;

/**
 * This tool was created to aid in the generating of a random password which meets a
 * wide variety of complexity requirements.
 * <p>
 * This is a rough draft at this point, and not yet finished...
 * 
 * @author Scott Griffis
 *
 */
public class PasswordGenerator {
	private static final String DEFAULT_UNUSABLE_SPECIALS = ";&@\\/\'";
	private static final String DEFAULT_REQUIRED_SPECIALS = "!#$%()*+,-:<=>?_";
	
	/* Minimum default Thresholds */
	private static final int DEFAULT_MIN_PASSWORD_LENGTH = 8;
	private static final int DEFAULT_MIN_DIGIT_COUNT = 2;
	private static final int DEFAULT_MIN_SPECIAL_COUNT = 1;
	private static final int DEFAULT_MIN_LOWER_COUNT = 1;
	private static final int DEFAULT_MIN_UPPER_COUNT = 1;
	
	/* Maximum default Thresholds */
	private static final int DEFAULT_MAX_PW_LENGTH = 12;
	
	/* Worker Variables */
	private static char[] generatedPassword;
	private static List<Integer> emptyLocations = new ArrayList<>();
	
	/**
	 * The main entry point of the application.
	 * 
	 * @param args - The arguments are not used by this application.
	 */
	public static void main(String[] args) {
		Random rnd = new Random();
		
		generatedPassword = new char[(DEFAULT_MIN_PASSWORD_LENGTH + rnd.nextInt(DEFAULT_MAX_PW_LENGTH - DEFAULT_MIN_PASSWORD_LENGTH))];
		for (int i = 0; i < generatedPassword.length; i++) {
			emptyLocations.add(new Integer(i));
		}
		
		/* Find and place required minimum digits */
		for (int i = 0; i < DEFAULT_MIN_DIGIT_COUNT; i++) {
			placeValue((char) (rnd.nextInt(10) + '0'));
		}
		
		/* Find and place required special characters */
		for (int i = 0; i < DEFAULT_MIN_SPECIAL_COUNT; i++) {
			char character = DEFAULT_REQUIRED_SPECIALS.charAt(rnd.nextInt(DEFAULT_REQUIRED_SPECIALS.length()));
			placeValue(character);
		}
		
		/* Find and place required upper-case characters */
		for (int i = 0; i < DEFAULT_MIN_UPPER_COUNT; i++) {
			char character = (char) (rnd.nextInt('Z' - 'A' + 1/*BlockWidth*/) + 'A'/*BlockStart*/);
			placeValue(character);
		}
		
		/* Find and place required lower-case characters */
		for (int i = 0; i < DEFAULT_MIN_LOWER_COUNT; i++) {
			char character = (char) (rnd.nextInt('z' - 'a' + 1/*BlockWidth*/) + 'a'/*BlockStart*/);
			placeValue(character);
		}
		
		/* Find and place the remaining characters */
		int tempEmptyLocationsSize = emptyLocations.size();
		for (int i = 0; i < tempEmptyLocationsSize; i++) {
			boolean characterFound = false;
			char character = 0;
			
			/* Find acceptable lower-case character */
			while (!characterFound) {
				int value = (rnd.nextInt(93/*BlockWidth*/) + 33/*BlockStart*/);
				if (!DEFAULT_UNUSABLE_SPECIALS.contains(String.valueOf((char) value))) {
					character = (char) value;
					characterFound = true;
				}
			}
			
			placeValue(character);
		}
		
		putTextOnClipboard(new String(generatedPassword));
		JOptionPane.showMessageDialog(null, "Your auto generated password is: " + new String(generatedPassword) + "\nIt is also available on the clipboard.");
	}
	
	/**
	 * Places a given {@link Character} in a random location of the
	 * password which is being generated.
	 * 
	 * @param character - The character to place as {@link Character}
	 */
	private static void placeValue(char character) {
		Random rnd = new Random();
		generatedPassword[emptyLocations.remove(rnd.nextInt(emptyLocations.size()))] = character;
	}
	
	/**
	 * Puts the passed text onto the system clipboard.
	 * 
	 * @param text - The text to place on the clipboard as {@link String}
	 */
	private static void putTextOnClipboard(String text) {
		Transferable trans = new DataHandler(text, DataFlavor.getTextPlainUnicodeFlavor().getMimeType());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
	}
}
