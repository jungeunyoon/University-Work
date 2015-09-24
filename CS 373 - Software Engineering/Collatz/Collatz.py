#!/usr/bin/env python3

# ---------------------------
# projects/collatz/Collatz.py
# Copyright (C) 2015
# Glenn P. Downing
# ---------------------------

# ------------
# global cache
# ------------

cache = [0] * 1000000

# ------------
# collatz_read
# ------------

def collatz_read (s) :
    """
    read two ints
    s a string
    return a list of two ints, representing the beginning and end of a range, [i, j]
    """
    a = s.split()
    return [int(a[0]), int(a[1])]

# ------------
# collatz_eval
# ------------

def collatz_eval (i, j) :
    """
    i the beginning of the range, inclusive
    j the end       of the range, inclusive
    return the max cycle length of the range [i, j]
    """

    # pre-conditions & argument validity
    assert i > 0
    assert j > 0
    assert i < 1000000
    assert j < 1000000

    # makes sure that beginning of range is less than end
    # otherwise makes a switch    
    if j < i :
        temp = i;
        i = j;
        j = temp;

    # holds temporary maximum cycle length
    tempMax = 0;

    # iteration to find cycle length of range
    for pos in range(i, j+1) :
        # if cycle length in cache use it, otherwise call collatz_cycle_length
        if cache[pos] == 0 :
            cycle_length = collatz_cycle_length (pos)
        else :
            cycle_length = cache[pos]
        # set the current max cycle length
        if cycle_length > tempMax :
            tempMax = cycle_length

    # return-value validity
    assert tempMax > 0

    return tempMax

# ------------
# collatz_cycle_length
# ------------

def collatz_cycle_length (l) :
    """
    l the integer to find cycle length of
    return the cycle length of the l
    """
    
    # pre-conditions
    assert l > 0
    assert l < 1000000

    # starts off at count 1 to account for the last '1'
    count = 1

    # find cycle length using algorithm
    tempNum = l
    while tempNum > 1 :
        # if value not already in cache
        if tempNum > 999999 or cache[tempNum] == 0 :  
            # if even number, divide by 2 
            if tempNum % 2 == 0 :
                tempNum //= 2
                count += 1
            # if odd, use optimization (n + n >> 1) + 1
            else :
                # tempNum = (3 * tempNum) + 1
                tempNum += (tempNum >> 1) + 1
                count += 2
        # if value found in cache
        else :
            count = count + (cache[tempNum] - 1)
            tempNum = 1;

    # post-conditions
    assert count > 0
    
    # stores new cycle length in cache
    cache[l] = count
    
    return count

# -------------
# collatz_print
# -------------

def collatz_print (w, i, j, v) :
    """
    print three ints
    w a writer
    i the beginning of the range, inclusive
    j the end       of the range, inclusive
    v the max cycle length
    """
    w.write(str(i) + " " + str(j) + " " + str(v) + "\n")

# -------------
# collatz_solve
# -------------

def collatz_solve (r, w) :
    """
    r a reader
    w a writer
    """
    for s in r :
        i, j = collatz_read(s)
        v    = collatz_eval(i, j)
        collatz_print(w, i, j, v)
