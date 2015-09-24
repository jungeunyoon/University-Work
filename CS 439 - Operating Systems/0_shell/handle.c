#include <assert.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <unistd.h>
#include "util.h"


/*
 * First, print out the process ID of this process.
 *
 * Then, set up the signal handler so that ^C causes
 * the program to print "Nice try.\n" and continue looping.
 *
 * Finally, loop forever, printing "Still here\n" once every
 * second.
 */

void handler(int sig) {
   ssize_t bytes;
   const int STDOUT = 1;
   bytes = write(STDOUT, "Nice try.\n", 10);
   if(bytes != 10)
      exit(-999);
}

void handler2(int sig) {
   ssize_t bytes;
   const int STDOUT = 1;
   bytes = write(STDOUT, "exiting\n", 8);
   exit(1);
}

int main(int argc, char **argv)
{

   // Kyung driving now

   pid_t pid = getpid();
   printf("%d\n", pid);
   signal(SIGINT, handler);
   signal(SIGUSR1, handler2);

   while(1) {  /* busy loop */
      struct timespec temp;
      temp.tv_sec = 1;
      temp.tv_nsec = 0;
      /* prints "Still here" about every 1 second */
      while(nanosleep(&temp, &temp) == -1);
      printf("Still here\n");
   }
   return 0;

   // end of Kyung driving
}


