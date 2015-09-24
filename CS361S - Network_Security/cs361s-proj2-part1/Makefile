# tools
CC := gcc
RM := rm -f
TAR := tar -zcf
ECHO := echo

# flags
CFLAGS := -ggdb
LDFLAGS :=
LDLIBS :=

# sources
sources := sploit1.c sploit2.c sploit3.c sploit4.c sploit5.c
targets := $(sources:.c=)

# gmake magic
.PHONY: default all clean

# targets
default: all
all: $(targets)

clean:
	$(RM) $(targets) $(sources:.c=.o)

# dependencies
$(sources:.c=.o): shellcode.h

# turnin files
files-part1 := Makefile shellcode.h SUBMISSION-PART1.txt README-PART1.txt sploit1.txt sploit2.txt
files-part2 := Makefile shellcode.h SUBMISSION-PART2.txt README-PART2.txt sploit3.txt sploit4.txt sploit5.txt
tarball-part1-filename := cs361s-proj2-part1.tgz
tarball-part2-filename := cs361s-proj2-part2.tgz

tarball-part1:
	$(RM) $(tarball-part1-filename)
	$(TAR) $(tarball-part1-filename) $(sources) $(files-part1)
	@echo "Part 1 tarball creation complete. Please submit" $(tarball-part1-filename) "on Canvas."

tarball-part2:
	$(RM) $(tarball-part2-filename)
	$(TAR) $(tarball-part2-filename) $(sources) $(files-part2)
	@echo "Part 2 tarball creation complete. Please submit" $(tarball-part2-filename) "on Canvas."
