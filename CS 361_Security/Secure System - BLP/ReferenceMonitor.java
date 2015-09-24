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

public class ReferenceMonitor {
	// global variables
	ObjectManager objManager = new ObjectManager();
	private Map<SecureObjects, SecurityLevel> objectsMap= new HashMap<SecureObjects, SecurityLevel>();
	private Map<SecureSubjects, SecurityLevel> subjectsMap = new HashMap<SecureSubjects, SecurityLevel>();
	
    /*********************
	* METHOD: createNewSubject
	* 	Purpose: creates new subjects by storing it into the hashmap.
	* @param Name of new Subject, Subject's security level
	**********************/
	public void createNewSubject(SecureSubjects subject, SecurityLevel level) {
		if (!subjectsMap.containsKey(subject))
			subjectsMap.put(subject, level);	
	}
	
    /*********************
	* METHOD: createNewObject
	* 	Purpose: creates new objects by storing it into the hashmap.
	* @param Name of new object, Objects's security level
	**********************/
	public void createNewObject(String objectName, SecurityLevel level) {
		SecureObjects obj = new SecureObjects(objectName);
		if (!objectsMap.containsKey(obj)) 
			objectsMap.put(obj, level);
	}
	
    /*********************
	* METHOD: catchBadInstructions
	* 	Purpose: validate the action type. Checks if InstructionObject 
	* 	is a valid instruction (not a BadInstructionObject)
	* @param InstructionObject
	**********************/
	public void catchBadInstruction(InstructionObject i) {
		// CASE 1: "BAD" - do nothing
		if (i.getOperation().equalsIgnoreCase("bad")) 
			System.out.println("Bad Instruction.");
		// CASE 2: "READ" - evaluate
		else if (i.getOperation().equalsIgnoreCase("read")) 
				executeRead(i.getSubjectName(), i.getObjectName());
		// CASE 3: "WRITE" - evaluate
		else if (i.getOperation().equalsIgnoreCase("write")) 
				executeWrite(i.getSubjectName(), i.getObjectName(), i.getValue());
	}
	
    /*********************
	* METHOD: executeRead
	* 	Purpose: checks simple security property and if valid, 
	* 	sends to ObjectManager
	* @param String subjectName, String objectName
	**********************/
	public void executeRead(String subjectName, String objectName) {
		// local variables
		int TEMP = 0;	// stores value into TEMP variable
		boolean valid = false;
		
		// SecurityLevel subjectLevel = subjectsMap.get(subjectName);
		// SecurityLevel objectLevel = objectsMap.get(objectName);
		
		// checks to see if subject and object exist
		for (SecureSubjects subject : subjectsMap.keySet()) {
			if(subject.getName().equalsIgnoreCase(subjectName)) {
				for (SecureObjects object : objectsMap.keySet()) {
					if (object.getName().equalsIgnoreCase(objectName)) {
						// meets simple security policy
						if (subjectsMap.get(subject).compareTo(objectsMap.get(object)) >= 0) 
							TEMP = object.getValue();
						System.out.println(subjectName + " reads " + objectName);
						objManager.OBJMan_Read(subject, TEMP);
						return;
					}
				}
			}
		} return;
	}
	
    /*********************
	* METHOD: executeWrite
	* 	Purpose: checks *- property and if valid, 
	* 	sends to ObjectManager
	* @param String subjectName, String objectName, int value
	**********************/
	public void executeWrite(String subjectName, String objectName, int value) {
		
		// checks to see if subject and object exist
		for (SecureSubjects subject : subjectsMap.keySet()) {
			if (subject.getName().equalsIgnoreCase(subjectName)) {
				for (SecureObjects object : objectsMap.keySet()) {
					if (object.getName().equalsIgnoreCase(objectName)) {
						if (subjectsMap.get(subject).compareTo(objectsMap.get(object)) <= 0)			
							objManager.OBJMan_Write(object, value);
						System.out.println(subjectName + " writes value " + value + " to " + objectName);
						return;
						}
					}
				} return;
			} return;
	}
	
    /*********************
	* METHOD: printState
	* 	Purpose: prints state. 
	**********************/
	public void printState() {
		System.out.println("The current state is:");
		// print objects
		for (SecureObjects object : objectsMap.keySet())
			System.out.println("   " + object.getName() + " has value: " + object.getValue());
		// print subjects
		for (SecureSubjects subject : subjectsMap.keySet())
			System.out.println("   " + subject.getName() + " has recently read: " + subject.getTEMP());
		System.out.println("");
	}
	
    /*********************
	* NESTED CLASS: Object Manager
	**********************/
	private class ObjectManager {
		public void OBJMan_Read (SecureSubjects subject, int temp) {
			subject.setTEMP(temp);
		}
		public void OBJMan_Write(SecureObjects object, int value) {
			object.setValue(value);
		}
	}
}
