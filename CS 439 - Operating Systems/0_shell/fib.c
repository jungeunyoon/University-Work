#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>

const int MAX = 13;

static void doFib(int n, int doPrint);


/*
 * unix_error - unix-style error routine.
 */
inline static void 
unix_error(char *msg)
{
    fprintf(stdout, "%s: %s\n", msg, strerror(errno));
    exit(1);
}


int main(int argc, char **argv)
{
  int arg;
  int print;

  if(argc != 2){
    fprintf(stderr, "Usage: fib <num>\n");
    exit(-1);
  }

  if(argc >= 3){
    print = 1;
  }

  arg = atoi(argv[1]);
  if(arg < 0 || arg > MAX){
    fprintf(stderr, "number must be between 0 and %d\n", MAX);
    exit(-1);
  }


  doFib(arg, 1);

  return 0;
}

/* 
 * Recursively compute the specified number. If print is
 * true, print it. Otherwise, provide it to my parent process.
 *
 * NOTE: The solution must be recursive and it must fork
 * a new child for each call. Each process should call
 * doFib() exactly once.
 */
static void 
doFib(int n, int doPrint)
{
   // Kyung driving now

   int status1 = 0, status2 = 0;
   pid_t child1, child2;
  
   if(n == 0 || n == 1) { /* base case */
      if(doPrint) {
         fprintf(stdout, "%d\n", n);
      }
      exit(n);
   }
   else {
      child1 = fork();
      if(child1 == 0) {  /* child process 1 */
         doFib(n-1, 0);
      }
      wait(&status1);
      child2 = fork();
      if(child2 == 0) { /* child process 2 */
         doFib(n-2, 0);
      }
      wait(&status2);
      if(doPrint) {  /* parent process */
         fprintf(stdout, "%d\n", WEXITSTATUS(status1) + WEXITSTATUS(status2));
      }
      exit(WEXITSTATUS(status1) + WEXITSTATUS(status2));
   }

   // end of Kyung driving      
}


