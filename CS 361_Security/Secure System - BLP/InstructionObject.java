/*
 * Name: Jung Yoon
 * UTEID: JEY283
 * Assignment: Assignment 1
 * Purpose: Your assignment is to implement in Java a simple "secure" system following 
 * the Bell and LaPadula (BLP) security rules - simple security, the *-property, and 
 * strong tranquility. 
 */

import java.io.IOException;

public class InstructionObject {
	// global variables
	static InstructionObject io = null; 
	private String operation;
	private String subjectName;
	private String objectName;
	private int value;
	
    /*********************
	* CONSTRUCTOR: InstructionObject
	* 	Purpose: check if valid format instruction, parse line
	* 	and create constant BadInstructionObject if invalid.
	* @param line of instruction
	**********************/
	public InstructionObject(String line) {
		// local variables:
		// For illegal instructions, generate a BadInstruction constant object.
		final InstructionObject BadInstruction = new InstructionObject("BAD", "0", "0", "0");
		
		// parse line of instruction and evaluate
		String [] parsedInstruction = line.split(" ");
		
		if (parsedInstruction.length > 0) {  // checks if not an empty line
			// CASE 1: read action
			if (parsedInstruction[0].equalsIgnoreCase("READ"))
			{
				// CASE 1A: has correct amount of arguments
				if (parsedInstruction.length == 3) {
					io = new InstructionObject(parsedInstruction[0], 
							parsedInstruction[1], parsedInstruction[2], "0");
				} 
				else 
					io = BadInstruction;
			}
			// CASE 2: write action
			else if (parsedInstruction[0].equalsIgnoreCase("WRITE"))
			{
				// CASE 2A: correct amount of arguments 
				if (parsedInstruction.length == 4) {
					// CASE 2B: value is an integer
					try{  
						if(Integer.valueOf(parsedInstruction[3]) instanceof Integer)
							io = new InstructionObject(parsedInstruction[0], 
									parsedInstruction[1], parsedInstruction[2], parsedInstruction[3]);
					}
					catch(NumberFormatException e){
						io = BadInstruction;
					}
				} 
				else 
					io = BadInstruction;
			}
			// CASE 3: action is not READ or WRITE
			else if ((!parsedInstruction[0].equalsIgnoreCase("READ")) 
						&& (!parsedInstruction[0].equalsIgnoreCase("WRITE"))) 
				io = BadInstruction;
		}
		// CASE 4: blank lines
		else 
			io = BadInstruction;
	}

    /*********************
	* CONSTRUCTOR: InstructionObject
	* 	Purpose: creates instruction object to be used by ReferenceMonitor
	* @param action, subject name, object name, value
	**********************/
	public InstructionObject(String action, String subject, String object, String val) {
		operation = action;
		subjectName = subject;
		objectName = object;
		if(operation.equalsIgnoreCase("write"))
			value = Integer.parseInt(val);
		else
			value = 0;
	}
	
    /*********************
	* METHOD: getInstructionObject
	* 	Purpose: access method for InstructionObject
	* @param none
	* @return InstructionObject object
	**********************/
	public InstructionObject getInstructionObject() {
		return io;
	}
	
    /*********************
	* METHOD: getOperation
	* 	Purpose: access method for action type
	* @param none
	* @return action type - BAD, READ, or WRITE
	**********************/
	public String getOperation() {
		return operation;
	}
	
    /*********************
	* METHOD: getSubjectName
	* 	Purpose: access method for subject name. Set to "0"
	* 	if no name. 
	* @param none
	* @return subject name
	**********************/
	public String getSubjectName() {
		return subjectName;
	}
	
    /*********************
	* METHOD: getObjectName
	* 	Purpose: access method for object name. Set to "0"
	* 	if no name.
	* @param none
	* @return object name
	**********************/
	public String getObjectName() {
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

