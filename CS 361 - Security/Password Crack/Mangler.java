/*
 * Name: Jung Yoon
 * UTEID: jey283
 * CS Account: jungyoon
 * E-MAIL: jungyoon@utexas.edu
 * Assignment: Assignment 5
 * Purpose: Password Crack - The goal of this assignment is to implement a portion of 
 * Crack's functionality and attempt to guess one or more passwords. 
 */

/******************************************************
*******************************************************
* CLASS: Mangler
* 	Purpose: Mangles given String into any
* 	of the possibilities, given by the functions
*******************************************************
*******************************************************/

public class Mangler {

	/*********************
	* METHOD: toggleEndsLower
	**********************/
	public String toggleEndsLower(String word) {
		if(word.length() >=3){
			return Character.toLowerCase(word.charAt(0)) 
					+ word.substring(1, word.length()-1).toUpperCase() 
					+ Character.toLowerCase(word.charAt(word.length()-1));
			}
		else
			return word;
	}

	/*********************
	* METHOD: toggleEndsUpper
	**********************/
	public String toggleEndsUpper(String word) {
		if(word.length() >=3){
			return Character.toUpperCase(word.charAt(0)) 
					+ word.substring(1, word.length()-1).toLowerCase()
					+ Character.toUpperCase(word.charAt(word.length()-1));
		}
		else
			return word;
	}
	
	/*********************
	* METHOD: toggleOdd
	**********************/
	public String toggleOdd(String word) {
		StringBuilder t = new StringBuilder();
		for(int i = 0; i < word.length(); i++){
			if((i & 1) == 1)
				t.append(Character.toUpperCase(word.charAt(i)));
			else
				t.append(Character.toLowerCase(word.charAt(i)));
		}
		return t.toString();
	}

	/*********************
	* METHOD: toggleEven
	**********************/
	public String toggleEven(String word) {
		StringBuilder t = new StringBuilder();
		for(int i = 0; i < word.length(); i++){
			if((i & 1) == 0)
				t.append(Character.toUpperCase(word.charAt(i)));
			else
				t.append(Character.toLowerCase(word.charAt(i)));
		}
		return t.toString();
	}

	/*********************
	* METHOD: ncapitalize
	**********************/
	public String ncapitalize(String word) {
		return Character.toLowerCase(word.charAt(0)) 
				+ word.substring(1).toUpperCase();	
	}

	/*********************
	* METHOD: capitalize
	**********************/
	public String capitalize(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	/*********************
	* METHOD: lower case
	**********************/
	public String lowercase(String word) {
		return word.toLowerCase();
	}

	/*********************
	* METHOD: upper case
	**********************/
	public String uppercase(String word) {
		return word.toUpperCase();
	}
	
	/*********************
	* METHOD: reflectForward
	**********************/
	public String reflectForward(String word) {
		return new StringBuilder(word).reverse().toString() + word;
	}
	
	/*********************
	* METHOD: reflectBackward
	**********************/
	public String reflectBackward(String word) {
		return word + new StringBuilder(word).reverse().toString();
	}

	/*********************
	* METHOD: duplicate
	**********************/
	public String duplicate(String word) {
		return new StringBuilder(word).append(word).toString();
	}
	
	/*********************
	* METHOD: reverse
	**********************/
	public String reverse(String word) {
		return new StringBuilder(word).reverse().toString();
	}

	/*********************
	* METHOD: deleteLastChar
	**********************/
	public String deleteLastChar(String word) {
		return word.substring(0, word.length()-1);
	}

	/*********************
	* METHOD: deleteFirstchar
	**********************/
	public String deleteFirstChar(String word) {
		return word.substring(1);
	}

	/*********************
	* METHOD: appendChar
	**********************/
	public String appendChar(String word, char c) {
		return new StringBuilder(word).append(c).toString();
	}

    /*********************
	* METHOD: prependChar
	* </br> O(N^2) - don't use until the very end
	**********************/
	public String prependChar(String word, char c) {
		return new StringBuilder(word).insert(0, c).toString();
	}
}
