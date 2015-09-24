FIRSTNAME : Jung;
LASTNAME : Yoon;
UTEID :  jey283;
CSACCOUNT : jungyoon;
EMAIL : jungyoon@utexas.edu;


***********************************************************************************

---------------------------------------------------  
---------> NOTES
---------------------------------------------------  
ASSUMPTIONS -->
* Lines in /etc/passwd have the following format, with fields separated by colons:
account:encrypted password data:uid:gid:GCOS-field:homedir:shell,
of which the account and encrypted password must be in the correct format and must be given.
* Uses "newshort-words.txt" as dictionary

RUNNING ---> (only 1 possible format)
* java PasswordCrack inputFile1 inputFile2 
- inputFile1 is the dictionary 
- inputFile2 is a list of passwords to crack

***********************************************************************************

---------------------------------------------------  
---------> ALGORITHM
---------------------------------------------------  

This is a breadth-first algorithm  in that it searches for the simplest passwords first. That is, it
searches for the 0-mangle then the 1-mangle, then etc. I wanted to avoid going in too deep too quickly 
and missing the easy passwords. 

1) Takes in dictionary and stores it into a List.
2) Takes in users and puts them into a map
--- key = username : value = arr[name, salt, encrypted password, status]
3) Makes List of combinations of name (users can have long names like Anne Marie Jenkins Stevenson)
4) Runs Crack tests on names only for each individual user to avoid unnecessary searches
(i.e. Tyler Jones most likely didn't use Michael Ferris's name for his password)
--- If matched, set the user's status to "Cracked" and they won't be searched through again.
5) For every dictionary word, for every user that hasn't been cracked yet, go through every mangling
of said words, looking to see if any of them match any user's password.
--- If matched, set the user's status to "Cracked" and they won't be searched through again.
--- Depending on each mangling's efficiency, we separate tasks into rounds. That is, we call the more
efficient functions first and see if we can crack passwords through them first. For example,
appending/prepending requires n^2 so we leave that for round 2 whereas ncapitalize functions in constant
time so we can call it in round 1.Note that each iteration of appending/prepending traverses through
a little under 100 characters so it's naturally less efficient. 
6) For every dictionary word, for every user that hasn't been cracked yet, go through every 2-mangling of
said words, looking to see if any of them match any user's password.
--- If matched, set the user's status to "Cracked" and they won't be searched through again.
--- Depending on each mangling's efficiency, we separate tasks into rounds. That is, we call the more
efficient functions first and see if we can crack passwords through them first. For example,
appending/prepending requires n^2, sometimes even n^3 so we leave that for a later round.
7) For every dictionary word, for every user that hasn't been cracked yet, go through a select few 3-mangling 
of said words, looking to see if any of them match any user's password.
--- I specifically chose manglings that are more efficient (i.e. ncapitalize + reverse + deleteFirstChar)
to make it as efficient as possible. 
--- This is also where I tested the 2-manglings of appending and prepending. Note that I put this process 
last because it's not a very efficient password cracking test case at all. 

***********************************************************************************

---------------------------------------------------  
---------> TIMING OUTPUT
---------------------------------------------------  

********************************
****** PROGRAM SUMMARY 1: ******
* User: [michael], Michael Ferris
     Password: michael
     Time to Crack: 40ms

* User: [abigail], Abigail Smith
     Password: liagiba
     Time to Crack: 3ms

* User: [maria], Maia Salizar
     Password: Salizar
     Time to Crack: 347ms

* User: [samantha], Samantha Connelly
     Password: amazing
     Time to Crack: 64ms

* User: [tyler], Tyler Jones
     Password: eeffoc
     Time to Crack: 176ms

* User: [jennifer], Jennifer Elmer
     Password: doorrood
     Time to Crack: 84ms

* User: [connor], Connor Larson
     Password: enoggone
     Time to Crack: 86ms

* User: [evan], Evan Whitney
     Password: Impact
     Time to Crack: 38ms

* User: [nicole], Nicole Rizzo
     Password: keyskeys
     Time to Crack: 31ms

* User: [jack], Jack Gibson
     Password: sATCHEL
     Time to Crack: 174ms

* User: [alexander], Alexander Brown
     Password: squadro
     Time to Crack: 32ms

* User: [victor], Victor Esperanza
     Password: THIRTY
     Time to Crack: 27ms

* User: [james], James Dover
     Password: icious
     Time to Crack: 22ms

* User: [benjamin], Benjamin Ewing
     Password: abort6
     Time to Crack: 38ms

* User: [morgan], Morgan Simmons
     Password: rdoctor
     Time to Crack: 1525ms

* User: [caleb], Caleb Patterson
     Password: teserP
     Time to Crack: 3195ms

* User: [rachel], Rachel Saxon
     Password: obliqu3
     Time to Crack: 10785ms

* User: [nathan], Nathan Moore
     Password: sHREWDq
     Time to Crack: 2272ms

* User: [dustin], Dustin Hart
     Password: litpeR
     Time to Crack: 8895ms

*************
* File: passwd1.txt
* Time Elapsed: 27840.0 ms
* Cracked 19 out of 20 passwords
*************
********************************


********************************
****** PROGRAM SUMMARY 2: ******
*  User: [samantha], Samantha Connelly
     Password: cOnNeLlY
     Time to Crack: 173ms

*  User: [connor], Connor Larson
     Password: nosral
     Time to Crack: 237ms

*  User: [morgan], Morgan Simmons
     Password: dIAMETER
     Time to Crack: 361ms

*  User: [tyler], Tyler Jones
     Password: eltneg
     Time to Crack: 118ms

*  User: [nicole], Nicole Rizzo
     Password: INDIGNITY
     Time to Crack: 62ms

*  User: [abigail], Abigail Smith
     Password: Saxon
     Time to Crack: 261ms

*  User: [michael], Michael Ferris
     Password: tremors
     Time to Crack: 104ms

*  User: [jack], Jack Gibson
     Password: ellows
     Time to Crack: 42ms

*  User: [caleb], Caleb Patterson
     Password: zoossooz
     Time to Crack: 1ms

*  User: [evan], Evan Whitney
     Password: ^bribed
     Time to Crack: 1348ms

*  User: [james], James Dover
     Password: enchant$
     Time to Crack: 1678ms

*  User: [maria], Maia Salizar
     Password: SpatteR
     Time to Crack: 3915ms

*  User: [alexander], Alexander Brown
     Password: Lacque
     Time to Crack: 1717ms

*  User: [benjamin], Benjamin Ewing
     Password: soozzoos
     Time to Crack: 221ms

*  User: [dustin], Dustin Hart
     Password: Swine3
     Time to Crack: 21408ms

*  User: [nathan], Nathan Moore
     Password: uPLIFTr
     Time to Crack: 1339ms

*************
* File: passwd2.txt
* Time Elapsed: 55677.0 ms
* Cracked 16 out of 20 passwords
*************
********************************

***********************************************************************************

@END