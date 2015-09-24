/*
 * Name: Jung Yoon
 * UTEID: jey283
 * CS Account: jungyoon
 * E-MAIL: jungyoon@utexas.edu
 * Assignment: Assignment 3
 * Purpose: Suppose you have a table indicating the relative frequencies of 
 * symbols in a language. You already know how to compute the entropy of the 
 * language. You also know that the Huffman algorithm generates an encoding 
 * that is pretty good. Your task in this assignment is to automate the process.
 * 
 * Huffman Code Algorithm from: http://rosettacode.org/wiki/Huffman_coding#Java
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class Encoder {
	
	//public static int totalFrequencies;
	//		MAP: holds model values (SYMBOL, FREQUENCY, AND HUFFMAN CODE) 
	// 		--> ArrayList holds pos(0) = frequency; pos(1) = probability; pos(2) = huffman code
	static Map <String, ArrayList<Object>> model = new HashMap <String, ArrayList<Object>>();
	static Map <String, ArrayList<Object>> model2 = new HashMap <String, ArrayList<Object>>();
	static Map <String, ArrayList<Object>> model3 = new HashMap <String, ArrayList<Object>>();
    // 		for calculations
    static int count1 = 0;
    static int count2 = 0;
    
	public static void main (String[] args) throws IOException{
		// variables:
		// 		for file reading and frequencies
		String fileName = null;
		char sym;
		int k = 10000;
		BufferedReader reader = null;
		String line;
		double[] charProbabilities = new double[26];
		double probability = 0.0;
		double freq = 0.0;
		int totalFrequencies = 0;
		int lineCount = 0;
		double entropy = 0.0;
		double prob = 0.0;
        ArrayList<Object> huffman = null;
		// 		for creating volumes
        PrintWriter text = new PrintWriter(new FileWriter("testText")); 
        PrintWriter textEnc1 = new PrintWriter(new File("testText.enc1"));
        PrintWriter textEnc2 = new PrintWriter(new FileWriter("testText.enc2"));
        PrintWriter textDec1 = new PrintWriter(new FileWriter("testText.dec1"));
        PrintWriter textDec2 = new PrintWriter(new FileWriter("testText.dec2"));
        // 		for multiple-chararacter models
        ArrayList<Object> huffman2 = null;
        //		for extra credit
        int numOfCharacters = 0;
        boolean extraCredit = false;
        StringBuffer possibleLetters = new StringBuffer(26);
        

		// ***** 1. check for valid arguments *****
    	if (args.length >= 2) {
    		fileName = args[0];
    		// for extra credit part 1
    		if(args.length == 3){
    			if(Integer.parseInt(args[2]) <= 1)
    				System.err.println("ERROR: j must be greater than 1.");
    			extraCredit = true;
    			numOfCharacters = Integer.parseInt(args[2]);
    		}
    		try{  
				if(Integer.valueOf(args[1]) instanceof Integer)
					k = Integer.parseInt(args[1]);
			}
			catch(NumberFormatException e){
				System.err.println("ERROR: k is not an integer.");
			}	
    	}
    	else {
    		System.err.println("ERROR: invalid number of arguments.");
    	}
    	
		reader = new BufferedReader(new FileReader(fileName));
		System.out.println("Reading from file: " + fileName);
		
		// ***** 2. read in a list of n non-negative integers from a file *****
		// 2a. access relative frequencies
		while ((line = reader.readLine()) != null) {
			huffman = new ArrayList<Object>();
			if(!line.equals("")){
				if((Integer.parseInt(line) != 0)){
				sym = (char)(lineCount + 65);
				huffman.add(Integer.parseInt(line));
				totalFrequencies += Integer.parseInt(line);
				model.put(Character.toString(sym), huffman);
				// extra credit part 1
				//if (extraCredit)
					possibleLetters.append(Character.toString(sym));
				}
			}
			lineCount++;
		}
		// 2b. access probabilities and entropy
		for(String ks : model.keySet()){
			huffman = new ArrayList<Object>();
			freq = (double)(Integer)(model.get(ks).get(0));
			prob = freq/totalFrequencies;
			huffman = model.get(ks);
			huffman.add(prob);
			model.put(ks, huffman);
			entropy += (double)(prob * (Math.log(prob) / Math.log(2)));
		}
		entropy *= -1;
		
		
		// ***** 3. use the Huffman algorithm to devise a binary encoding *****
		// build tree and brint out results
        HuffmanTree tree = buildTree(1);
        System.out.println("\n******************************" +
        		"\n***** First-Order Model: *****" +
        		"\n******************************");
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(1, tree, new StringBuffer());
        
        
        // ***** 4. create a volume of text *****
        generateVolume(1, text, totalFrequencies, k);
        encodeVolume(1, textEnc1);
        decodeVolume(1, textDec1);
     
        String newSymbol = "";
        int newFreq = 0;
        double newProb = 0.0;
        

        // ***** 5. consider the 2-symbol derived alphabet *****
        for(String key1 : model.keySet()){
        	for(String key2 : model.keySet()){
        		newSymbol = key1 + key2;
        		newFreq = (int)model.get(key1).get(0) * (int)model.get(key2).get(0);
        		newProb = (double)model.get(key1).get(1) * (double)model.get(key2).get(1);
        		huffman2 = new ArrayList<Object>();
        		huffman2.add(newFreq);
        		huffman2.add(newProb);
        		model2.put(newSymbol, huffman2);
        	}
        }
        HuffmanTree tree2 = buildTree(2);
        System.out.println("\n******************************" +
        		"\n***** Second-Order Model: ****" +
        		"\n******************************");
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(2, tree2, new StringBuffer());
        
        
        // ***** 6. Re-run your text from step 3 above *****
        encodeVolume(2, textEnc2);
        decodeVolume(2, textDec2);
        
        
        // ***** 7. At the end, print a nice summary of your results. *****
        /*Measure the efficiency of your encoding by computing the actual average bits 
         * per symbol as you do the translation. Compare this with the computed entropy 
         * of the language (from step 1) and record the percentage difference.
         */
        System.out.println("\n******************************" +
        		"\n********** Summary: **********" +
        		"\n******************************");
        double av1 = (double)count1/k;
        double av2 = (double)count2/k;
        System.out.println("Entropy of Language: " + entropy);
        System.out.println("* First-Order Model:" + count1);
        System.out.println("\tAverage Bits/Symbol: "  + (double)count1/k);
        System.out.println("\tPercent Difference: %" + Math.abs(100* ((av1 - entropy)/(entropy) )));
        System.out.println("* Second-Order Model: " + + count2);
        System.out.println("\tAverage Bits/Symbol: " + (double)count2/k);
        System.out.println("\tPercent Difference: %" + Math.abs(100*((av2 - entropy)/(entropy))));
        // END OF ASSIGNMENT

        
        // ***** 8. EXTRA CREDIT - PART 1 *****
        if(extraCredit){
        	generateCombinations(possibleLetters.toString(), numOfCharacters, numOfCharacters, new StringBuffer());
        	HuffmanTree tree3 = buildTree(3);
            System.out.println("\n******************************" +
            		"\n******* EXTRA CREDIT: ********" +
            		"\n******************************");
            System.out.println("Generating an encoding on the " +
            		numOfCharacters + "-symbol derived alphabet:");
            printCodes(3, tree3, new StringBuffer());
        }
        

        
        // close all PrintWriters
        text.close();
        textEnc1.close();
        textDec1.close();
        textEnc2.close();
        textDec2.close();
    }
    
	
    /*********************
	* METHOD: generateCombinations
	* 	purpose: gets all possible combinations, taking sequences of numOfCharacters symbols at a time.
	**********************/
	static void generateCombinations(String input, int numOfCharacters, int count, StringBuffer output) {
		String symbol;
		int freq = 0;
		char c;
		double prob = 0.0;
		// puts combination into map
        if (count == 0) {
        	symbol = output.toString();
        	ArrayList<Object> huffman = new ArrayList<Object>();
        	freq = 0;
        	prob = 0;
        	for(int i=0; i<numOfCharacters; i++){
        		c = symbol.charAt(i);
        		// calculate joint frequency and probability
        		if(i == 0){
        			freq = (int)model.get(Character.toString(c)).get(0);
        			prob = (double)model.get(Character.toString(c)).get(1);
        		}
        		else{
        			freq *= (int)model.get(Character.toString(c)).get(0);
        			prob *= (double)model.get(Character.toString(c)).get(1);
        		}
        	}
        	huffman.add(freq);
        	huffman.add(prob);
        	model3.put(symbol, huffman);
            
        } else {
        	// permutation to get all combinations
            for (int i = 0; i < input.length(); i++) {
                output.append(input.charAt(i));
                generateCombinations(input, numOfCharacters, count - 1, output);
                output.deleteCharAt(output.length() - 1);
            }
        }
    }
	
    /*********************
	* METHOD: generateVolume
	* 	purpose: Create a volume of text (k characters, e.g., 10000 characters) in 
	* 	your alphabet using a random function to generate characters at the expected 
	* 	probabilities, and store the text in a file named testText.
	* "Suppose you want to generate characters in the alphabet {A,B,C,D} with proportions of 
	* 4/10, 2/10, 3/10, and 1/10, respectively. One way would be to divide a dartboard into 
	* regions of those exact proportional sizes and throw a dart at it. If the dart landed in 
	* the A region (which consumes 40% of the total area), write down "A", etc."
	**********************/
	private static void generateVolume(int modelType, PrintWriter text, int totalFrequencies, int k) throws IOException {
		double cumulativeProbability = 0.0;
		for (int i = 0; i < k; i++)
		{
			// random function to generate characters at the expected probabilities
			double p = Math.random();
			cumulativeProbability = 0.0;
			for (String keys : model.keySet()) {
			    cumulativeProbability += (double)model.get(keys).get(1);
			    if (p <= cumulativeProbability) {
			    	// print to testText
			    	text.print(keys.charAt(0));
			    	break;
			    }			
			}
		}
		text.close();
	}
    
    /*********************
	* METHOD: encodeVolume
	* 	purpose: Write a utility to encode the text using your encoding 
	* 	into a file named testText.enc1
     * @throws IOException 
	**********************/
    public static void encodeVolume (int modelType, PrintWriter encodeWrite) throws IOException {    
    	// variables
        int ch = 0, charCount = 1;
        String sBinary = "";
        BufferedReader text = null;
        text = new BufferedReader(new InputStreamReader(new FileInputStream("testText")));
        
            while(!((ch = text.read()) == -1)) 
            {
            	sBinary = sBinary + (char)ch;
            	// accounts for multiple symbol models
            	if (charCount < modelType){
            		charCount++;
            		continue;
            	}
            	else
            	{
            		charCount = 1;
            			// prints to logs
            			if(modelType == 1)
            				encodeWrite.println(model.get(sBinary).get(2));
            			else{
            				encodeWrite.println(model2.get(sBinary).get(2));
            			}
            		sBinary = "";
            	}
            }
        
        encodeWrite.close();
        text.close();
    }
    
    
    /*********************
	* METHOD: decodeVolume
	* 	purpose: a utility to decode this file into another file testText.dec1 
     * @throws IOException 
	**********************/
    public static void decodeVolume (int modelType, PrintWriter writer) throws IOException 
    {
        BufferedReader text = new BufferedReader(new InputStreamReader(new FileInputStream("testText.enc" + modelType)));
        String line = "";
		while (!((line = text.readLine()) == null)) {
		  // ONE-SYMBOL 
		  if(modelType == 1){
	    	  for (String key : model.keySet())
	    	  {
	    		  if (line.equals(model.get(key).get(2)))
	    		  {
	    			  count1 += line.length();
	    			  writer.print(key);
	    			  break;
	    		  }
	    	  }
		  	}
		  // TWO-SYMBOL
		  else{
			  for (String key : model2.keySet())
	    	  {
	    		  if (line.equals(model2.get(key).get(2)))
	    		  {
	    			  count2 += line.length();
	    			  writer.print(key);
	    			  break;
	    		  }
	    	  }
		  	}
		  }
        writer.close();
        text.close();
    }
    
   /*******************************************************
    *******************************************************
    *******************************************************
    * BORROWED/MODIFIED METHODS STARTS HERE
    *******************************************************
    *******************************************************
    *******************************************************/
    
    /*********************
	* METHOD: buildTree
	* Borrowed from: http://rosettacode.org/wiki/Huffman_coding#Java
	**********************/
	// input is an array of frequencies, indexed by character code
    public static HuffmanTree buildTree(int modelType) {
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();
        // initially, we have a forest of leaves
        // one for each non-empty character
        switch(modelType){
        	case 1:	// first-order model
                for(String key : model.keySet()){
                	trees.offer(new HuffmanLeaf((int)model.get(key).get(0), key));
                }
        		break;
        	case 2:	// second-order model
        		for(String key2 : model2.keySet()){
                	trees.offer(new HuffmanLeaf((int)model2.get(key2).get(0), key2));
                }
        		break;
        	case 3:	// extra credit: second-order model
        		for(String key3 : model3.keySet()){
                	trees.offer(new HuffmanLeaf((int)model3.get(key3).get(0), key3));
                }
        		break;
        }

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();
 
            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }
    
    
    /*********************
	* METHOD: printCodes
	* Borrowed from: http://rosettacode.org/wiki/Huffman_coding#Java
	**********************/
    public static void printCodes(int modelType, HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        
        switch(modelType){
        // first-order model
    	case 1:	
            if (tree instanceof HuffmanLeaf) {
                HuffmanLeaf leaf = (HuffmanLeaf)tree;
     
                // print out character, frequency, and code for this leaf (which is just the prefix)
                System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
                ArrayList<Object> huff = model.get(leaf.value);
                huff.add(prefix.toString());
                model.put(leaf.value, huff);
     
            } else if (tree instanceof HuffmanNode) {
                HuffmanNode node = (HuffmanNode)tree;
     
                // traverse left
                prefix.append('0');
                printCodes(1, node.left, prefix);
                prefix.deleteCharAt(prefix.length()-1);
     
                // traverse right
                prefix.append('1');
                printCodes(1, node.right, prefix);
                prefix.deleteCharAt(prefix.length()-1);
            }
    		break;
    	// second-order model
    	case 2:	
            if (tree instanceof HuffmanLeaf) {
                HuffmanLeaf leaf = (HuffmanLeaf)tree;
     
                // print out character, frequency, and code for this leaf (which is just the prefix)
                System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
                ArrayList<Object> huff = model2.get(leaf.value);
                huff.add(prefix.toString());
                model2.put(leaf.value, huff);
     
            } else if (tree instanceof HuffmanNode) {
                HuffmanNode node = (HuffmanNode)tree;
     
                // traverse left
                prefix.append('0');
                printCodes(2, node.left, prefix);
                prefix.deleteCharAt(prefix.length()-1);
     
                // traverse right
                prefix.append('1');
                printCodes(2, node.right, prefix);
                prefix.deleteCharAt(prefix.length()-1);
            }
    		break;
    	// extra credit: second order model
    	case 3:
            if (tree instanceof HuffmanLeaf) {
                HuffmanLeaf leaf = (HuffmanLeaf)tree;
     
                // print out character, frequency, and code for this leaf (which is just the prefix)
                System.out.println(leaf.value + "\t" + leaf.frequency + "\t" + prefix);
                ArrayList<Object> huff = model3.get(leaf.value);
                huff.add(prefix.toString());
                model3.put(leaf.value, huff);
     
            } else if (tree instanceof HuffmanNode) {
                HuffmanNode node = (HuffmanNode)tree;
     
                // traverse left
                prefix.append('0');
                printCodes(3, node.left, prefix);
                prefix.deleteCharAt(prefix.length()-1);
     
                // traverse right
                prefix.append('1');
                printCodes(3, node.right, prefix);
                prefix.deleteCharAt(prefix.length()-1);
            }
    		break;
    }
      
    }
}
