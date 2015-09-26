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

public class SecureSubject {

	// global variables
	int subjectTEMP;
	String subjectName;
	SecurityLevel subjectLevel;
	
	int byteValue;
	int covertBitCount;
	int formingByte;
	
    /*********************
	* CONSTRUCTOR: SecureSubject
	* @param val
	**********************/
	public SecureSubject(String n, SecurityLevel l, int temp)
	{
		subjectName = n;
		subjectLevel = l;
		subjectTEMP = temp;
	}
	
	
    /*********************
	* METHOD: setValue
	* 	Purpose: sets count
	* @param val
	* @return access
	**********************/
	public void incrementBitCount()
	{
		covertBitCount = covertBitCount + 1;
	}
	
    /*********************
	* METHOD: setValue
	* 	Purpose: sets count
	* @param val
	* @return access
	**********************/
	public void clearBitCount()
	{
		covertBitCount = 0;
	}
	
    /*********************
	* METHOD: setValue
	* 	Purpose: sets count
	* @param val
	* @return access
	**********************/
	public int getBitCount()
	{
		return covertBitCount;
	}
	
    /*********************
	* METHOD: getFormingByte
	* 	Purpose: sets Bytes
	* @param 
	* @return 
	**********************/
	public int getFormingByte()
	{
		return formingByte;
	}
	
    /*********************
	* METHOD: setFormingByte
	* 	Purpose: sets Bytes
	* @param 
	* @return 
	**********************/
	public void clearFormingByte()
	{
		formingByte = 0;
	}
	

    /*********************
	* METHOD: setFormingByte
	* 	Purpose: sets Bytes
	* @param 
	* @return 
	**********************/
	public void setFormingByte(int bt)
	{
		formingByte = bt;
	}
	
    /*********************
	* METHOD: setByteValue
	* 	Purpose: sets 8-bit/byte value to be written into .out file
	* @param byte value passed from executeRun(subject) method
	* @return 
	**********************/
	public void setByteValue(int bv) {
		byteValue = bv;
		
	} 
	
    /*********************
	* METHOD: getFormingByte
	* @param 
	* @return 
	**********************/
	public int getByteValue() {
		return byteValue;
		
	} 
	
    /*********************
	* METHOD: setValue
	* 	Purpose: sets TEMP, which holds the subject's most 
	* 		recently read value; 0 by default.
	* @param val
	* @return access
	**********************/
	public void setSubjectTEMP(int t)
	{
		subjectTEMP = t;
	}
	
    /*********************
	* METHOD: getsubjectLevel
	* 	Purpose: access method for subject level
	* @param none
	* @return subject name
	**********************/
	public SecurityLevel getSubjectLevel() {
		return subjectLevel;
	}
	
    /*********************
	* METHOD: getsubjectName
	* 	Purpose: access method for subject name
	* @param none
	* @return subject name
	**********************/
	public String getSubjectName() {
		return subjectName;
	}
	
    /*********************
	* METHOD: getValue
	* 	Purpose: access method for value
	* @param none
	* @return access
	**********************/
	public int getSubjectTEMP() {
		return subjectTEMP;
	}
}
