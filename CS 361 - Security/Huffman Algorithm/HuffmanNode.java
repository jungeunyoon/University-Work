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
* CLASS: HuffmanNode
* Borrowed from: http://rosettacode.org/wiki/Huffman_coding#Java
**********************/
class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees
 
    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}