#include <assert.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>

/*
 * Takes a process ID as an argument and that sends
 * the SIGUSR1 signal to the specified process ID
 */

int main(int argc, char **argv)
{
   // Kyung driving now

   kill(atoi(argv[argc-1]), SIGUSR1);
   return 0;

   // end of Kyung driving
}
