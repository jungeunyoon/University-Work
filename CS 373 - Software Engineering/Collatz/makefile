FILES :=                              \
    .travis.yml                       \
    collatz-tests/jey283-RunCollatz.in   \
    collatz-tests/jey283-RunCollatz.out  \
    collatz-tests/jey283-TestCollatz.py  \
    collatz-tests/jey283-TestCollatz.out \
    Collatz.html                      \
    Collatz.log                       \
    Collatz.py                        \
    RunCollatz.py                     \
    RunCollatz.in                     \
    RunCollatz.out                    \
    TestCollatz.py                    \
    TestCollatz.out

all:

check:
	@for i in $(FILES);                                         \
    do                                                          \
        [ -e $$i ] && echo "$$i found" || echo "$$i NOT FOUND"; \
    done

clean:
	rm -f  .coverage
	rm -f  *.pyc
	rm -f  Collatz.html
	rm -f  Collatz.log
	rm -f  RunCollatz.out
	rm -f  TestCollatz.out
	rm -rf __pycache__

config:
	git config -l

test: RunCollatz.out TestCollatz.out

collatz-tests:
	git clone https://github.com/cs373-summer-2015/collatz-tests.git

Collatz.html: Collatz.py
	pydoc3 -w Collatz

Collatz.log:
	git log > Collatz.log

RunCollatz.out: RunCollatz.py
	cat RunCollatz.in
	./RunCollatz.py < RunCollatz.in > RunCollatz.out
	cat RunCollatz.out

TestCollatz.out: TestCollatz.py
	coverage3 run    --branch TestCollatz.py >  TestCollatz.out 2>&1
	coverage3 report -m                      >> TestCollatz.out
	cat TestCollatz.out

