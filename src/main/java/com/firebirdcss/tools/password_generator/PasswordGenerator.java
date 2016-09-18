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

import com.firebirdcss.tools.password_generator.exceptions.StatisticalImprobabilityException;

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
	
	/* Worker Variables */
	private static char[] generatedPassword;
	private static List<Integer> emptyLocations = new ArrayList<>();
	
	/**
	 * The main entry point of the application.
	 * <p>
	 * Available Switches:
	 * 
	 * --first AULDS          - First Character must be of type (A = Alpha, N = Numeric and/or S = Special).
	 * --last AULDS           - Last Character must be of type (A = Alpha, N = Numeric and/or S = Special).
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
		int specLen = (specialsRequired != null ? (specialsRequired.length > minSpecials ? specialsRequired.length : minSpecials) : minSpecials);
		specLen += (firstTypeOf == null || charArrayContains(firstTypeOf, 'S') ? 0 : 1);
		specLen += (lastTypeOf == null || charArrayContains(lastTypeOf, 'S') ? 0 : 1);
		
		int lowLen = minLowers + (firstTypeOf == null || charArrayContains(firstTypeOf, 'L') ? 0 : 1);
		lowLen += (lastTypeOf == null || charArrayContains(lastTypeOf, 'L') ? 0 : 1);
		
		int uppLen = minUppers + (firstTypeOf == null || charArrayContains(firstTypeOf, 'U') ? 0 : 1);
		uppLen += (lastTypeOf == null || charArrayContains(lastTypeOf, 'U') ? 0 : 1);
		
		int digLen = minDigits + (firstTypeOf == null || charArrayContains(firstTypeOf, 'D') ? 0 : 1);
		digLen += (lastTypeOf == null || charArrayContains(lastTypeOf, 'D') ? 0 : 1);
		
		int baseLen = digLen + uppLen + lowLen + specLen;
		
		if (passwordLength < baseLen && (maxPasswordLength == null || maxPasswordLength > passwordLength)) {
			System.out.println("ERROR: The requirement(s) exceed the desired length of the password!");
			System.exit(1);
		}
		
		/* ********************************************************************** *
		 * Adjust the length of the generated password based on maxPasswordLength *
		 * *********************************************************************** */
		Random rnd = new Random();
		if (maxPasswordLength != null) {
			passwordLength += rnd.nextInt((maxPasswordLength + 1) - passwordLength);
		}
		
		/* ********************************************** *
		 * Setup worker variables for password generation *
		 * ********************************************** */
		generatedPassword = new char[passwordLength];
		for (int i = 0; i < passwordLength; i++) {
			emptyLocations.add(new Integer(i));
		}
		
		try {
			/* **************************************** *
			 * Place the location sensitive items first *
			 * **************************************** */
			if (firstTypeOf != null) {
				generatedPassword[emptyLocations.remove(0)] = getChar(new String(firstTypeOf));
			}
			if (lastTypeOf != null) {
				generatedPassword[emptyLocations.remove((emptyLocations.size() - 1))] = getChar(new String(lastTypeOf));
			}
			
			/* ************************ *
			 * Place the required items *
			 * ************************ */
			for (int i = 0; i < minDigits.intValue(); i++) {
				placeValue(getChar("D"));
			}
			for (int i = 0; i < minUppers.intValue(); i++) {
				placeValue(getChar("U"));
			}
			for (int i = 0; i < minLowers.intValue(); i++) {
				placeValue(getChar("L"));
			}
			for (int i = 0; i < minSpecials.intValue(); i++) {
				placeValue(getChar("S"));
			}
			
			/* ******************************* *
			 * Fill in the remaining locations *
			 * ******************************* */
			int locationsLeft = emptyLocations.size();
			for (int i = 0; i < locationsLeft; i++) {
				placeValue(getChar("ADS"));
			}
			
			/* ******************************** *
			 * Handle the results appropriately *
			 * ******************************** */
			putTextOnClipboard(new String(generatedPassword));
			
			String message = null;
			if (outputClipboardOnly) {
				message = "Your auto generated password is available on the clipboard.";
			} else {
				message = "Your auto generated password is: " + new String(generatedPassword) + "\nIt is also available on the clipboard.";
			}
			System.out.println(message);
			JOptionPane.showMessageDialog(null, message);
			
		} catch (StatisticalImprobabilityException e) {
			System.out.println(e.getMessage() + "\n~Application Terminated~\n");
			System.exit(1);
		}
	}
	
	/**
	 * Gets a random location from epmtyLocations removing it and returns it.
	 * 
	 * @return Returns the random location as {@link int}
	 */
	private static int randomLocation() {
		Random rnd = new Random();
		
		return emptyLocations.remove(rnd.nextInt(emptyLocations.size()));
	}
	
	/**
	 * Randomly selects a character by it's type.<br>
	 * Types are as follows:<br>
	 * A = Alpha, could be upper or lower case
	 * U = Upper-case alpha
	 * L = Lower-case alpha
	 * D = Digit 0-9
	 * S = Special
	 * 
	 * @param charTypes - The character type(s) from which to randomly choose as {@link String}
	 * @return Returns the randomly selected character as {@link char}
	 * @throws StatisticalImprobabilityException 
	 */
	private static char getChar(String charTypes) throws StatisticalImprobabilityException {
		Random rnd = new Random();
		
		char charType = (char) rnd.nextInt(charTypes.length());
		switch (charTypes.toUpperCase().charAt(charType)) {
			case 'A':
				if (rnd.nextBoolean()) {
					
					return (char) (rnd.nextInt(('z' - 'a')/*BlockWidth*/ + 1) + 'a'/*BlockStart*/);
				} 
				
				return (char) (rnd.nextInt(('Z' - 'A')/*BlockWidth*/ + 1) + 'A'/*BlockStart*/);
			case 'U':
				
				return (char) (rnd.nextInt(('Z' - 'A')/*BlockWidth*/ + 1) + 'A'/*BlockStart*/);
			case 'L':
				
				return (char) (rnd.nextInt(('z' - 'a')/*BlockWidth*/ + 1) + 'a'/*BlockStart*/);
			case 'D':
				
				return (char) (rnd.nextInt(('9' - '0')/*BlockWidth*/ + 1) + '0'/*BlockStart*/);
			case 'S':
				if (specialsRequired != null) {
				
					return getAndRemoveSpecial();
				} else {
					if (specialsOnly != null) {
						
						return specialsOnly[rnd.nextInt(specialsOnly.length)];
					} else {
						char special = 0;
						int cycles = 0;
						do {
							if (cycles > 2000) {
								throw new StatisticalImprobabilityException("No satisfactory special character has been found in '" + cycles + "' cycles!");
							}
							cycles ++;
							switch (rnd.nextInt(4)) {
								case 0:
									
									special = (char) (rnd.nextInt(15/*BlockWidth*/ + 1) + 33/*BlockStart*/);
								case 1:
									
									special = (char) (rnd.nextInt(7/*BlockWidth*/ + 1) + 58/*BlockStart*/);
								case 2:
									
									special = (char) (rnd.nextInt(6/*BlockWidth*/ + 1) + 91/*BlockStart*/);
								case 4:
									
									special = (char) (rnd.nextInt(4/*BlockWidth*/ + 1) + 123/*BlockStart*/);
							}
						} while (specialsNot != null && String.valueOf(specialsNot).contains(String.valueOf(special)));
						
						return special;
					}
				}
			// break; <---- Unreachable Break would go here.
		}
		
		return (char) 0;
	}
	
	/**
	 * Checks to see if a given character array contains a specified character.
	 * 
	 * @param array - The array in which to look for the given character, as {@link char} array
	 * @param chr - The character to look for as {@link char}
	 * @return Returns true as {@link boolean} if character is found in the array otherwise false.
	 */
	private static boolean charArrayContains(char[] array, char chr) {
		if (array != null) {
			
			return (new String(array)).toLowerCase().contains(Character.toString(chr).toLowerCase());
		}
		
		return false;
	}
	
	/**
	 * Parses and unpacks the passed Arguments into worker variables for use in controlling
	 * the password generation process.
	 * 
	 * @param args - Program arguments as a {@link String} array
	 */
	private static void unpackArguments(String[] args) {
		Map<String/*Switches*/, String/*SwitchArgs*/> parsedArguments = null;
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
		if (args != null && args.length > 0) {
			Map<String/*Switch*/, String/*Args*/> results = new HashMap<>();
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-")) {
					if (args.length > i && !args[i + 1].startsWith("-")) {
						results.put(args[i], args[i + 1]);
						i ++;
					} else {
						results.put(args[i], null);
					}
				}
			}
			
			return results;
		}
		
		return null;
	}
	
	/**
	 * Displays the usage syntax for this application.
	 */
	private static void programUsage() {
		String message = ""
			+ "usage: password-generator [options]\n"
			+ "Options:\n\n"
			
			+ "-h|--help\n"
					+ "\t\tPrints the program's usage to the screen.\n"
			+ "-v|--version\n"
					+ "\t\tPrints the application's version information to the screen.\n"
			+ "--first <AULDS>\n"
					+ "\t\tUsed to force the first character to be of a specific type:\n"
					+ "\t\tA = Alpha (any case)\n"
					+ "\t\tU = Upper-case\n"
					+ "\t\tL = Lower-case\n"
					+ "\t\tD = Digit\n"
					+ "\t\tS = Special\n"
			+ "--last <AULDS>\n"
					+ "\t\tUsed to force the last character to be of a specific type:\n"
					+ "\t\tA = Alpha (any case)\n"
					+ "\t\tU = Upper-case\n"
					+ "\t\tL = Lower-case\n"
					+ "\t\tD = Digit\n"
					+ "\t\tS = Special\n"
			+ "--length <###>\n"
					+ "\t\tSpecifies the desired length of the password (minimum length if used with --max-length).\n"
			+ "--max-length <###>\n"
					+ "\t\tSpecifies the maximum length of the password, actual length will be randomly chosen.\n"
			+ "-d|--digit <###>\n"
					+ "\t\tSpecifies if and at least how many digit(s) are required.\n"
			+ "-u|--upper <###>\n"
					+ "\t\tSpecifies if and at least how many upper-case character(s) are required.\n"
			+ "-l|--lower <###>\n"
					+ "\t\tSpecifies if and at least how many lower-case character(s) are required.\n"
			+ "-s|--special <###>\n"
					+ "\t\tSpecifies if and at least how many special character(s) are required.\n"
			+ "--specials-only <***>\n"
					+ "\t\tSpecifies the set valid special characters.\n"
			+ "--specials-not <***>\n"
					+ "\t\tExcludes a specific set of special characters from use.\n"
			+ "--specials-required <***>\n"
					+ "\t\tSpecifies a set of specials that must be used at least once.\n"
			+ "-r|--repeat <###>\n"
					+ "\t\tMaximum number of sequentially repeating characters.\n"
			+ "-c|--clipboard-only\n"
					+ "\t\tSpecifies that generated password should only appear on clipboard and not be displayed.\n";
		System.out.println(message);
	}
	
	/**
	 * Places a given {@link Character} in a random location of the
	 * password which is being generated.
	 * 
	 * @param character - The character to place as {@link Character}
	 */
	private static void placeValue(char character) {
		generatedPassword[randomLocation()] = character;
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
	
	private static char getAndRemoveSpecial() {
		Random rnd = new Random();
		int rndNum = rnd.nextInt(specialsRequired.length);
		int rndNumIndex = 0;
		char selectedChar = 0;
		char[] newArray = new char[specialsRequired.length - 1];
		for (int i = 0; i < specialsRequired.length; i++) {
			if (i != rndNum) {
				newArray[rndNumIndex ++] = specialsRequired[i];
			} else {
				selectedChar = specialsRequired[i];
			}
		}
		
		specialsRequired = newArray;
		
		return  selectedChar;
	}
}
