/*
 * Name: Jung Yoon
 * UTEID: jey283
 * CS Account: jungyoon
 * E-MAIL: jungyoon@utexas.edu
 * Assignment: Assignment 5
 * Purpose: Password Crack - The goal of this assignment is to implement a portion of 
 * Crack's functionality and attempt to guess one or more passwords. 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PasswordCrack {
	private static Mangler mangle = new Mangler();
	private static jcrypt jcrypter = new jcrypt();
	private static List<String> possibleWords = new ArrayList<String>();
	private static List<String> possibleNames = new ArrayList<String>();
	private static Map<String, String[]> users = new HashMap<String, String[]>();
	private static int totalPasswords = 0;
	private static int totalCrackedPasswords = 0;
	private static long startTime = 0;
	private static long currentTime = 0;
	private static long oldTime = 0;
	
	public static void main (String[] args) throws IOException{
		BufferedReader dictionaryFile = null;
		BufferedReader passwordsFile = null;
		String dictionaryWord = "";
		String userLine = "";
		double time = 0;
		
		if(args.length == 2){
			dictionaryFile = new BufferedReader(new FileReader(args[0]));
			passwordsFile = new BufferedReader(new FileReader(args[1]));
		
		
			/*dictionaryFile = new BufferedReader(new FileReader("newshort-words.txt"));
			passwordsFile = new BufferedReader(new FileReader("passwd2.txt"));
			
			startTime = System.currentTimeMillis();
			oldTime = 0;
			*/

			
			// stores all words into list
			while((dictionaryWord = dictionaryFile.readLine()) != null)
				possibleWords.add(dictionaryWord.toLowerCase());
		
			// access users
			while((userLine = passwordsFile.readLine()) != null){
				totalPasswords++;
				// split line by delimiter :
				String[] userData = userLine.split("\\:");
				String[] neededUserData = new String[4];
				neededUserData[0] = userData[4];
				neededUserData[1] = userData[1].substring(0,2);
				neededUserData[2] = userData[1];
				neededUserData[3] = "Uncracked";
					//System.out.println(Arrays.toString(neededUserData));
					
				users.put(userData[0].toString(), neededUserData);
				
				// split name by space(s)
				String[] name = userData[4].split("\\ ");
				for(int i=0; i<name.length; i++){
					possibleNames.add(name[i].toLowerCase());
				}
				possibleNames.add(name[0]+name[1]);
				possibleNames.add(name[1]+name[0]);
				// tests name mangles for each user, individually
				nameRound(userData[0]);
				possibleNames = new ArrayList<String>();
			}
			
			// *************** PASSWORD CRACKING TESTS ***************
			// tests 1-mangles, split into rounds by efficiency
			firstRound();
			secondRound();
			// tests 2-mangles, split into rounds by efficiency
			thirdRound();
			fourthRound(); 
			sixthRound(); // mangle1 + prepend
			fifthRound(); // mangle1 + append
			// tests a select few 3-mangles
			seventhRound(); 
			// re-tests names with more in-depth search
			nameRoundTwo(); 
			eighthRound(); 
		
			// *************** DEBUGGING: PRINTS OUT TIME ***************
			/*
			time = System.currentTimeMillis() - startTime;
			System.out.println("********************************" +
					"\n******* PROGRAM SUMMARY: *******");
			System.out.println("* Time Elapsed: " + time + " ms");
			System.out.println("* Cracked " + totalCrackedPasswords 
					+ " out of " + totalPasswords + " passwords");
			System.out.println("********************************");
			*/
			
			
		}
		else {
	  		System.err.println("ERROR: invalid number of arguments.");
	  		return; 
	  	}
	  	
		
		dictionaryFile.close();
		passwordsFile.close();
	}

	/*********************
	* METHOD: nameRound
	* 	Purpose: runs through manglings of names
	* 	for each individual user.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void nameRound(String key) {
		String salt = users.get(key)[1];
		String actualPassword = users.get(key)[2];
		boolean exit = false;
		
		// run tests for all possible words
		for(String word : possibleNames){
			exit = false;
			if(jcrypt.crypt(salt, word).equals(actualPassword)){
				printCrackedPassword(key, word);
				break;
			}
			if(jcrypt.crypt(salt, mangle.capitalize(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.capitalize(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.reverse(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.reverse(word));
				break;
			}	
			if(jcrypt.crypt(salt, mangle.ncapitalize(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.ncapitalize(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.uppercase(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.uppercase(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.duplicate(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.duplicate(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.reflectForward(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.reflectForward(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.reflectBackward(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.reflectBackward(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.deleteFirstChar(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.deleteFirstChar(word));
				break;
			}	
			if(jcrypt.crypt(salt, mangle.deleteLastChar(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.deleteLastChar(word));
				break;
			}	
			if(jcrypt.crypt(salt, mangle.toggleEndsLower(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.toggleEndsLower(word));
				break;
			}	
			if(jcrypt.crypt(salt, mangle.toggleEndsUpper(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.toggleEndsUpper(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.toggleOdd(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.toggleOdd(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.toggleEven(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.toggleEven(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.toggleEndsLower(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.toggleEndsLower(word));
				break;
			}
			if(jcrypt.crypt(salt, mangle.toggleEndsUpper(word)).equals(actualPassword)){
				printCrackedPassword(key, mangle.toggleEndsUpper(word));
				break;
			}	
			for (int ch = 32; ch <=126; ch++) {
				if(jcrypt.crypt(salt, mangle.appendChar(word, (char)ch)).equals(actualPassword)){
					printCrackedPassword(key, mangle.appendChar(word, (char)ch));
					exit = true;
					break;
				}
				if(exit)
					break;
			}
			for (int ch = 32; ch <=126; ch++) {
				if(jcrypt.crypt(salt, mangle.prependChar(word, (char)ch)).equals(actualPassword)){
					printCrackedPassword(key, mangle.prependChar(word, (char)ch));
					exit = true;
					break;
				}
				if(exit)
					break;
			}
		}
	}
	
	
	/*********************
	* METHOD: nameRoundTwo
	* 	Purpose: runs through 2-manglings of names
	* 	for each individual user.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void nameRoundTwo() {
		boolean exit = false;
		
		for(String word : possibleNames){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					// deleteLastChar + deleteLastChar
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.deleteLastChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.deleteLastChar(word)));
						break;
					} 
					// deleteLastChar + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteLastChar + uppercase
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.uppercase(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.uppercase(word)));
						break;
					} 
					// deleteLastChar + capital
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.capitalize(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.capitalize(word)));
						break;
					}
					// deleteLastChar + n-capital
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.ncapitalize(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.ncapitalize(word)));
						break;
					}
					// deleteLastChar + toggleEven
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.toggleEven(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.toggleEven(word)));
						break;
					}
					// deleteLastChar + toggleOdd
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.toggleOdd(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.toggleOdd(word)));
						break;
					}
					// deleteFirstChar + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + uppercase
					if(jcrypt.crypt(salt, mangle.uppercase(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.uppercase(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + capital
					if(jcrypt.crypt(salt, mangle.capitalize(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.capitalize(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + n-capital
					if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.ncapitalize(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + toggleEven
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.toggleEven(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.toggleEven(word)));
						break;
					}
					// deleteFirstChar + toggleOdd
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.toggleOdd(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.toggleOdd(word)));
						break;
					}
					// duplicate, reflectForward, reflectBackward
					if(word.length() < 5){
						if(jcrypt.crypt(salt, mangle.reflectForward(mangle.reverse(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectForward(mangle.reverse(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectBackward(mangle.reverse(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectBackward(mangle.reverse(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.duplicate(mangle.reverse(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.duplicate(mangle.reverse(word)));
							break;
						}
						if(jcrypter.crypt(salt, mangle.toggleEven(mangle.reflectForward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleEven(mangle.reflectForward(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.reflectForward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleOdd(mangle.reflectForward(word)));
							break;
						}
						if(jcrypt.crypt(salt, mangle.toggleEven(mangle.reflectBackward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleEven(mangle.reflectBackward(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.reflectBackward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleOdd(mangle.reflectBackward(word)));
							break;
						}
						if(jcrypt.crypt(salt, mangle.toggleEven(mangle.duplicate(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleEven(mangle.duplicate(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.duplicate(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleOdd(mangle.duplicate(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.duplicate(mangle.deleteFirstChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.duplicate(mangle.deleteFirstChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.duplicate(mangle.deleteLastChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.duplicate(mangle.deleteLastChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectForward(mangle.deleteFirstChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectForward(mangle.deleteFirstChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectForward(mangle.deleteLastChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectForward(mangle.deleteLastChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectBackward(mangle.deleteFirstChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectBackward(mangle.deleteFirstChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectBackward(mangle.deleteLastChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectBackward(mangle.deleteLastChar(word)));
							break;
						}
					}
					
					// deleteLastChar + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.deleteLastChar(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.deleteLastChar(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
						
					}
					// deleteFirstChar + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.deleteFirstChar(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.deleteFirstChar(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// capital + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.capitalize(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.capitalize(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// n-capital + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.ncapitalize(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.ncapitalize(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// reverse + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.reverse(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.reverse(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// append + reverse
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.reverse(mangle.appendChar(word, (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reverse(mangle.appendChar(word, (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// upper + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.uppercase(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.uppercase(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// append + upper
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.uppercase(mangle.appendChar(word, (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.uppercase(mangle.appendChar(word, (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// toggleEven + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.toggleEven(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.toggleEven(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// toggleOdd + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.toggleOdd(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.toggleOdd(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
				}
			}
		}
	}

	/*********************
	* METHOD: firstRound
	* 	Purpose: runs through first round of mangling.
	* 	That is, it tests the mangles that are the
	* 	least expensive. 
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void firstRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					if(jcrypt.crypt(salt, word).equals(actualPassword)){
						printCrackedPassword(key, word);
						break;
					}
					if(jcrypt.crypt(salt, mangle.capitalize(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.capitalize(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.reverse(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.reverse(word));
						break;
					}	
					if(jcrypt.crypt(salt, mangle.ncapitalize(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.ncapitalize(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.uppercase(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.uppercase(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.duplicate(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.duplicate(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.reflectForward(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.reflectForward(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.reflectBackward(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.reflectBackward(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(word));
						break;
					}	
					if(jcrypt.crypt(salt, mangle.deleteLastChar(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(word));
						break;
					}	
				}
			}
		}
	}

	/*********************
	* METHOD: secondRound
	* 	Purpose: runs through second round of mangling.
	* 	That is, it tests the mangles that are more
	*	expensive than the firstRound.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void secondRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					if(jcrypt.crypt(salt, mangle.toggleEndsLower(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.toggleEndsLower(word));
						break;
					}	
					if(jcrypt.crypt(salt, mangle.toggleEndsUpper(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.toggleEndsUpper(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.toggleOdd(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.toggleOdd(word));
						break;
					}
					if(jcrypt.crypt(salt, mangle.toggleEven(word)).equals(actualPassword)){
						printCrackedPassword(key, mangle.toggleEven(word));
						break;
					}
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(word, (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(word, (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(word, (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(word, (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
				}
			}
		}
	}
	
	
	/*********************
	* METHOD: thirdRound
	* 	Purpose: runs through third round of mangling.
	* 	These tests have two mangles.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void thirdRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					// capitalize + reverse
					if(jcrypt.crypt(salt, mangle.reverse(mangle.capitalize(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.reverse(mangle.capitalize(word)));
						break;
					} 
					// reverse + capitalize
					if(jcrypt.crypt(salt, mangle.capitalize(mangle.reverse(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.capitalize(mangle.reverse(word)));
						break;
					}	
					// n-capitalize + reverse
					if(jcrypt.crypt(salt, mangle.reverse(mangle.ncapitalize(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.reverse(mangle.ncapitalize(word)));
						break;
					} 
					// reverse + n-capitalize
					if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.reverse(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.ncapitalize(mangle.reverse(word)));
						break;
					}
					// reverse + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.reverse(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.reverse(word)));
						break;
					}	
					// deleteFirstChar + reverse
					if(jcrypt.crypt(salt, mangle.reverse(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.reverse(mangle.deleteFirstChar(word)));
						break;
					} 
					// reverse + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.reverse(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.reverse(word)));
						break;
					}	
					// deleteFirstChar + reverse
					if(jcrypt.crypt(salt, mangle.reverse(mangle.deleteLastChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.reverse(mangle.deleteLastChar(word)));
						break;
					} 
					// reverse + toggleEven
					if(jcrypt.crypt(salt, mangle.toggleEven(mangle.reverse(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.toggleEven(mangle.reverse(word)));
						break;
					}	
					// reverse + toggleOdd
					if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.reverse(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.toggleOdd(mangle.reverse(word)));
						break;
					}	
				}
			}
		}
	}
	
	/*********************
	* METHOD: fourthRound
	* 	Purpose: runs through third round of mangling.
	* 	These tests have two mangles.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void fourthRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					// deleteLastChar + deleteLastChar
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.deleteLastChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.deleteLastChar(word)));
						break;
					} 
					// deleteLastChar + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteLastChar + uppercase
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.uppercase(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.uppercase(word)));
						break;
					} 
					// deleteLastChar + capital
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.capitalize(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.capitalize(word)));
						break;
					}
					// deleteLastChar + n-capital
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.ncapitalize(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.ncapitalize(word)));
						break;
					}
					// deleteLastChar + toggleEven
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.toggleEven(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.toggleEven(word)));
						break;
					}
					// deleteLastChar + toggleOdd
					if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.toggleOdd(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteLastChar(mangle.toggleOdd(word)));
						break;
					}
					// deleteFirstChar + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + uppercase
					if(jcrypt.crypt(salt, mangle.uppercase(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.uppercase(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + capital
					if(jcrypt.crypt(salt, mangle.capitalize(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.capitalize(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + n-capital
					if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.deleteFirstChar(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.ncapitalize(mangle.deleteFirstChar(word)));
						break;
					} 
					// deleteFirstChar + toggleEven
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.toggleEven(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.toggleEven(word)));
						break;
					}
					// deleteFirstChar + toggleOdd
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.toggleOdd(word))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.toggleOdd(word)));
						break;
					}
					// duplicate, reflectForward, reflectBackward
					if(word.length() < 5){
						if(jcrypt.crypt(salt, mangle.reflectForward(mangle.reverse(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectForward(mangle.reverse(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectBackward(mangle.reverse(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectBackward(mangle.reverse(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.duplicate(mangle.reverse(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.duplicate(mangle.reverse(word)));
							break;
						}
						if(jcrypt.crypt(salt, mangle.toggleEven(mangle.reflectForward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleEven(mangle.reflectForward(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.reflectForward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleOdd(mangle.reflectForward(word)));
							break;
						}
						if(jcrypt.crypt(salt, mangle.toggleEven(mangle.reflectBackward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleEven(mangle.reflectBackward(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.reflectBackward(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleOdd(mangle.reflectBackward(word)));
							break;
						}
						if(jcrypt.crypt(salt, mangle.toggleEven(mangle.duplicate(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleEven(mangle.duplicate(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.toggleOdd(mangle.duplicate(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.toggleOdd(mangle.duplicate(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.duplicate(mangle.deleteFirstChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.duplicate(mangle.deleteFirstChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.duplicate(mangle.deleteLastChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.duplicate(mangle.deleteLastChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectForward(mangle.deleteFirstChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectForward(mangle.deleteFirstChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectForward(mangle.deleteLastChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectForward(mangle.deleteLastChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectBackward(mangle.deleteFirstChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectBackward(mangle.deleteFirstChar(word)));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reflectBackward(mangle.deleteLastChar(word))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reflectBackward(mangle.deleteLastChar(word)));
							break;
						}
					}
				}
			}
		}
	}
	
	/*********************
	* METHOD: fifthRound
	* 	Purpose: runs through third round of mangling.
	* 	These tests have two mangles.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void fifthRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					// deleteFirstChar + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.deleteFirstChar(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.deleteFirstChar(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// deleteFirstChar + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.deleteLastChar(mangle.prependChar(word, (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteLastChar(mangle.prependChar(word, (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// reverse + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.reverse(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.reverse(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// prepend + reverse
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.reverse(mangle.prependChar(word, (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reverse(mangle.prependChar(word, (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// upper + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.uppercase(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.uppercase(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// capital + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.capitalize(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.capitalize(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// n-capital + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.ncapitalize(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.ncapitalize(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// toggleEven + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.toggleEven(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.toggleEven(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// toggleOdd + prepend
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.prependChar(mangle.toggleOdd(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.prependChar(mangle.toggleOdd(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
				}
			}
		}
	}
	
	
	/*********************
	* METHOD: sixthRound
	* 	Purpose: runs through third round of mangling.
	* 	These tests have two mangles.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void sixthRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					// deleteLastChar + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.deleteLastChar(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.deleteLastChar(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
						
					}
					// deleteFirstChar + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.deleteFirstChar(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.deleteFirstChar(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// capital + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.capitalize(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.capitalize(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// n-capital + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.ncapitalize(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.ncapitalize(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// reverse + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.reverse(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.reverse(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// append + reverse
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.reverse(mangle.appendChar(word, (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reverse(mangle.appendChar(word, (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// upper + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.uppercase(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.uppercase(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// append + upper
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.uppercase(mangle.appendChar(word, (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.uppercase(mangle.appendChar(word, (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// toggleEven + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.toggleEven(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.toggleEven(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
					// toggleOdd + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.appendChar(mangle.toggleOdd(word), (char)ch)).equals(actualPassword)){
							printCrackedPassword(key, mangle.appendChar(mangle.toggleOdd(word), (char)ch));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
				}
			}
		}
	}
	
	/*********************
	* METHOD: seventhRound
	* 	Purpose: runs through third round of mangling.
	* 	These tests have two mangles.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void seventhRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					
					// append + append
					// prepend + prepend
					// append + prepend
					/*for (int i = 32; i <=126; i++) {
						for (int j = 32; j <=126; j++) {
							if(jcrypt.crypt(salt, mangle.appendChar(mangle.appendChar(word, (char)j), (char)i)).equals(actualPassword)){
								printCrackedPassword(key, mangle.appendChar(mangle.appendChar(word, (char)j), (char)i));
								exit = true;
								break;
							}
							else if(jcrypt.crypt(salt, mangle.prependChar(mangle.prependChar(word, (char)j), (char)i)).equals(actualPassword)){
								printCrackedPassword(key, mangle.prependChar(mangle.prependChar(word, (char)j), (char)i));
								exit = true;
								break;
							}
							if(exit)
								break;
						}
					}*/
					// capitalize + reverse + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.reverse(mangle.capitalize(word)))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.reverse(mangle.capitalize(word))));
						break;
					} 
					// capitalize + reverse + capitalize
					if(jcrypt.crypt(salt, mangle.capitalize(mangle.reverse(mangle.capitalize(word)))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.reverse(mangle.capitalize(word))));
						break;
					} 
					// ncapitalize + reverse + deleteFirstChar
					if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.reverse(mangle.ncapitalize(word)))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.reverse(mangle.capitalize(word))));
						break;
					} 
					// ncapitalize + reverse + ncapitalize
					if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.reverse(mangle.ncapitalize(word)))).equals(actualPassword)){
						printCrackedPassword(key, mangle.deleteFirstChar(mangle.reverse(mangle.capitalize(word))));
						break;
					} 
					// deleteFirstChar + deleteLastChar combination
					if(word.length() >= 3){
						if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.ncapitalize(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.ncapitalize(word))));
							break;
						} 
						else if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.capitalize(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.capitalize(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.capitalize(mangle.deleteLastChar(mangle.deleteFirstChar(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.capitalize(mangle.deleteLastChar(mangle.deleteFirstChar(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.deleteLastChar(mangle.deleteFirstChar(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.ncapitalize(mangle.deleteLastChar(mangle.deleteFirstChar(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.uppercase(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.uppercase(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.reverse(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.reverse(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.reverse(mangle.deleteLastChar(mangle.deleteFirstChar(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.reverse(mangle.deleteLastChar(mangle.deleteFirstChar(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.toggleEven(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.toggleEven(word))));
							break;
						}
						else if(jcrypt.crypt(salt, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.toggleOdd(word)))).equals(actualPassword)){
							printCrackedPassword(key, mangle.deleteFirstChar(mangle.deleteLastChar(mangle.toggleOdd(word))));
							break;
						}
						if(exit)
							break;
					}
				}
			}
		}
	}
	
	
	/*********************
	* METHOD: eighthRound
	* 	Purpose: runs through eighth round of mangling.
	* 	These tests have three mangles.
	* <p> Parameters: None
	* <p> Return: None
	**********************/
	private static void eighthRound() {
		boolean exit = false;
		
		for(String word : possibleWords){
			// iterates through every user
			for(String key : users.keySet()){
				if(!users.get(key)[3].equals("Cracked")){
					String salt = users.get(key)[1];
					String actualPassword = users.get(key)[2];
					
					exit = false;
					// capitalize deleteLastChar + append
					for (int ch = 32; ch <=126; ch++) {
						if(jcrypt.crypt(salt, mangle.capitalize(mangle.appendChar(mangle.deleteLastChar(word), (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.capitalize(mangle.appendChar(mangle.deleteLastChar(word), (char)ch)));
							exit = true;
							break;
						}
						else if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.appendChar(mangle.deleteLastChar(word), (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.ncapitalize(mangle.appendChar(mangle.deleteLastChar(word), (char)ch)));
							exit = true;
							break;
						}
						else if(jcrypt.crypt(salt, mangle.ncapitalize(mangle.prependChar(mangle.deleteLastChar(word), (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.ncapitalize(mangle.prependChar(mangle.deleteLastChar(word), (char)ch)));
							exit = true;
							break;
						}
						else if(jcrypt.crypt(salt, mangle.capitalize(mangle.prependChar(mangle.deleteLastChar(word), (char)ch))).equals(actualPassword)){
							printCrackedPassword(key, mangle.capitalize(mangle.prependChar(mangle.deleteLastChar(word), (char)ch)));
							exit = true;
							break;
						}
						if(exit)
							break;
					}
				}
			}
		}
	}
	
	
	
	/*********************
	* METHOD: printCrackedPassword
	* 	Purpose: prints cracked passwords as they are
	*   cracked. 
	* <p> Parameters: int position (position of user in ArrayList)
	* <p> Return: None
	**********************/
	private static void printCrackedPassword(String key, String password) {
		totalCrackedPasswords++;
		System.out.println(password);
		String[] neededUserData = new String[4];
		neededUserData = users.get(key);
		neededUserData[3] = "Cracked";
		users.put(key, neededUserData);
		
		
		// prints timing output
		/*currentTime = System.currentTimeMillis();
		totalCrackedPasswords++;
		System.out.println("*  User: [" + key + "], " + users.get(key)[0]);
		System.out.println("     Password: " + password);
		if(totalCrackedPasswords == 1)
			System.out.println("     Time to Crack: " + (currentTime-startTime) + "ms\n");
		else 
			System.out.println("     Time to Crack: " + ((currentTime-startTime)-(oldTime-startTime)) + "ms\n");
		//users.remove(key);
		String[] neededUserData = new String[4];
		neededUserData = users.get(key);
		neededUserData[3] = "Cracked";
		users.put(key, neededUserData);
		oldTime = currentTime;*/
	}
}

