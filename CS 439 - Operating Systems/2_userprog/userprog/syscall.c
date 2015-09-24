#include "userprog/syscall.h"
#include <stdio.h>
#include <syscall-nr.h>
#include <user/syscall.h>
#include "threads/interrupt.h"
#include "devices/input.h"
#include "devices/shutdown.h"
#include "filesys/file.h"
#include "filesys/filesys.h"
#include "threads/synch.h"
#include "threads/thread.h"
#include "threads/vaddr.h"
#include "userprog/pagedir.h"
#include "userprog/process.h"

static void syscall_handler (struct intr_frame *);

static void halt_handler (void);
static void exit_handler (int status);
static pid_t exec_handler (const char *cmd_line);
static int wait_handler (pid_t pid);
static bool create_handler (const char *file, unsigned initial_size);
static int remove_handler (const char *file);
static int open_handler (const char  *file);
static int filesize_handler (int fd);
static int read_handler (int fd, void *buffer, unsigned size);
static int write_handler (int fd, const void *buffer, unsigned size);
static void seek_handler (int fd, unsigned position);
static unsigned tell_handler (int fd);
static void close_handler (int fd);
static bool addr_is_valid (const void *addr);

/* Lee drove here */
struct semaphore sema;    // For when we need mutual exclusion

void
syscall_init (void) 
{
  intr_register_int (0x30, 3, INTR_ON, syscall_handler, "syscall");
  sema_init(&sema, 1);
}

static void
syscall_handler (struct intr_frame *f) 
{

  /* Lee drove here */
  int *sys_num = (int *)f->esp;

  if(!addr_is_valid((const void *)f->esp))
    exit_handler(-1);

  /* Lee drove here */
  switch (*sys_num) {
    case SYS_HALT:
      halt_handler();
      break;
    case SYS_EXIT:
      exit_handler((int)*sys_num + 1);
      break;
    case SYS_EXEC:
      f->eax = exec_handler((char *)sys_num + 1);
      break;
    case SYS_WAIT:
      f->eax = wait_handler((pid_t)sys_num + 1);
      break;
    case SYS_CREATE:
      f->eax = create_handler(*((char *)sys_num + 1), (unsigned)*(sys_num + 2));
      break;
    case SYS_REMOVE:
      f->eax = remove_handler(*((char *)sys_num + 1));
      break;
    case SYS_OPEN:
      f->eax = open_handler(*((char *)sys_num + 1));
      break;
    case SYS_FILESIZE:
      f->eax = filesize_handler(*(sys_num + 1));
      break;
    case SYS_READ:
      f->eax = read_handler(*(sys_num + 1),
                             (void *)(sys_num + 2), (unsigned)*(sys_num + 3));
      break;
    case SYS_WRITE:
      f->eax = write_handler(*(sys_num + 1),
                             (void *)(sys_num + 2), (unsigned)*(sys_num + 3));
      break;
    case SYS_SEEK:
      seek_handler(*sys_num + 1, (unsigned)*(sys_num + 2));
      break;
    case SYS_TELL:
      f->eax = tell_handler(*(sys_num + 1));
      break;
    case SYS_CLOSE:
      close_handler(*(sys_num + 1));
      break;
  }
}

/* Lee drove here */
static void
halt_handler (void)
{
  shutdown_power_off();
}

/* Lee drove here */
static void
exit_handler (int status)
{
	// put status in thread struct
  struct thread *cur = thread_current ();
  cur->exit_status = status;
  printf("%s: exit(%d)\n", thread_current()->name, status);
	thread_exit ();
}

/* Lee drove here */
static pid_t 
exec_handler (const char *cmd_line)
{
  return process_execute(cmd_line);
}

/* Lee drove here */
static int 
wait_handler (pid_t pid)
{
  return process_wait (pid);
}

/* Lee drove here */
static bool 
create_handler (const char *file, unsigned initial_size)
{
	return filesys_create (file, initial_size);
}

/* Lee drove here */
static int 
remove_handler (const char *file)
{
  /*
  return filesys_remove(file);
  */
  return 1;
}

static int
open_handler (const char *file)
{
  /* Lee drove here */
  /*
	int fd;
	static struct *opened_file = filesys_open (file);

	if (opened_file == NULL)
		fd = -1;
	else
		// set fd to file descriptor. We need to figure that one out
*/
  return 1;
}

static int 
filesize_handler (int fd)
{
	// use off_t file_length()???
  return 1;
}

static int 
read_handler (int fd, void *buffer, unsigned size)
{
  return 1;
}

static int 
write_handler (int fd, const void *buffer, unsigned size)
{
  return 1;
}

/* Lee drove here */
static void 
seek_handler (int fd, unsigned position)
{
  /*
  return file_seek(fd, position);
  */
}

/* Lee drove here */
static unsigned 
tell_handler (int fd)
{
  /*
  return file_tell(fd);
  */
  return 1;
}

/* Lee drove here */
static void 
close_handler (int fd)
{
  /*
  file_close(file); //close file
  list_remove(); //remove opened files
  */

}

/* Lee drove here */
static bool
addr_is_valid (const void *addr) 
{
  // May need to add more checks
  if (addr >= PHYS_BASE || !is_user_vaddr (addr) || addr == NULL)
    return false;
  else
    return true;
}