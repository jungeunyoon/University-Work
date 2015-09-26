/************************************************************************************
 * Name: Jung Yoon
 * UTEID: jey283
 * E-MAIL: jungyoon@utexas.edu
 * Assignment: Assignment 2
 * Purpose: Your assignment is to update your secure system from Assignment 1 
 * and add three new operations designed to introduce a covert channel into the 
 * system. You will implement the channel and use it to signal information from 
 * a high level user to a low level user. Finally, you will measure and report 
 * the bandwidth of the channel.
 ************************************************************************************
 * Notes to TA: 
 * I eliminated some parsing and syntax checking since we know that the instructions
 * will be correct but not all of it, otherwise the program would have been kind of
 * bare. 
 ************************************************************************************
 * --------- TIMING EXPERIMENTS ---------
 ******************************************
 * Reading from file: Metamorphosis (Size: 141,418 bytes)
 * Run 1 --- Time: 695 ms ; Bandwidth: 1624 bits/ms
 * Run 2 --- Time: 648 ms ; Bandwidth: 1744 bits/ms
 * Run 3 --- Time: 652 ms ; Bandwidth: 1728 bits/ms
 * Run 4 --- Time: 636 ms ; Bandwidth: 1776 bits/ms
 * Run 5 --- Time: 674 ms ; Bandwidth: 1672 bits/ms
 * ----- Avg Time: 661 ms ; Avg Bandwidth: 1708.8 bits/ms
 ******************************************
 * Reading from file: Pride and Prejudice (Size: 717,568 bytes)
 * Run 1 --- Time: 2360 ms ; Bandwidth: 2432 bits/ms
 * Run 2 --- Time: 2360 ms ; Bandwidth: 2432 bits/ms
 * Run 3 --- Time: 2313 ms ; Bandwidth: 2480 bits/ms
 * Run 4 --- Time: 2334 ms ; Bandwidth: 2456 bits/ms
 * Run 5 --- Time: 2338 ms ; Bandwidth: 2448 bits/ms
 * ----- Avg Time: 2341 ms ; Avg Bandwidth: 2449.6 bits/ms
 ******************************************
 * Reading from file: The Great Learning - Confucius (Size: 17,426 bytes)
 * Run 1 --- Time: 299 ms ; Bandwidth: 464 bits/ms
 * Run 2 --- Time: 321 ms ; Bandwidth: 432 bits/ms
 * Run 3 --- Time: 326 ms ; Bandwidth: 424 bits/ms
 * Run 4 --- Time: 326 ms ; Bandwidth: 424 bits/ms
 * Run 5 --- Time: 321 ms ; Bandwidth: 432 bits/ms
 * ----- Avg Time: 318 ms ; Avg Bandwidth: 436 bits/ms
 ******************************************
 * Machine Type: Macbook Pro - Intel Core i5
 * Clock Speed: 2.3 GHz
 ************************************************************************************/