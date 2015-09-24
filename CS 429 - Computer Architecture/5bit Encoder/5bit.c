/********************************************************************
	Jung Yoon
	UTEID: JEY283
	Section: #53820
	TA: Hang Yu
	Lab: #2
*********************************************************************/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

int main (int argc, char *argv[])
{
    // local variables
    int unsigned long bitstream = 0, temp;
    int c, a, m, n, b, lineCount = 0, finCh, i = 0, count = 0, multiple = 0, remainder = 0, pos = 0;
    int arr[102004];
    char ch;


/********************************************************************
*********DECODE*************DECODE***************DECODE**************
*********************************************************************/

    // decode: string to 5-bit
    // if (strcmp("-d", argv[1]) == 0){
    if (argc == 2 &&  (strcmp("-d", argv[1]) == 0)){
         while ((c = getchar()) != EOF){
             if (c != 10){  // ignores new lines
                 if ((c > 64) && (c < 91)){  // for letters
                     arr[count] = c - 65;
                     count++;
                 }
                 else if ((c > 47) && (c < 54)){  // for numbers
                     arr[count] = c - 22;
                     count++;
                }
             }
         } 

    // deocde: recreate bitstream and evaluate
    while (pos < count){  // stop at new line
        if (i < 7){
            bitstream = (bitstream << 5) + arr[pos];
	    // evaluate last 0-4 bytes
            if((i!=0) && ((pos+1)==count)){
                remainder = (((i+1)*5)%8);
                temp = bitstream >> remainder;
                b = ((i+1)*5)/8;
                b--;
                for (b; b >= 0; b--){
                    finCh = (temp >> (b*8)) & 0xFF;
		    printf("%c", (char) finCh);
                }
           }
           i++;
        }       
 	else if (i == 7){
           bitstream = (bitstream << 5) + arr[pos];
           // evaluate the 5 byes at a time
           for (a = 4; a >= 0; a--){
                finCh = (bitstream >> (a*8)) & 0xFF;
                    printf("%c", (char) finCh);
           }
           i = 0;
           bitstream = 0;
        }
        pos++;
    }
  }
   
/********************************************************************
*********ENCODE*************ENCODE***************ENCODE**************
*********************************************************************/
  
    else
    {
        // array of characters in file
	while ((c = getchar()) != EOF){
	    arr[pos] = c;
	    pos++;
	    count++;
	}

	// values needed for encoding process
	multiple = (count / 5);
	remainder = (count % 5);
	pos = 0;
	lineCount = 0;

	// ENCODES MULTIPLE OF 5
	for(a = 0; a < multiple; a++){
	    // make bitstream of 40 bits
            for(b = 0; b < 5; b++){
	        bitstream = (bitstream << 8) + (0x00 | arr[pos]);	
		pos++;
	    }
	    // convert 40 bits to 5 bits 
	    for(m = 7; m >= 0; m--){
	        finCh = (bitstream >> (m*5)) & 0x1F;

		// encode as letter A-F
		if ((0 <= finCh) && (finCh <= 25)){
		    finCh = finCh + 65;
		    printf("%c", (char) finCh);
		    // accounts for 72 char per line rule
                    lineCount++;
                    if (lineCount == 72){
                        lineCount = 0;
                        printf("\n");
                    }
		}    
		// encode as number 0-5
		else if ((26 <= finCh) && (finCh <= 31)){ 
		    finCh = finCh + 22;
                    printf("%c", (char) finCh);
		    // accounts for 72 char per line rule
		    lineCount++; 
                    if (lineCount == 72){
                        lineCount = 0;
                        printf("\n");
                    }
		}
	    }
	    bitstream = 0;
	}

	bitstream = 0; // not needed but just to be safe
        finCh = 0;

    if(remainder != 0){
	// ENCODES REMAINING BITS
	// make bitstream with remaining bits
	for(a=0; a < remainder; a++){
	    bitstream = (bitstream << 8) + (0x00 | arr[pos]);
	    pos++;
	}
	// pad bitstream with zeros if bits%5 !=0
	bitstream = (bitstream << (5-((remainder*8)%5))); 
	// count how many 5-bits that are left
	n = ((remainder*8) + (5-((remainder*8)%5)));
	n = n/5;
	n--;
	
	// encode the bits
        for(m = n; m >= 0; m--){
            finCh = (bitstream >> (m*5)) & 0x1F;
	    // encode as letter A-F
            if ((0 <= finCh) && (finCh <= 25)){
                finCh = finCh + 65;
                printf("%c", (char) finCh);
		// accounts for 72 char per line rule
		lineCount++;
                if (lineCount == 72){
                    lineCount = 0;
                    printf("\n");
                }
            }
	    // encode as number 0-5
            else if ((26 <= finCh) && (finCh <= 31)){        
                finCh = finCh + 22;
                printf("%c", (char) finCh);
	        // accounts for 72 char per line rule 
		lineCount++;
                if (lineCount == 72){
                    lineCount = 0;
                    printf("\n");
                }
            }
        }
      }
    }  
}
