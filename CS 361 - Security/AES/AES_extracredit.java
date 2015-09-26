/*
 * Name: Jung Yoon
 * UTEID: jey283
 * CS Account: jungyoon
 * E-MAIL: jungyoon@utexas.edu
 * Assignment: Assignment 4
 * Purpose: AES
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class AES {
	// *********** GLOBAL VARIABLES ************
	//		for general use
	private static byte[][] inputArr = new byte[4][4];
	private static byte[][] encState = new byte[4][4];
	private static byte[][] decState = new byte[4][4];
	// 		for 258-bit
	private static byte[][] key256 = new byte[4][8];
	private static byte[][] expandedKey256 = new byte[4][60];
	
	// *********** ARRAYS ************
	public static final int[] Rcon = {
		0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 
		0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 
		0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 
		0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 
		0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 
		0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 
		0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 
		0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 
		0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 
		0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 
		0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 
		0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 
		0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 
		0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 
		0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 
		0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d
	};
	
	final static int[] sTable = {
		0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
		0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
		0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
		0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
		0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
		0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
		0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
		0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
		0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
		0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
		0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
		0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
		0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
		0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
		0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
		0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16
	};
	
	final static int[] inverseTable = {
		0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB,
		0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB,
		0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E,
		0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25,
		0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92,
		0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84,
		0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06,
		0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
		0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73,
		0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E,
		0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B,
		0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4,
		0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F,
		0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF,
		0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61,
		0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D
	};
	
    final static int[] LogTable = {
		0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3, 
		100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193, 
		125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120, 
		101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142, 
		150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56, 
		102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16, 
		126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186, 
		43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87, 
		175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232, 
		44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160, 
		127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183, 
		204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157, 
		151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209, 
		83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171, 
		68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165, 
		103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112, 7
	};

    final static int[] AlogTable = {
    	1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53, 
    	95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170, 
    	229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49, 
    	83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205, 
    	76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136, 
    	131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154, 
    	181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163, 
    	254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160, 
    	251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65, 
    	195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117, 
    	159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 
    	155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84, 
    	252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202, 
    	69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14, 
    	18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23, 
    	57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246, 1
    };
    
    /******************************************************
    *******************************************************
    *******************************************************
    * MAIN
    *******************************************************
    *******************************************************
    *******************************************************/
	public static void main (String[] args) throws IOException{
		// *********** LOCAL VARIABLES ************	
		// 		default settings
		String option = "";	
		String mode = "ECB";
		int length = 256;
		String keyLine = "";
		String line = "";
		String inputLine = "";
		int count = 0;
		int count1 = 0;
		// 		for files
		String inputFileName = "";
		BufferedReader inputRead = null;
		BufferedReader keyRead = null;
		PrintWriter printEnc = null;
		PrintWriter printDec = null;
		
		// ****************************************************
		// ************* 1) TAKE IN ARGUMENTS
		// ****************************************************
		if((args.length == 3){
			// option
			if(args[0].equalsIgnoreCase("e"))
				option = "Encrypt";
			else if(args[0].equalsIgnoreCase("d"))
				option = "Decrypt";
			else
				System.err.println("ERROR: invalid option. Please pick 'e' or 'd'.");
			
			// ** NORMAL RUN COMMAND **
			// command in format: java AES option keyFile inputFile
				// keyFile
				keyRead = new BufferedReader(new FileReader(args[1]));
				
				// inputFile
				inputRead = new BufferedReader(new FileReader(args[2]));
				inputFileName = args[2];
		
			// ****************************************************
			// ************* 2) READ KEY FILE & GET EXPANDED KEY
			// ****************************************************
			keyLine = keyRead.readLine();
			// AES-256 KEY EXPANSION 
				if((keyLine != null) && keyLine.length() == 64){			
					count = 0;
					while(count != 64){
						for(int c = 0; c < 8; c++){
							for(int r = 0; r < 4; r++){
								// load key array and first block of expanded key
								key256[r][c] = (byte) Integer.parseInt(keyLine.substring(count, count+2), 16);
								expandedKey256[r][c] = (byte) Integer.parseInt(keyLine.substring(count, count+2), 16);
								count = count + 2;
							}
						}
					}	
					keyExpansion256();
				}
				else
					System.err.println("ERROR: invalid key for AES-256.");
			}	
			
			// ****************************************************		
			// ************* 3) CHECK OPTION & HANDLE E OR D
			// ****************************************************
			// creates .enc and .dec files
			printEnc = new PrintWriter(new FileWriter(inputFileName + ".enc"));
			printDec = new PrintWriter(new FileWriter(inputFileName + ".dec"));
				
			// ************* ENCRYPTION 
			if(option.equals("Encrypt")){
				// repeat for all input lines that are should be 32 bits long
				while ((inputLine = inputRead.readLine()) != null) {
					// A) ignore malformed lines, ignore long lines, & pad short lines
					if(inputLine.matches("[0-9a-fA-F]+") && (inputLine.length() <= 32) && !inputLine.equals("")){
						line = inputLine;
						if(inputLine.length() < 32){
							for(int i=0; i< (32-inputLine.length()); i++)
								line = line + "0";
						}
						inputLine = line;
						
						// B) store current line into input array & update the state
						count1 = 0;
						while(count1 != 32){
							for(int c = 0; c < 4; c++){
								for(int r = 0; r < 4; r++){
									inputArr[r][c] = (byte) Integer.parseInt(inputLine.substring(count1, count1+2),16);
									encState[r][c] = (byte) Integer.parseInt(inputLine.substring(count1, count1+2),16);
									count1 = count1 + 2;
								}
							}
						}	
						
						// C) carry out rounds based on AES-length 
						else{
							for(int round = 1; round < 16; round++){
								// first round
								if(round == 1){
									addRoundKey(round, length);
								}
								// last round
								else if(round == 15){
									subBytes();
									shiftRows();
									addRoundKey(round, length);
								}
								// intermediate rounds
								else{
									subBytes();
									shiftRows();
									for (int i = 0; i < 4; i++)
										mixColumn2(i);
									addRoundKey(round, length);
								}
							}
						}
					
					// ----- ENCRYPTION [FOR LINE] END

					// PRINT TO .ENC FILE
					printToEncFile(printEnc);
					
					// clear state and encrypt next line, if any
					encState = new byte[4][4];
					}
				}
			}
			// ************* DECRYPTION
				else{
					while ((inputLine = inputRead.readLine()) != null) {
						// A) ignore malformed lines, ignore long lines, & pad short lines
						if(inputLine.matches("[0-9a-fA-F]+") && (inputLine.length() <= 32) && !inputLine.equals("")){
							line = inputLine;
							if(inputLine.length() < 32){
								for(int i=0; i< (32-inputLine.length()); i++){
									line = line + "0";
								}
							}
							inputLine = line;
							
							// B) store current line into input array & update the state
							count1 = 0;
							while(count1 != 32){
								for(int c = 0; c < 4; c++){
									for(int r = 0; r < 4; r++){
										inputArr[r][c] = (byte) Integer.parseInt(inputLine.substring(count1, count1+2),16);
										decState[r][c] = (byte) Integer.parseInt(inputLine.substring(count1, count1+2),16);
										count1 = count1 + 2;
									}
								}
							}	
										
									
							// C) carry out rounds based on AES-length 
							else{
								for(int round = 15; round > 0; round--){
									// first round
									if(round == 15){
										addRoundKeyInverse(round, length);
										shiftRowsInverse();
										subBytesInverse();
									}
									// last round
									else if(round == 1){
										addRoundKeyInverse(round, length);
									}
									// intermediate rounds
									else{
										addRoundKeyInverse(round, length);
										for (int i = 0; i < 4; i++)
											invMixColumn2(i);
										shiftRowsInverse();
										subBytesInverse();
									}
								}
							}
						
							// ----- ENCRYPTION [FOR LINE] END
							
							// PRINT TO .DEC FILE
						
								printToDecFile(printDec);
							
							// clear state and encrypt next line if any
							decState = new byte[4][4];
							X = new byte[4][4];
						}	
	
					}
				}		
			} // ----- end of processing
		  	else {
		  		System.err.println("ERROR: invalid number of arguments.");
		  		return; 
		  	}
			
			// timing data		
			/*System.out.println("**************************************************\n" +
							"****************** TIMING DATA *******************");
			System.out.println(option + "ion of file (" + inputFileName + ") at AES-" + length + "/" + mode + ":");
			System.out.println("\tFor 1000 lines of input: ");	
			//long end = System.currentTimeMillis();
			//System.out.println("\t=" + (double)((128000)/1048576.0)/((end - start)/1000000.0) + " MB/s");	
			 */		
		
		// close all files
		printEnc.flush();
		printDec.flush();
		printEnc.close();
		printDec.close();
		inputRead.close();
		keyRead.close();
	}
	
	
    /******************************************************
    *******************************************************
    *******************************************************
    * KEY EXPANSION
    *******************************************************
    *******************************************************
    *******************************************************/ 
    private static void keyExpansion256() {
    	int n = 32, b = 240, rConIternation = 0, numBytes = 0, currRow = 0, currCol = 0; 
    	byte[] tempT = new byte[4];
    	
    	// 1) The first n bytes of the expanded key are simply the encryption key.
    	// --> already taken care of in keyFile read-in process
    	
    	// 2) The rcon iteration value i is set to 1
    	rConIternation = 1;
    	
    	// 3) Until we have b bytes of expanded key, we do the following to generate all bytes 
    	numBytes = n;
    	currRow = 0;
    	currCol = 8;
    	while(numBytes < b){
    		// ***** NEXT 4 BYTES *****
    		// A1) We create a 4-byte temporary variable, t
    		for(int i = 0; i < 4; i++)
    			tempT[i] = expandedKey256[i][currCol-1];
    		// A2) rotate 
    		for (int j = 0; j < 4; j++){
    			if(j == 3)
    				tempT[j] = expandedKey256[0][currCol-1];
    			else
    				tempT[j] = expandedKey256[j+1][currCol-1];
    		}
    		// A3) S-Box
    		for (int k = 0; k < 4; k++)
    			tempT[k] = (byte) sTable[(tempT[k] & 0xFF)];
    		// A4) rCon
    		for (int l = 0; l < 4; l++)
    		{
    			if(l == 0)
    				tempT[l] = (byte) (tempT[l] ^ Rcon[rConIternation]);
    			else
    				tempT[l] = (byte) (tempT[l] ^ 0); 
    		} 
    		// A5) increment i
    		rConIternation++;
    		// A6) XOR 
    		for (int m = 0; m < 4; m++)
    			tempT[m] = (byte)(tempT[m] ^ expandedKey256[m][currCol-8]);
    		// A7) add tempT into expandedKey
    		for(int p = 0; p < 4; p++)
    			expandedKey256[p][currCol] = tempT[p];
    		// A8) increment counts
    		currCol++;
    		numBytes += 4;
    		// ***** NEXT 12 BYTES *****
    		for(int q = 0; q<3; q++){
        		// B1) We create a 4-byte temporary variable, t
        		for(int r = 0; r < 4; r++)
        			tempT[r] = expandedKey256[r][currCol-1];
        		// B2) XOR first byte
        		for (int m = 0; m < 4; m++)
        			tempT[m] = (byte)(tempT[m] ^ expandedKey256[m][currCol-8]);
        		// B3) add tempT into expandedKey
        		for(int p = 0; p < 4; p++)
        			expandedKey256[p][currCol] = tempT[p];
        		// B4) increment counts
        		currCol++;
        		numBytes += 4;
    		}
    		// --------> FOR THE EXTRA BYTES AT THE END WE DON'T NEED
    		if(numBytes < (b-16)){
    			// ***** NEXT 4 BYTES *****
        		// C1) We create a 4-byte temporary variable, t
        		for(int r = 0; r < 4; r++)
        			tempT[r] = expandedKey256[r][currCol-1];
        		// C2) S-Box
        		for (int k = 0; k < 4; k++)
        			tempT[k] = (byte) sTable[(tempT[k] & 0xFF)];
        		// B2) XOR first byte
        		for (int m = 0; m < 4; m++)
        			tempT[m] = (byte)(tempT[m] ^ expandedKey256[m][currCol-8]);
        		// B3) add tempT into expandedKey
        		for(int p = 0; p < 4; p++)
        			expandedKey256[p][currCol] = tempT[p];
        		// B4) increment counts
        		currCol++;
        		numBytes += 4;
        		// ***** NEXT 12 BYTES *****
        		for(int q = 0; q<3; q++){
            		// B1) We create a 4-byte temporary variable, t
            		for(int r = 0; r < 4; r++)
            			tempT[r] = expandedKey256[r][currCol-1];
            		// B2) XOR first byte
            		for (int m = 0; m < 4; m++)
            			tempT[m] = (byte)(tempT[m] ^ expandedKey256[m][currCol-8]);
            		// B3) add tempT into expandedKey
            		for(int p = 0; p < 4; p++)
            			expandedKey256[p][currCol] = tempT[p];
            		// B4) increment counts
            		currCol++;
            		numBytes += 4;
        		}
    		}
    	}
    	

    /******************************************************
    *******************************************************
    *******************************************************
    * STEP #1: SUB BYTES
    *******************************************************
    *******************************************************
    *******************************************************/
	
    /*********************
	* METHOD: subBytes
	* 	Purpose: for encryption
	**********************/
    private static void subBytes() {
    	for (int i = 0; i < 4; i++){
    		for (int j = 0; j < 4; j++)
    			encState[i][j] = (byte)sTable[(encState[i][j] & 0xFF)];
    	}
	}

    /*********************
	* METHOD: subBytesInverse
	* 	Purpose: for decryption
	**********************/
	public static void subBytesInverse() 
	{
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				for (int k = 0; k < sTable.length; k++){
					if (sTable[k] == (decState[i][j] & 0xFF)){
						decState[i][j] = (byte) k;
						break;
					}
				}
			}
		}		
	}
	/******************************************************
    *******************************************************
    *******************************************************
    * STEP #2: SHIFT ROWS
    *******************************************************
    *******************************************************
    *******************************************************/

    /*********************
	* METHOD: shiftRows
	* 	Purpose: for encryption
	**********************/
	private static void shiftRows() 
	{	
        byte temp = encState[1][0];  
        
        encState[1][0]=encState[1][1];
        encState[1][1]=encState[1][2];
        encState[1][2]=encState[1][3];
        
        encState[1][3] = temp;  
        temp = encState[2][0];  
        
        encState[2][0] = encState[2][2];  
        encState[2][2] = temp;  
        temp = encState[2][1];  
        
        encState[2][1] = encState[2][3];  
        encState[2][3] = temp;  
        temp = encState[3][3]; 
         
        encState[3][3] = encState[3][2];  
        encState[3][2] = encState[3][1];  
        encState[3][1] = encState[3][0];  
        
        encState[3][0] = temp; 
	}
	
    /*********************
	* METHOD: shiftRowsInverse
	* 	Purpose: for decryption
	**********************/
	private static void shiftRowsInverse() 
	{
        byte temp = decState[3][0];  
  
        decState[3][0] = decState[3][1];  
        decState[3][1] = decState[3][2];  
        decState[3][2] = decState[3][3];  
        decState[3][3] = temp;  
 
        temp = decState[2][0];  
        decState[2][0] = decState[2][2];  
        decState[2][2] = temp;  
        
        temp = decState[2][1];  
        decState[2][1] = decState[2][3];  
        decState[2][3] = temp;  
        
        temp = decState[1][3];  
        decState[1][3]=decState[1][2]; 
        decState[1][2]=decState[1][1]; 
        decState[1][1]=decState[1][0]; 
        decState[1][0] = temp;
	}

	/******************************************************
    *******************************************************
    *******************************************************
    * STEP #3: MIX COLUMNS
    * <br> Borrowed from Professor Young
    * <br> http://www.cs.utexas.edu/~byoung/cs361/mixColumns-cheat-sheet
    *******************************************************
    *******************************************************
    *******************************************************/

	/*********************
	* METHOD: mixColumn2
	* Borrowed from Professor Young
	**********************/
    public static void mixColumn2 (int c) {	
    	byte a[] = new byte[4];
    	
    	for (int i = 0; i < 4; i++) 
    		a[i] = encState[i][c];

    	encState[0][c] = (byte)(mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
    	encState[1][c] = (byte)(mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
    	encState[2][c] = (byte)(mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
    	encState[3][c] = (byte)(mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));
    } 

    /*********************
	* METHOD: invMixColumn2
	* Borrowed from Professor Young
	**********************/
    public static void invMixColumn2 (int c) {
    	byte a[] = new byte[4];
    	
    	for (int i = 0; i < 4; i++) 
    		a[i] = decState[i][c];
	
    	decState[0][c] = (byte)(mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]));
    	decState[1][c] = (byte)(mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]));
    	decState[2][c] = (byte)(mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]));
    	decState[3][c] = (byte)(mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]));
    } 
    
    /*********************
	* HELPER METHOD: mul
	* Borrowed from Professor Young
	**********************/
    private static byte mul (int a, byte b) {
    	int inda = (a < 0) ? (a + 256) : a;
    	int indb = (b < 0) ? (b + 256) : b;

    	if ( (a != 0) && (b != 0) ) {
    		int index = (LogTable[inda] + LogTable[indb]);
    		byte val = (byte)(AlogTable[ index % 255 ] );
    		return val;
    	}
    	else 
    		return 0;
    }
    
    /******************************************************
    *******************************************************
    *******************************************************
    * STEP #4: ADD ROUND KEY
    *******************************************************
    *******************************************************
    *******************************************************/    
    
    /*********************
	* METHOD: addRoundKey
	* 	Purpose: for encryption
	**********************/
    private static void addRoundKey(int round, int length) {
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				if(length == 128)
					encState[i][j] = (byte)(encState[i][j] ^ expandedKey128[i][((round-1) * 4)+j]);
				else if(length == 196)
					encState[i][j] = (byte)(encState[i][j] ^ expandedKey196[i][((round-1) * 4)+j]);
				else
					encState[i][j] = (byte)(encState[i][j] ^ expandedKey256[i][((round-1) * 4)+j]);
			}
		}
	}
    
    /*********************
	* METHOD: addRoundKeyInverse
	* 	Purpose: for decryption
	**********************/
	public static void addRoundKeyInverse(int round, int length)
	{
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				if(length == 128)
					decState[j][i] = (byte)(decState[j][i] ^ expandedKey128[j][((round-1) * 4)+i]);
				else if(length == 196)
					decState[j][i] = (byte)(decState[j][i] ^ expandedKey196[j][((round-1) * 4)+i]);
				else
					decState[j][i] = (byte)(decState[j][i] ^ expandedKey256[j][((round-1) * 4)+i]);
			}
		}
	}
	
    /******************************************************
    *******************************************************
    *******************************************************
    * PRINTING
    *******************************************************
    *******************************************************
    *******************************************************/
    
    /*********************
	* METHOD: printToEncFile
	* 	Purpose: prints state to .enc file
	**********************/
    private static void printToEncFile(PrintWriter enc){
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++)
				enc.printf("%02X", encState[j][i]);
		}
		enc.print("\n");
    }
    
    /*********************
	* METHOD: printToDecFile
	* 	Purpose: prints state to .dec file
	**********************/
    private static void printToDecFile(PrintWriter dec){
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++)
				dec.printf("%02X", decState[j][i]);
		}
		dec.print("\n");
    }
