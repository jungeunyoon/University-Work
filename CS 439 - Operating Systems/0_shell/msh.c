/* msh - A mini shell program with job control
 *
 * <Put your name and login ID here>
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <ctype.h>
#include <signal.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <errno.h>
#include "util.h"
#include "jobs.h"


/* Global variables */
int verbose = 0; /* if true, print additional output */

extern char **environ; /* defined in libc */
static char prompt[] = "msh> "; /* command line prompt (DO NOT CHANGE) */
static struct job_t jobs[MAXJOBS]; /* The job list */
/* End global variables */


/* Function prototypes */

/* Here are the functions that you will implement */
void eval(char *cmdline);
int builtin_cmd(char **argv);
void do_bgfg(char **argv);
void waitfg(pid_t pid);

void sigchld_handler(int sig);
void sigtstp_handler(int sig);
void sigint_handler(int sig);

/* Here are helper routines that we've provided for you */
void usage(void);
void sigquit_handler(int sig);



/*
 * main - The shell's main routine
 */
int main(int argc, char **argv)
{
	char c;
	char cmdline[MAXLINE];
	int emit_prompt = 1; /* emit prompt (default) */

	/* Redirect stderr to stdout (so that driver will get all output
	 * on the pipe connected to stdout) */
	dup2(1, 2);

	/* Parse the command line */
	while ((c = getopt(argc, argv, "hvp")) != EOF) {
		switch (c) {
			case 'h': /* print help message */
				usage();
				break;
			case 'v': /* emit additional diagnostic info */
				verbose = 1;
				break;
			case 'p': /* don't print a prompt */
				emit_prompt = 0; /* handy for automatic testing */
				break;
			default:
				usage();
		}
	}

	/* Install the signal handlers */

	/* These are the ones you will need to implement */
	Signal(SIGINT, sigint_handler); /* ctrl-c */
	Signal(SIGTSTP, sigtstp_handler); /* ctrl-z */
	Signal(SIGCHLD, sigchld_handler); /* Terminated or stopped child */

	/* This one provides a clean way to kill the shell */
	Signal(SIGQUIT, sigquit_handler);

	/* Initialize the job list */
	initjobs(jobs);

	/* Execute the shell's read/eval loop */
	while (1) {

		/* Read command line */
		if (emit_prompt) {
			printf("%s", prompt);
			fflush(stdout);
		}
		if ((fgets(cmdline, MAXLINE, stdin) == NULL) && ferror(stdin))
			app_error("fgets error");
		if (feof(stdin)) { /* End of file (ctrl-d) */
			fflush(stdout);
			exit(0);
		}

		/* Evaluate the command line */
		eval(cmdline);
		fflush(stdout);
		fflush(stdout);
	}

	exit(0); /* control never reaches here */
}

/*
 * eval - Evaluate the command line that the user has just typed in
 *
 * If the user has requested a built-in command (quit, jobs, bg or fg)
 * then execute it immediately. Otherwise, fork a child process and
 * run the job in the context of the child. If the job is running in
 * the foreground, wait for it to terminate and then return. Note:
 * each child process must have a unique process group ID so that our
 * background children don't receive SIGINT (SIGTSTP) from the kernel
 * when we type ctrl-c (ctrl-z) at the keyboard.
 */
void eval(char *cmdline)
{
	// Kyung and Jung drove here

	/* Outline taken from Bryant & O'Hall page 735 */

	/* variables*/
	char *argv[MAXARGS];
	char buf[MAXLINE];
	int bg;
	pid_t pid;
	struct job_t *addedjob;
	sigset_t mask; /* for signals */
	sigemptyset(&mask);
	sigaddset(&mask, SIGCHLD);

	strcpy(buf,cmdline); /* parse command line */
	bg = parseline(buf,argv);

	/**************************
	 * evaluates command line
	 **************************/
	if(argv[0] == NULL)
		return;
	if(!builtin_cmd(argv))
	{ /* built-in command */
		sigprocmask(SIG_BLOCK, &mask, NULL);

		pid = fork();
		if(pid < 0) { /* fork error handling */
			unix_error("Fork error");
		}
		else if(pid==0) { /* child */
			setpgid(0, 0);
			sigprocmask(SIG_UNBLOCK, &mask, NULL);
			if(execve(argv[0], argv,environ) < 0) {
				printf("%s: Command not found\n", argv[0]);
				exit(1);
			}
		}
		else { /* parent - not a build-in command */
			if(bg) { /* run in background */
				sigprocmask(SIG_UNBLOCK, &mask, NULL);
				addjob(jobs, pid, BG, cmdline);
				addedjob = getjobpid(jobs,pid);
				printf("[%d] (%d) %s", addedjob->jid, addedjob->pid, addedjob->cmdline);
			}
			else { /* run in foreground */
				sigprocmask(SIG_UNBLOCK, &mask, NULL);
				addjob(jobs, pid, FG, cmdline);
				waitfg(pid);
			}
		}
	}
	return;

	/* done with code excerpt/outline */

	// end of Kyung and Jung driving
}


/*
 * builtin_cmd - If the user has typed a built-in command then execute
 * it immediately.
 * Return 1 if a builtin command was executed; return 0
 * if the argument passed in is *not* a builtin command.
 */
int builtin_cmd(char **argv)
{
	// Jung driving now

	/* Taken from Bryant & O'Hall page 735 with minor edits */
	if(!strcmp(argv[0], "quit")) { /* quit command terminates the shell */
		exit(0);
	}
	else if(!strcmp(argv[0], "jobs")) { /* jobs command lists all background jobs */
		listjobs(jobs);
		return 1;
	}
	else if(!strcmp(argv[0], "bg") || !strcmp(argv[0], "fg")) { /* bg or fb command */
		do_bgfg(argv);
		return 1;
	}
	return 0; /* not a builtin command */
	/* done with code excerpt */

	// end of Jung driving, Kyung driving now
}

