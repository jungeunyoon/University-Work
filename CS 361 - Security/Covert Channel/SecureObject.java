/*
 * Name: Jung Yoon
 * UTEID: JEY283
 * E-MAIL: jungyoon@utexas.edu
 * Assignment: Assignment 2
 * Purpose: Your assignment is to update your secure system from Assignment 1 
 * and add three new operations designed to introduce a covert channel into the 
 * system. You will implement the channel and use it to signal information from 
 * a high level user to a low level user. Finally, you will measure and report 
 * the bandwidth of the channel.
 */

public class SecureObject {

	// global variables
	int objectValue;
	String objectName;
	SecurityLevel objectLevel;
	
    /*********************
	* CONSTRUCTOR: SecureObject
	* @param val
	**********************/
	public SecureObject(String n, SecurityLevel l, int v)
	{
		objectName = n;
		objectLevel = l;
		objectValue = v;
	}
	
    /*********************
	* METHOD: setValue
	* 	Purpose: sets value
	* @param val
	* @return access
	**********************/
	public void setObjectValue(int val)
	{
		objectValue = val;
	}
	
    /*********************
	* METHOD: getObjectLevel
	* 	Purpose: access method for object level
	* @param none
	* @return object name
	**********************/
	public SecurityLevel getObjectLevel() {
		return objectLevel;
	}
	
    /*********************
	* METHOD: getObjectName
	* 	Purpose: access method for object name
	* @param none
	* @return object name
	**********************/
	public String getObjectName() {
		return objectName;
	}
	
    /*********************
	* METHOD: getValue
	* 	Purpose: access method for value
	* @param none
	* @return access
	**********************/
	public int getObjectValue() {
		return objectValue;
	} 
}
