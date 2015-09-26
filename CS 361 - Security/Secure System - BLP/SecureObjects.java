/*
 * Name: Jung Yoon
 * UTEID: JEY283
 * Assignment: Assignment 1
 * Purpose: Your assignment is to implement in Java a simple "secure" system following 
 * the Bell and LaPadula (BLP) security rules - simple security, the *-property, and 
 * strong tranquility. 
 */

public class SecureObjects {
	// global variables
	private int value;
	private String objectName;
	
    /*********************
	* CONSTRUCTOR: SecureObjects
	* 	Purpose: defines three kinds of constructors
	**********************/
	public SecureObjects() {
		value = 0;
	}

	public SecureObjects(String name) {
		objectName = name;		
	}
	
	public SecureObjects(String name, int val) {
		value = val;
		objectName = name;
	}
	
    /*********************
	* METHOD: setValue
	* 	Purpose: sets value
	* @param val
	* @return access
	**********************/
	public void setValue(int val)
	{
		value = val;
	}

    /*********************
	* METHOD: overrides equals for SecureObjects equals since they're objects
	**********************/
	public boolean equals (SecureObjects object)
	{
		if (object == null) return false;
		if (!(object instanceof SecureObjects)) return false;
		if (this == object) return true;
		if (object.getName().equals(this.getName()))
			return true;
		else
			return false;
	}

    /*********************
	* METHOD: getObjectName
	* 	Purpose: access method for object name. Set to "0"
	* 	if no name.
	* @param none
	* @return object name
	**********************/
	public String getName() {
		return objectName;
	}
	
    /*********************
	* METHOD: getValue
	* 	Purpose: access method for value, if any. Set to "0" 
	* 	if no value. 
	* @param none
	* @return access
	**********************/
	public int getValue() {
		return value;
	}
}
