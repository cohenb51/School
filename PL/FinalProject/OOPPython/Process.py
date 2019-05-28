#!/usr/bin/python
import sys
class Lower:

  def charToLower(self, c):
  		if ord(c)  <= 90 and ord(c) >= 65: 
  			return chr(ord(c) + 32);
  		return c;

  def process(self, string): 
  	ret = []
  	for char in string:
  		ret.append(self.charToLower(char))
  	return ''.join(ret)

class removeNonAlphabet:
	def process(self, stringList):
		newstr = []
		newList = []
		for word in stringList:
			newstr = []
			for char in word:
				if((ord(char) >= 65 and ord(char) <= 122) or char.isdigit()):
					newstr.append(char)
					continue
			string = ''.join(newstr);
			if string:
				newList.append(''.join(newstr));
		return newList

class FilterLines:
	def __init__(self, string):
		self.searchString = string

	def restOfWordMatches(self, string, offset):
		searchCounter = 0
		for i in range(len(self.searchString)):
			if string[offset + i] != self.searchString[i]:
				return False
		return True;

	def process(self, string):
	  newstr = []
	  lines = self.split(string)
	  start = 0
	  for i in range(len(lines)):
	  	charNumber = 0
	  	for char in lines[i]:
	  		if(char == self.searchString[0]):
	  			if(self.restOfWordMatches(lines[i],charNumber )):
	  				newstr.append(lines[i])
	  				newstr.append(' ')
	  				break;
	  		charNumber+=1
	  return ''.join(newstr);

	def split(self, string):
		ret = []
		start = 0
		for i in range(len(string)):
			if string[i] == '\r':
				if string[i] == '\n':
					ret.append(string[start:i] + ' ')
					start = i
			if string[i] == '\n':
				ret.append(string[start:i] + ' ')
				ret.append(' ')
				start = i

		ret.append(' ' + string[start:])
		return ret




class FindWords:
	def process(self,string):
		temp = self.split(string)
		return temp


	def split(self, string):
		ret = []
		start = 0
		for i in range(len(string)):
			if string[i] == ' ' or string[i] == '\n' or (string[i] == '\r' and string[i+1] == '\n'):
				ret.append(string[start:i])
				start = i

		ret.append(string[start:])
		return ret

class CountWords:
	def process(self, words):
		map = {}
		for i in words:
			if i in map:
				map[i] = map[i] + 1
			else:
				map[i] = 1;
		return map



  

