#include <stdio.h>
#include <stdbool.h>

/* function to swap array elements */

int permutations[50000][300];
int totalPerm, totalBids;
int bids[50000][3];
bool saveIt = true;

void swap (int v[], int i, int j) {
	int	t;

	t = v[i];
	v[i] = v[j];
	v[j] = t;
}

void perm (int v[], int n, int i)
{
    /* this function generates the permutations of the array
    * from element i to element n-1
    */
    int     j,a=0, l, bidID2,  bidID, q;
    /* if we are at the end of the array, we have one permutation
    * we can use (here we print it; you could as easily hand the
    * array off to some other function that uses it for something
    */
    if (i == n)
    {
        // save permutations;
        saveIt = true;
	l = 0;
        for(l = 0; l < totalBids; l++)
        {
            if(bids[l][0] == 1)
            { // single-item bid

//printf("TAKEN BID %d\n", l);
                bidID = l;
//printf("testing first bid : %d \n", bidID);
//printf("bid should equal value at bid %d = %d  \n", bidID,v[(bids[bidID][2])]);
                if(v[(bids[bidID][2])] == bidID)
                {
//printf("testing first bid : %d  \n", bidID);
                    saveIt = true;
                    for(q = 0; q < totalBids; q++)
                    {
                        if((bids[q][0] == 1) && (q != bidID))
                        { // single-item bid
//printf("testing first bid : %d --- second bid to test : %d \n", bidID, bidID2);
                            bidID2 = q;
                            for (j=0; j < n; j++)
                            {
                                if(v[j] == bidID2 )
                                v[j] = -1;
                            }
                        }
                    }
                    break;
                }
                else if(v[(bids[bidID][2])] == (-1))
                {
                    for(q = 0; q < totalBids; q++)
                    {
                        if(bids[q][0] == 1)
                        { // single-item bid
                            bidID2 = q;
                            for (j=0; j < n; j++)
                            {
                                if(v[j] == bidID2 )
                                v[j] = -1;
                            }
                        }
                    }
                    break;
                }
                else
                saveIt = false;
            }
        }
        // saves permutation
        if(saveIt)
        {
            for (j=0; j < n; j++)
            {
                permutations[a][j] = v[j];
                a++;
                totalPerm = a;
                printf("%d ", v[j]);
            } printf("\n");
       }
    }
    else
    {
        /* recursively explore the permutations starting
        * at index i going through index n-1
        */
        for (j=i; j < n; j++)
        {
            /* try the array with i and j switched */
            swap (v, i, j);
            perm (v, n, i+1);
            /* swap them back the way they were */
            swap (v, i, j);
        }
    }
}


/**************************/

int main(int argc, char *argv[]) 
{
    // ensures that input file is passed correctly
    if (argc != 2)	
    	printf("Error: invalid input.");

    // READS FILE
    FILE *file = fopen( argv[1], "r" );
    int p, i, j, n, row, col, numEntries, nextValue;
    char c[10000];
    fscanf(file, "%s", c);
    numEntries = atol(&c);
    int rooms[numEntries+1][2];

    // takes in hotel entries
    for(i=0 ; i < numEntries ; i++){
    fscanf(file, "%s", c);
    nextValue = atol(&c);
    rooms[i][0] = nextValue;
    fscanf(file, "%s", c);
    nextValue = atol(&c);
    rooms[i][1] = nextValue;
    }

    // takes in bids  
    j = 0, n = 0;  
    while(fscanf(file, "%s", c) != EOF)
    {
    nextValue = atol(&c);
        if(nextValue == 3)
        {
//	    printf("%d\n", nextValue);
        }     
	// takes linear bids
	else if(nextValue == 2)
	{
            bids[j][0] = 2;
	    fscanf(file, "%s", c);
	    nextValue = atol(&c);
            bids[j][1] = nextValue;
  	    fscanf(file, "%s", c);
    	    nextValue = atol(&c);
            bids[j][2] = nextValue;
	    j++;
	}
	// takes single-item bid
        else if(nextValue == 1)
        {
            bids[j][0] = 1;
            fscanf(file, "%s", c);
            nextValue = atol(&c);
            bids[j][1] = nextValue;
            fscanf(file, "%s", c);
            nextValue = atol(&c);
            bids[j][2] = nextValue;
	    n++;
	    j++;
        }
    }
    totalBids = j; // sets global variable

    // creates permutations 
    if(numEntries > j)
	p = numEntries + 1;
    else
	p = j + 1;
    int v[p];
	for (i=0; i<(p-1); i++)
	     v[i] = i;
	v[p-1] = -1;
    perm(v, p, 0);

    // goes through permutations and finds best option
/*    for(m = 0; m < numEntries; m++){
	
    }    
  */  
    fclose(file);    
    return 0;
}
