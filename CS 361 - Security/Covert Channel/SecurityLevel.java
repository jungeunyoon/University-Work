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

public class SecurityLevel {
	// global variables
	public static final SecurityLevel HIGH = new SecurityLevel(1);
	public static final SecurityLevel LOW = new SecurityLevel(0);
	int level;
	
    /*********************
	* CONSTRUCTOR: SecurityLevel(int level)
	* 	Purpose: access method for level
	* @param int level
	**********************/
	public SecurityLevel(int level) {
		this.level = level;
	}
	
    /*********************
	* METHOD: getSecurityLevel
	* 	Purpose: access method for level
	* @param none
	* @return object name
	**********************/
	public int getSecurityLevel() {
		return level;
	}
}
