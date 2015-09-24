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

import java.io.IOException;

public class InstructionObject {
	// global variables
	static InstructionObject io = null; 
	private Operations operationObj;
	private String operation;
	private String subjectName;
	private String objectName;
	private int value;
	
    /*********************
	* CONSTRUCTOR: InstructionObject
	* 	Purpose: check if valid format instruction, parse line
	* 	and create constant BadInstructionObject if invalid,
	* 	otherwise, pass to constructor:
	* 	InstructionObject(String operation, String subject, String object, String val).
	* @param line of instruction
	**********************/
	public InstructionObject(String line) {
		
		// local variables:
		// For illegal instructions, generate a BadInstruction constant object.
		final InstructionObject BadInstruction = new InstructionObject("BAD", "0", "0", "0");
		String[] parsedInstruction;
		String delims;
		Operations action;
		
		// parse line of instruction with respect to space(s) and evaluate
		delims = "[ ]+";
		parsedInstruction = line.split(delims);
		
		// evaluates instruction, given that it's one of the 5 valid ones
		try{
			action = Operations.valueOf(parsedInstruction[0].toUpperCase());
			
			// evaluate instruction if not empty
			if (parsedInstruction.length > 0) { 
				switch(action) {
					// ***** READ *****
					case READ: 
						// must specify subjectName and objectName
						if (parsedInstruction.length != 3) 
							io = BadInstruction;
						else {
							io = new InstructionObject(parsedInstruction[0].toUpperCase(), 
									parsedInstruction[1], parsedInstruction[2], "0");
						}
						break;
					// ***** WRITE *****
					case WRITE:
						// must specify subjectName, objectName, and value
						if (parsedInstruction.length != 4) 
							io = BadInstruction;
						// value must be integer
						else {
							try{  
								if(Integer.valueOf(parsedInstruction[3]) instanceof Integer)
									io = new InstructionObject(parsedInstruction[0].toUpperCase(), 
											parsedInstruction[1], parsedInstruction[2], parsedInstruction[3]);
							}
							catch(NumberFormatException e){
								io = BadInstruction;
							}
						}
						break;
					// ***** CREATE *****
					case CREATE:
						// must specify subjectName and objectName
						if (parsedInstruction.length != 3) 
							io = BadInstruction;
						else 
							io = new InstructionObject(parsedInstruction[0].toUpperCase(), 
									parsedInstruction[1], parsedInstruction[2], "0");
						break;	
					// ***** DESTROY *****
					case DESTROY:
						// must specify subjectName and objectName
						if (parsedInstruction.length != 3) 
							io = BadInstruction;
						else 
							io = new InstructionObject(parsedInstruction[0].toUpperCase(), 
									parsedInstruction[1], parsedInstruction[2], "0");
						break;	
					// ***** RUN *****
					case RUN:
						// must specify subjectName
						if (parsedInstruction.length != 2) 
							io = BadInstruction;
						else 
							io = new InstructionObject(parsedInstruction[0].toUpperCase(), 
									parsedInstruction[1], parsedInstruction[2], "0");
						break;
					// ***** MISC. *****
					// not necessary but best to be safe
					default: 
						io = BadInstruction;
						break;
				}
			}	
			else // ***** EMPTY LINE *****
				io = BadInstruction;
		} // ***** NOT ONE OF FIVE VALID OPERATIONS *****
		catch(IllegalArgumentException e){
			io = BadInstruction;
		}
	
	}
	
    /*********************
	* CONSTRUCTOR: InstructionObject
	* 	Purpose: creates instruction object to be used by ReferenceMonitor
	* @param action, subject name, object name, value
	**********************/
	public InstructionObject(String action, String subject, String object, String val) {
		operation = action;
		operationObj = Operations.valueOf(operation.toUpperCase());
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
	* @return action type - BAD, READ, WRITE, DESTROY, CREATE, OR RUN
	**********************/
	public Operations getOperation() {
		return operationObj;
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