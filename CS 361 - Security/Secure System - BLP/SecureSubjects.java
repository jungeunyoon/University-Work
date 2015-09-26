/*
 * Name: Jung Yoon
 * UTEID: JEY283
 * Assignment: Assignment 1
 * Purpose: Your assignment is to implement in Java a simple "secure" system following 
 * the Bell and LaPadula (BLP) security rules - simple security, the *-property, and 
 * strong tranquility. 
 */

import java.util.Map;
import java.util.HashMap;

public class SecureSubjects {
	// global variables
	private String subjectName;
	private int TEMP; 
	
    /*********************
	* CONSTRUCTOR: SecureSubjects
	* 	Purpose: defines three kinds of constructors
	**********************/
	public SecureSubjects () {
		TEMP = 0;
	}
	
	public SecureSubjects (String subject) {
		subjectName = subject;
	}

    /*********************
	* METHOD: setTemp
	* 	Purpose: sets temp
	* @param val
	* @return access
	**********************/
	public void setTEMP (int temp) 
	{
		TEMP = temp;
	}
	
    /*********************
	* METHOD: overrides hashcode!!!!!
	**********************/
	public int hashCode() {
		int answer = 1;
		final int p = 31;
		return p * answer + ((subjectName == null) ? 0 : subjectName.hashCode());
	}
	
    /*********************
	* METHOD: getName
	* 	Purpose: access method for object name. Set to "0"
	* 	if no name.
	* @param none
	* @return object name
	**********************/
	public String getName()
	{
		return subjectName;
	}
	
    /*********************
	* METHOD: getTemp
	* 	Purpose: access method for value, if any. Set to "0" 
	* 	if no value. 
	* @param none
	* @return access
	**********************/
	public int getTEMP ()
	{
		return TEMP;
	}
	
}
