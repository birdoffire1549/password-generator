package com.firebirdcss.tools.password_generator;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.activation.DataHandler;
import javax.swing.JOptionPane;

/**
 * This tool was created to aid in the generating of a random password which meets a
 * wide variety of complexity requirements.
 * 
 * @author Scott Griffis
 *
 */
public class PasswordGenerator {
	private static boolean outputClipboardOnly = false;
	private static char[] firstTypeOf = null;
	private static char[] lastTypeOf = null;
	private static Integer maxSeqRep = null;
	private static Integer passwordLength = new Integer(8);
	private static Integer maxPasswordLength = null;
	private static Integer minDigits = new Integer(1);
	private static Integer minUppers = new Integer(1);
	private static Integer minLowers = new Integer(1);
	private static Integer minSpecials = new Integer(1);
	
	private static char[] specialsOnly = null;
	private static char[] specialsNot = null;
	private static char[] specialsRequired = null;
	
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
	 * <p>
	 * Available Switches:
	 * 
	 * --first ANS          - First Character must be of type (A = Alpha, N = Numeric and/or S = Special).
	 * --last ANS           - Last Character must be of type (A = Alpha, N = Numeric and/or S = Special).
	 *  -r | --repeat ##    - Maximum number of sequentially repeating characters. 
	 * --length ##          - Specific length for generated password.
	 * --max-length ###     - Generated password will randomly be between specified length and max-length.
	 *  -d | --digit ###    - Specifies if and at least how many digit(s) are required.
	 *  -u | --upper ###    - Specifies if and at least how many upper-case character(s) are required.
	 *  -l | --lower ###    - Specifies if and at least how many lower-case character(s) are required.
	 *  -s | --special ###  - Specifies if and at least how many special character(s) are required.
	 * --specials-only ***  - Specifies the set valid special characters.
	 * --specials-not ***   - Excludes a specific set of special characters from use.
	 * --specials-required ***  - Specifies a set of specials that must be used at least once.
	 *  -c | --clipboard-only   - Specifies that generated password should only appear on clipboard and not be displayed.
	 *  -h | --help - Displays the program usage syntax.
	 * 
	 * @param args - An array of switches which direct application's behavior as {@link String} array.
	 */
	public static void main(String[] args) {
		unpackArguments(args);
		
		/* ************************************************************************************** *
		 * Sanity check that given max and min values for options don't break for password length *
		 * ************************************************************************************** */
		
		/* ***************************************** *
		 * Place the placement sensitive items first *
		 * ***************************************** */
		
		/* ************************ *
		 * Place the required items *
		 * ************************ */
		
		/* ******************************* *
		 * Fill in the remaining locations *
		 * ******************************* */
		
		/* ******************************** *
		 * Handle the results appropriately *
		 * ******************************** */
			
//			generatedPassword = new char[(DEFAULT_MIN_PASSWORD_LENGTH + rnd.nextInt(DEFAULT_MAX_PW_LENGTH - DEFAULT_MIN_PASSWORD_LENGTH))];
//			for (int i = 0; i < generatedPassword.length; i++) {
//				emptyLocations.add(new Integer(i));
//			}
//			
//			/* Find and place required minimum digits */
//			for (int i = 0; i < DEFAULT_MIN_DIGIT_COUNT; i++) {
//				placeValue((char) (rnd.nextInt(10) + '0'));
//			}
//			
//			/* Find and place required special characters */
//			for (int i = 0; i < DEFAULT_MIN_SPECIAL_COUNT; i++) {
//				char character = DEFAULT_REQUIRED_SPECIALS.charAt(rnd.nextInt(DEFAULT_REQUIRED_SPECIALS.length()));
//				placeValue(character);
//			}
//			
//			/* Find and place required upper-case characters */
//			for (int i = 0; i < DEFAULT_MIN_UPPER_COUNT; i++) {
//				char character = (char) (rnd.nextInt('Z' - 'A' + 1/*BlockWidth*/) + 'A'/*BlockStart*/);
//				placeValue(character);
//			}
//			
//			/* Find and place required lower-case characters */
//			for (int i = 0; i < DEFAULT_MIN_LOWER_COUNT; i++) {
//				char character = (char) (rnd.nextInt('z' - 'a' + 1/*BlockWidth*/) + 'a'/*BlockStart*/);
//				placeValue(character);
//			}
//			
//			/* Find and place the remaining characters */
//			int tempEmptyLocationsSize = emptyLocations.size();
//			for (int i = 0; i < tempEmptyLocationsSize; i++) {
//				boolean characterFound = false;
//				char character = 0;
//				
//				/* Find acceptable lower-case character */
//				while (!characterFound) {
//					int value = (rnd.nextInt(93/*BlockWidth*/) + 33/*BlockStart*/);
//					if (!DEFAULT_UNUSABLE_SPECIALS.contains(String.valueOf((char) value))) {
//						character = (char) value;
//						characterFound = true;
//					}
//				}
//				
//				placeValue(character);
//			}
//			
//			putTextOnClipboard(new String(generatedPassword));
//			JOptionPane.showMessageDialog(null, "Your auto generated password is: " + new String(generatedPassword) + "\nIt is also available on the clipboard.");
		
	}
	
	/**
	 * Parses and unpacks the passed Arguments into worker variables for use in controlling
	 * the password generation process.
	 * 
	 * @param args - Program arguments as a {@link String} array
	 */
	private static void unpackArguments(String[] args) {
		Map<String/*Switches*/,String/*SwitchArgs*/> parsedArguments = null;
		if ((parsedArguments = parseArguments(args)) != null) {
			for (Entry<String/*Switch*/, String/*Arguments*/> entry : parsedArguments.entrySet()) {
				switch (entry.getKey()) {
					case "--first":
						firstTypeOf = entry.getKey().toCharArray();
						break;
					case "--last":
						lastTypeOf = entry.getValue().toCharArray();
						break;
					case "-r":
					case "--repeat":
						maxSeqRep = new Integer(entry.getValue());
						break;
					case "--length":
						passwordLength = new Integer(entry.getValue());
						break;
					case "--max-length":
						maxPasswordLength = new Integer(entry.getValue());
						break;
					case "-d":
					case "--digit":
						minDigits = new Integer(entry.getValue());
						break;
					case "-u":
					case "--upper":
						minUppers = new Integer(entry.getValue());
						break;
					case "-l":
					case "--lower":
						minLowers = new Integer(entry.getValue());
						break;
					case "-s":
					case "--special":
						minSpecials = new Integer(entry.getValue());
						break;
					case "--specials-only":
						specialsOnly = entry.getValue().toCharArray();
						break;
					case "--specials-not":
						specialsNot = entry.getValue().toCharArray();
						break;
					case "--specials-required":
						specialsRequired = entry.getValue().toCharArray();
						break;
					case "-h":
					case "--help":
						programUsage();
						System.exit(0);
						break;
					case "-c":
					case "--clipboard-only":
						outputClipboardOnly = true;
						break;
					default:
						break;
				}
			}
		}
	}
	
	/**
	 * Handles parsing and setting up the application to run in accordance
	 * to the supplied arguments.
	 *
	 * @param args - The supplied arguments as a {@link String} array
	 * @return
	 */
	private static Map<String, String> parseArguments(String[] args) {
		
		return new HashMap<>();
	}
	
	/**
	 * Displays the usage syntax for this application.
	 */
	private static void programUsage() {
		
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
