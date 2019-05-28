#!/usr/bin/python
import sys
import functools


def lower(string):
	return ''.join(map(charToLower, string)) ## strings are immutable so doesn't change original

def charToLower(c):
	if ord(c)  <= 90 and ord(c) >= 65: #again view it as evaluating a functional. Not store 2 seperate variables and then check. Deep down declarative languages anyways at lower levels of abstraction are imperative
		return chr(ord(c) + 32);
	return c;


def charInAlphabet(char):
	if(ord(char) >= 65 and ord(char) <= 122) or char.isdigit():
			return char
	return ""	

def removeNonAlphabetFromString(string):
	return ''.join(filter(charInAlphabet, string))

def removeEmptyWords(string):
	return list(filter(lambda x: len(x.strip()) > 0, string))

def removeNonAlphabet(string):
	return removeEmptyWords(list(map(lambda x: removeNonAlphabetFromString(x), string))) # char in alphabet is the function ^

def printIt(args, str):
	if not isWc(args):
		print(str)
	else:
		printIndexed(0, list(str), str)


def printIndexed(i, keys, map):
	if i>len(keys) -1:
		return
	print(keys[i] + " " + str(map.get(keys[i])))
	return printIndexed(1+i, keys, map)



def FilterLines(string, searhString):
	return "\n".join(list(filter(lambda x: searhString in x, string.split("\n"))))

def filterWords(string):
	return string.replace("\n", " ").split(" ")


def countWords(words):
	return dict(zip(words, map(lambda x: functools.reduce(lambda y,z: (y+match(x, z)), words,0), words)))

def match(str1, str2):
	if str1 == str2:return 1;
	else:return 0;

def getFileName(args):
	if args[1] == "grep":
		return args[3]
	return args[2]

def isWc(args):
	return "wc" in args 
def isGrep(args):
	return "grep" in args

# 1. Filter-Lines: filter out lines that don’t meet grep search criteria 
# 2. Convert-Case: put everything in lowercase 
# 3. Find-Words: split the text into individual words 
#  4. Non-Alphabetic-Filter: strip out all non-alphabetic characters.
#   Eliminate any “words” that are just white space.
#    5. Count-Words: produce a set of unique words and
#    the number of times they each appear 

def doWcAndGrep(string, searchString):
	return (countWords(removeNonAlphabet(filterWords(lower((FilterLines(''.join(list(string)), searchString)))))))

def doWc(string):
	return (countWords(removeNonAlphabet(filterWords(lower(''.join(list(string)))))))
def doGrep(string, searchString):
	return (FilterLines(''.join(list(string)), searchString))

def main():#Again I thought you wanted them all as freestanding functions when I started
	if isWc(sys.argv) and isGrep(sys.argv):
		printIt(sys.argv, doWcAndGrep(open(getFileName(sys.argv)), sys.argv[2]))

	if isWc(sys.argv) and not isGrep(sys.argv):
		printIt(sys.argv, doWc(open(getFileName(sys.argv))))
	if not isWc(sys.argv) and isGrep(sys.argv):
		printIt(sys.argv, doGrep(open(getFileName(sys.argv)), sys.argv[2]))

if __name__ == "__main__":main()