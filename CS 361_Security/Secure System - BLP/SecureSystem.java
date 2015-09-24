/*
 * Name: Jung Yoon
 * UTEID: JEY283
 * Assignment: Assignment 1
 * Purpose: Your assignment is to implement in Java a simple "secure" system following 
 * the Bell and LaPadula (BLP) security rules - simple security, the *-property, and 
 * strong tranquility. 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SecureSystem {
	
	// global variables
	static ReferenceMonitor refMon = new ReferenceMonitor();

    /*********************
	* METHOD: main
	* @param file from terminal
	**********************/
	public static void main (String [] args) throws IOException {
		
		// local variables
		SecureSystem secSys = new SecureSystem();
		String filename;
		BufferedReader buffReader;		
		String line;
		InstructionObject instruction;
		InstructionObject currInstruction;
		
		// 1. Define low and high levels
		SecurityLevel low  = SecurityLevel.LOW;
		SecurityLevel high = SecurityLevel.HIGH;
	
		// 2a. Create two new subjects: Lyle of security level LOW, and Hal of security level HIGH. 
		secSys.createSubject("Lyle", low);
		secSys.createSubject("Hal", high);
		
		// 2b. Create two new objects: LObj of security level LOW, and HObj of security level HIGH. 
		//add two objects, one high and one low.
		secSys.getReferenceMonitor().createNewObject("LObj", low);
		secSys.getReferenceMonitor().createNewObject("HObj", high);
		
		// 3. Read successive instructions from the input file and execute them
		
		
		if (args.length > 0) {
			filename = args[0];
			System.out.print("Reading from file: " + filename + "\n\n"); 
			buffReader = new BufferedReader(new FileReader(filename));
			
			// access each line 
			while ((line = buffReader.readLine()) != null) {	
				
				instruction = new InstructionObject(line);
				currInstruction = instruction.getInstructionObject();

				// pass InstructionObject to ReferenceMonitor
				refMon.catchBadInstruction(currInstruction);
				
				// print state after each line
				secSys.printState();
			}
		}
		else {
			System.out.println(" No input file specified! "); 
			return; 
		}
			buffReader.close();
	}
	
    /*********************
	* METHOD: createSubject
	* 	Purpose: creates new subjects. Store these subjects into the state and 
	* 	informs the reference monitor about them.
	* @param Name of new subject, Subject's security level
	**********************/
	private void createSubject(String subName, SecurityLevel level) {
		SecureSubjects sub = new SecureSubjects(subName);
		refMon.createNewSubject(sub, level);
	}
	
    /*********************
	* METHOD: getReferenceMonitor
	* 	Purpose: returns the current reference monitor.
	* @param None
	**********************/
	private ReferenceMonitor getReferenceMonitor(){
		return refMon;
	}
	
    /*********************
	* METHOD: printState
	* 	Purpose: prints out current values from the state: the value of objects 
	* 	and the TEMP value for subjects. For debugging purposes.
	* @param none
	**********************/
	private void printState() {
		refMon.printState();
	}
	
}