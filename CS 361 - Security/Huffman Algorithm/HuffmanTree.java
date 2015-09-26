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
* CLASS: HuffmanTree
* Borrowed from: http://rosettacode.org/wiki/Huffman_coding#Java
**********************/
abstract class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }
 
    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}