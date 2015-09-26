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
	
/*****************************************
 *****************************************
 * CLASS: Operations
 * Keeps track of operations as predefined constants.
 *****************************************
 *****************************************/

public enum Operations {
	// 1. illegal instructions
    BAD, 
    // 2. subject reads the current value of the object
    READ, 
    // 3. change object's value
    WRITE, 
    // 4. a new object is added to the state with SecurityLevel equal to the level of the creating subject
    CREATE, 
    // 5. eliminate the designated object from the state
    DESTROY, 
    // 6. allows the named subject to execute some arbitrary private code
    RUN 
  }