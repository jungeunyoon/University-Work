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
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class CovertChannel {
	// global variables
	static ReferenceMonitor refMon = new ReferenceMonitor();
	SecureSystem ss = new SecureSystem();
	static SecureSystem secSys = new SecureSystem();
	
	public static void main (String [] args) throws IOException {
		
		// local variables
		String filename;
		boolean verbose = false;
		ByteArrayInputStream byteInputStream = null;
		Path path = null; Paths.get("path/to/file");
		byte[] data = null; 
		FileWriter log = null;
		FileWriter outie = null;
		PrintWriter outputStream = null; 
		PrintWriter printVerbose = null;
		int nextByte = 0;
		long start = System.currentTimeMillis();
		long stop = 0;
		long len = 0;
		long bandwidth = 0;
	
		// 1. Define low and high levels
		SecurityLevel low  = SecurityLevel.LOW;
		SecurityLevel high = SecurityLevel.HIGH;
	 
		// 2a. Create two new subjects: Lyle of security level LOW, and Hal of security level HIGH. 	
		SecureSubject sub = new SecureSubject("Lyle", low, 0);
		refMon.subjectsMap.put(sub, low);
		SecureSubject sub1 = new SecureSubject("Hal", high, 0);
		refMon.subjectsMap.put(sub1, high);
		
		// sets up byte concatenation 
		sub.clearBitCount();
		sub.clearFormingByte();
		
		// 3. Read successive instructions from the input file and execute them
		if (args.length > 0) {
			// initialize output streams
			if(args[0].equalsIgnoreCase("v")) {
				filename = args[1];
				verbose = true;
			}
			else {
				filename = args[0];
			}
			
			// sets up output file
			File out = new File(filename + ".out");
			if(!out.exists())
				out.createNewFile();
			outie = new FileWriter(out);
			outputStream = new PrintWriter(outie);

			// sets up log file
			File f = new File("log");
			if(!f.exists())
				f.createNewFile();
			log = new FileWriter(f);
			printVerbose = new PrintWriter(log);
			
			// turns file into a array of bytes
			path = Paths.get(filename);
			data = Files.readAllBytes(path);
			byteInputStream = new ByteArrayInputStream(data);
			
			// run until end of file
			while((nextByte = byteInputStream.read()) != -1){
				int mask = 0x80;
				int bit = 0; 
				// access a byte at a time
				for (int j = 0; j < 8; j++)
				{
					if(j==0)
						mask = 0x80;
					bit = (nextByte & mask);
					
					   switch(bit){
						// send a bit 0
						case 0: {
							// print to log for verbose mode
							if(verbose){
								printVerbose.println("RUN HAL");
								printVerbose.println("CREATE HAL OBJ");
								printVerbose.println("CREATE LYLE OBJ");
								printVerbose.println("WRITE LYLE OBJ 1");
								printVerbose.println("READ LYLE OBJ");
								printVerbose.println("DESTROY LYLE OBJ");
								printVerbose.println("RUN LYLE");
							}
							// instructions to execute to transmit 0
							refMon.executeCreate("Hal", "obj");
							refMon.executeCreate("Lyle", "obj");
							refMon.executeWrite("Lyle", "obj", 1);
							refMon.executeRead("Lyle", "obj");
							refMon.executeDestroy("Lyle", "obj");
							refMon.executeRun("Lyle");
							break;
						}
						// send a bit 1
						default:{
							// print to log for verbose mode
							if(verbose){
								printVerbose.println("CREATE LYLE OBJ");
								printVerbose.println("WRITE LYLE OBJ 1");
								printVerbose.println("READ LYLE OBJ");
								printVerbose.println("DESTROY LYLE OBJ");
								printVerbose.println("RUN LYLE");
							}
							// instructions to execute to transmit 1
							refMon.executeCreate("Lyle", "obj");
							refMon.executeWrite("Lyle", "obj", 1);
							refMon.executeRead("Lyle", "obj");
							refMon.executeDestroy("Lyle", "obj");
							refMon.executeRun("Lyle");
							break;
						}
					}
					mask = (mask >>> 1);
				}
				// outputs byte to .out file after being processed by RUN
				outputStream.print((char) sub.getByteValue());
			}

	}
		else {
			System.out.println(" No input file specified! "); 
			return; 
		}
		
		stop = System.currentTimeMillis();
		len = new File(filename).length();
		bandwidth = len/(stop-start);
		System.out.println("***************************************************");
		System.out.println("Reading from file: " + filename);
		System.out.println("     Size: " + len + " bytes");
		System.out.println("     Time: " + (stop - start) + " ms");
		System.out.println("Bandwidth: " + (bandwidth*8) + " ms");
		System.out.println("***************************************************");
		outputStream.flush();
		printVerbose.flush();
		outputStream.close();
		log.close();
		byteInputStream.close();
		printVerbose.close();

	}
	
}
