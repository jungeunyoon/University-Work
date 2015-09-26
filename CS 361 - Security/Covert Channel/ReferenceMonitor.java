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

import java.util.Map;
import java.util.HashMap;

public class ReferenceMonitor {
	// global variables
	ObjectManager objManager = new ObjectManager();
	
	// holds subjects and objects
	Map<SecureSubject, SecurityLevel> subjectsMap = new HashMap<SecureSubject, SecurityLevel>();
	Map<SecureObject, SecurityLevel> objectsMap = new HashMap<SecureObject, SecurityLevel>();
	
    /*********************
	* METHOD: checkNewObject
	* 	Purpose: creates new object
	* @param String objName, SecurityLevel objLevel
	**********************/
	public void createNewObject(String objName, SecurityLevel objLevel) {
		objManager.createNewObject(objName, objLevel, 0);
	}
	
    /*********************
	* METHOD: checkInstructionValidity
	* 	Purpose: validate the action type. Checks if InstructionObject 
	* 	is a valid instruction (not a BadInstructionObject)
	* @param InstructionObject
	**********************/
	public void checkInstructionValidity(InstructionObject i) {			 
		Operations action = i.getOperation();
		switch(action){
			case READ:
				executeRead(i.getSubjectName(), i.getObjectName());
				break;
			case WRITE:
				executeWrite(i.getSubjectName(), i.getObjectName(), i.getValue());
				break;
			case CREATE:
				executeCreate(i.getSubjectName(), i.getObjectName());
				break;
			case DESTROY:
				executeDestroy(i.getSubjectName(), i.getObjectName());
				break;
			case RUN:
				executeRun(i.getSubjectName());
				break;
			default:
				break;
		}
	}

	/*********************
	* METHOD: executeRead
	* 	Purpose: checks simple security property and if valid, 
	* 	sends to ObjectManager
	* 	--> if object exists and Ls >= Lo
	* @param String subjectName, String objectName
	**********************/
	public void executeRead(String subjectName, String objectName) {		
		// local variables
		SecureSubject sub = null;
		SecureObject obj = null;
		int subLevel = 0;
		int objLevel = 0;
		boolean subjectExists = false;
		boolean objectExists = false;
		
		// check if subject exists
		for(SecureSubject subKey : subjectsMap.keySet()){
			if(subKey.getSubjectName().equals(subjectName)){
				sub = subKey;
				subLevel = subKey.getSubjectLevel().getSecurityLevel();
				subjectExists = true;
				break;
			}
		}	
		// check if object exists
		for(SecureObject objKey : objectsMap.keySet()){
			if(objKey.getObjectName().equals(objectName)){
				obj = objKey;
				objLevel = objKey.getObjectLevel().getSecurityLevel();
				objectExists = true;
				break;
			}
		}
		// validate simple security policy and pass to object manager
		if((subjectExists && objectExists) && (subLevel >= objLevel)){
			objManager.executeRead(sub, obj);
		}
		// if simply security policy not met, set TEMP to 0 
		else {
			sub.setSubjectTEMP(0);
		}
	}
	
    /*********************
	* METHOD: executeWrite
	* 	Purpose: checks *- property and if valid, 
	* 	sends to ObjectManager
	* @param String subjectName, String objectName, int value
	**********************/
	public void executeWrite(String subjectName, String objectName, int value) {
		// local variables
		SecureSubject sub = null;
		SecureObject obj = null;
		int subLevel = 0;
		int objLevel = 0;
		boolean subjectExists = false;
		boolean objectExists = false;
		
		// check if subject exists
		for(SecureSubject subKey : subjectsMap.keySet()){
			if(subKey.getSubjectName().equals(subjectName)){
				sub = subKey;
				subLevel = subKey.getSubjectLevel().getSecurityLevel();
				subjectExists = true;
				break;
			}
		}	
		// check if object exists
		for(SecureObject objKey : objectsMap.keySet()){
			if(objKey.getObjectName().equals(objectName)){
				obj = objKey;
				objLevel = objKey.getObjectLevel().getSecurityLevel();
				objectExists = true;
				break;
			}
		}
		// validate *-Property and pass to object manager
		if((subjectExists && objectExists) && (subLevel <= objLevel)){
			objManager.executeWrite(sub, obj, value);
		}
	}
	