/*
 * do_bgfg - Execute the builtin bg and fg commands
 */
void do_bgfg(char **argv)
{
	// Kyung driving now

	/**************************
	 * validates arguments
	 **************************/
	if(argv[1] == NULL) {
		printf("%s command requires PID or %%jobid argument\n", argv[0]);
		return;
	}
	struct job_t *tempjob;
	if(argv[1][0] == '%') { /* jobid tests */
		int count = 1;
		while(argv[1][count] != NULL) {
			if(!isdigit(argv[1][count])) {
				printf("%s: argument must be a PID or %%jobid\n", argv[0]);
				return;
			}
			count++;
		}
		tempjob = getjobjid(jobs, atoi(&argv[1][1]));
		if(tempjob == NULL) { /* job not in list */
			printf("%s: No such job\n", &argv[1][0]);
			return;
		}
	}
	else { /* pid tests */
		int count = 0;
		while(argv[1][count] != NULL) {
			if(!isdigit(argv[1][count])) {
				printf("%s: argument must be a PID or %%jobid\n", argv[0]);
				return;
			}
			count++;
		}
		tempjob = getjobpid(jobs, atoi(&argv[1][0]));
		if(tempjob == NULL) { /* process not in list */
			printf("(%s): No such process\n", &argv[1][0]);
			return;
		}
	}

	// end of Kyung driving, Jung driving now

	/**************************
	 * runs bg and fg
	 **************************/
	if(strcmp(argv[0], "bg") == 0) { /* background */
		tempjob->state = BG;
		printf("[%d] (%d) %s", tempjob->jid, tempjob->pid, tempjob->cmdline);
		kill(-(tempjob->pid), SIGCONT);
	}
	else { /* foreground */
		tempjob->state = FG;
		kill(-(tempjob->pid), SIGCONT);
		waitfg(tempjob->pid);
	}
	return;

	// end of Jung driving, Kyung driving now
}

/*
 * waitfg - Block until process pid is no longer the foreground process
 */
void waitfg(pid_t pid)
{
	// Kyung driving now

	/* busy loop arounf sleep function */
	while(fgpid(jobs) == pid)
		sleep(1);
	return;

	// end of Kyung driving, Jung driving now
}

/*****************
 * Signal handlers
 *****************/

/*
 * sigchld_handler - The kernel sends a SIGCHLD to the shell whenever
 * a child job terminates (becomes a zombie), or stops because it
 * received a SIGSTOP or SIGTSTP signal. The handler reaps all
 * available zombie children, but doesn't wait for any other
 * currently running children to terminate.
 */
void sigchld_handler(int sig)
{
	// Jung driving now

	int status;
	pid_t pid;
	struct job_t *job;
	const int STDOUT = 1;
	/* check either status of children or return if no child zombied */
	while ((pid = waitpid(-1, &status, WNOHANG|WUNTRACED)) > 0) {

		job = getjobpid(jobs, pid);

		if (WIFSTOPPED(status)) { /* stopped or ctrl+z */
			job->state = ST;
			char buffer[100];
			sprintf(buffer, "Job [%d] (%d) stopped by signal %d\n", job->jid, job->pid, WSTOPSIG(status));
			write(STDOUT, buffer, strlen(buffer));
		} 
		else {
			if (WIFSIGNALED(status)) { /* terminated or ctrl+c */
				char buffer[100];
				sprintf(buffer, "Job [%d] (%d) terminated by signal %d\n", job->jid, job->pid, WTERMSIG(status));
				write(STDOUT, buffer, strlen(buffer));
			}
			deletejob(jobs, pid); /* delete job in all other cases */
		}
	}
	return;

	// end of Jung driving, Kyung driving now
}

/*
 * sigint_handler - The kernel sends a SIGINT to the shell whenver the
 * user types ctrl-c at the keyboard. Catch it and send it along
 * to the foreground job.
 */
void sigint_handler(int sig)
{
	// Kyung driving now

	pid_t pid;
	if((pid = fgpid(jobs)) != 0)
		kill(-pid, SIGINT);
	return;

	// end of Kyung driving
}

/*
 * sigtstp_handler - The kernel sends a SIGTSTP to the shell whenever
 * the user types ctrl-z at the keyboard. Catch it and suspend the
 * foreground job by sending it a SIGTSTP.
 */
void sigtstp_handler(int sig)
{
	// Kyung driving now

	pid_t pid;
	if((pid = fgpid(jobs)) != 0)
		kill(-pid, SIGTSTP);
	return;

	// end of Kyung driving
}

/*********************
 * End signal handlers
 *********************/



/***********************
 * Other helper routines
 ***********************/

/*
 * usage - print a help message
 */
void usage(void)
{
	printf("Usage: shell [-hvp]\n");
	printf(" -h print this message\n");
	printf(" -v print additional diagnostic information\n");
	printf(" -p do not emit a command prompt\n");
	exit(1);
}

/*
 * sigquit_handler - The driver program can gracefully terminate the
 * child shell by sending it a SIGQUIT signal.
 */
void sigquit_handler(int sig)
{
	ssize_t bytes;
	const int STDOUT = 1;
	bytes = write(STDOUT, "Terminating after receipt of SIGQUIT signal\n", 45);
	if(bytes != 45)
		exit(-999);
	exit(1);
}
