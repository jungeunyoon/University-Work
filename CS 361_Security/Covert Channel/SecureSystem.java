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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SecureSystem {
	
	static ReferenceMonitor refMon = new ReferenceMonitor();
 
	/*public static void main (String [] args) throws IOException {
		
		// local variables
		SecureSystem secSys = new SecureSystem();
		String filename;
		BufferedReader buffReader;		
		String line = null;
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
		
		
		//if (args.length > 0) {
		//	filename = args[0];
		if (true) {	
			filename = "instructions.txt";	// DELETE THIS LINE
			System.out.print("Reading from file: " + filename + "\n\n"); 
			buffReader = new BufferedReader(new FileReader(filename));
			
			// access each line until end of file
			while ((line = buffReader.readLine()) != null) {	
				// creates new InstructionObject object depending on instruction
				instruction = new InstructionObject(line);
				
				// and gets the new InstructionObject
				currInstruction = instruction.getInstructionObject();

				// pass InstructionObject to ReferenceMonitor
				refMon.checkInstructionValidity(currInstruction);
				
				// print state after each line
				secSys.printState(currInstruction);
			}
		}
		else {
			System.out.println(" No input file specified! "); 
			return; 
		}
			buffReader.close();	
		
	}
	*/
	
    /*********************
	* METHOD: createSubject
	* 	Purpose: creates new subjects. Store these subjects into the state and 
	* 	informs the reference monitor about them.
	* @param Name of new subject, Subject's security level
	**********************/
	public void createSubject(String subName, SecurityLevel level) {
		// Each subject has a name and an integer variable TEMP recording the 
		// value it most recently read. TEMP is initially 0
		SecureSubject sub = new SecureSubject(subName, level, 0);
		refMon.subjectsMap.put(sub, level);
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
	private void printState(InstructionObject i) {
		Operations action = i.getOperation();
		switch(action){
			case BAD:
				System.out.println("Bad Instruction");
				break;
			case READ:
				System.out.println(i.getSubjectName() + " reads "
						+ i.getObjectName());
				break;
			case WRITE:
				System.out.println(i.getSubjectName() + " writes value "
						+ i.getValue() + " to " + i.getObjectName());
				break;
			case CREATE:
				break;
			case DESTROY:
				break;
			case RUN:
				break;
			default:
				break;
		}
		
		System.out.println("The current state is:");
	
		for(SecureObject objects : refMon.objectsMap.keySet() ){
			System.out.println("   " + objects.getObjectName() 
					+ " has value: " + objects.getObjectValue());
		}
		
		for(SecureSubject subjects : refMon.subjectsMap.keySet() ){
			System.out.println("   " + subjects.getSubjectName() 
					+ " has recently read: " + subjects.getSubjectTEMP());
		}
		System.out.println("");
		
	}
}