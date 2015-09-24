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

/*********************
* CLASS: HuffmanLeaf
* Borrowed from: http://rosettacode.org/wiki/Huffman_coding#Java
**********************/
class HuffmanLeaf extends HuffmanTree {
    public final String value; // the character this leaf represents
 
    // ***MODIFICATION: convert val to String to accept 2-character symbols
    public HuffmanLeaf(int freq, String val) {
        super(freq);
        value = val;
    }
}