	/*********************
	* METHOD: executeCreate
	* 	Purpose: a new object is added to the state with SecurityLevel equal 
	* 	to the level of the creating subject. It is given an initial value of 0.
	*	If there already exists an object with that name at any level, the operation 
	*	is a no-op.
	* @param String subjectName, String objectName
	**********************/
	public void executeCreate(String subjectName, String objectName) {
		// local variables
		SecureSubject sub = null;
		SecureObject obj = null;
		int subLevel = 0;
		int objLevel = 0;
		boolean subjectExists = false;
		boolean objectExists = false;
		
		// check if subject exists
		for(SecureSubject subKey : subjectsMap.keySet()){
			if(subKey.getSubjectName().equals(subjectName)){
				sub = subKey;
				subLevel = subKey.getSubjectLevel().getSecurityLevel();
				subjectExists = true;
				break;
			}
		}	
		// check if object exists:
		// do nothing if object exists with that name at any level
		for(SecureObject objKey : objectsMap.keySet()){
			if(objKey.getObjectName().equals(objectName)){
				objectExists = true;
				break;
			}
		}
		// creates new object
		if(subjectExists && (!objectExists)){
			objManager.createNewObject(objectName, sub.getSubjectLevel(), 0);
		}
	}
	
	/*********************
	* METHOD: executeDestroy
	* 	Purpose: will eliminate the designated object from the state, assuming 
	* 	that the object exists and the subject has WRITE access to the object 
	* 	according to the *-property of BLP. Otherwise, the operation is a no-op.
	* @param String subjectName, String objectName
	**********************/
	public void executeDestroy(String subjectName, String objectName) {
		// local variables
		SecureObject obj = null;

		for(SecureObject objKey : objectsMap.keySet()){
			if(objKey.getObjectName().equals(objectName)){
				obj = objKey;
				break;
			}
		}
		// validate BLP and pass to object manager
			objManager.executeDestroy(obj);

	}
	
	/*********************
	* METHOD: executeRun
	*   Purpose: allows the named subject to execute some arbitrary private code. 
 	* 	It has no access to any of the state objects, and so should be irrelevant to 
	* 	the security of the system. It models whatever processing the subject may do 
	* 	with the value it has just read. 
	* @param String subjectName
	**********************/
    public void executeRun(String subjectName) {
		SecureSubject sub = null;
		SecureObject obj = null;
		int subLevel = 0;
		boolean subjectExists = false;
		int mask = 0;
		
		// in the case of subject Lyle executing the run operation
		if(subjectName.equalsIgnoreCase("Lyle")){
			// check if subject exists
			for(SecureSubject subKey : subjectsMap.keySet()){
				if(subKey.getSubjectName().equals(subjectName)){
					sub = subKey;
					subLevel = subKey.getSubjectLevel().getSecurityLevel();
					subjectExists = true;
					break;
				}
			}
			// concatenate 8 bits together
			if(sub.getBitCount() < 8){
				//System.out.print(sub.getSubjectTEMP());
				mask = (7-sub.getBitCount());
				sub.setFormingByte((sub.getSubjectTEMP() << mask) | sub.getFormingByte());
				sub.incrementBitCount();
			}
			// send completed byte back to subject's state
			if(sub.getBitCount() == 8){
				sub.setByteValue(sub.getFormingByte());
				// clear all counts and bytes
				sub.clearBitCount();
				sub.clearFormingByte();
			}
			
		}
	}
	
   /*********************
    *********************
    * NESTED CLASS: Object Manager
	*********************
	*********************/
	private class ObjectManager {
		/*********************
		* METHOD: createNewObject
		* 	Purpose: creates new object
		* @param String objName, SecurityLevel objLevel, int i
		**********************/
		public void createNewObject(String objName, SecurityLevel objLevel,
				int i) {
			SecureObject obj = new SecureObject(objName, objLevel, 0);
			objectsMap.put(obj, objLevel);
		}
		
		/*********************
		* METHOD: executeRead
		* 	Purpose: executes read operation
		* @param SecureSubject subject, SecureObject object
		**********************/
		public void executeRead(SecureSubject subject, SecureObject object) {
			subject.setSubjectTEMP(object.getObjectValue());
		}
		
		/*********************
		* METHOD: executeDestroy
		* 	Purpose: destroys object
		* @param SecureObject obj
		**********************/
		public void executeDestroy(SecureObject obj) {
			objectsMap.remove(obj);
		}
		
	    /*********************
		* METHOD: executeWrite
		* 	Purpose: executes write operation
		* @param SecureSubject subject, SecureObject object, int value
		**********************/
		public void executeWrite(SecureSubject subject, SecureObject object, int value) {
			object.setObjectValue(value);
		}
	}
}