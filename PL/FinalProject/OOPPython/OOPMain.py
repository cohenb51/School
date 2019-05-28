import sys
from DocumentProcessorBuilder import DocumentProcessorBuilder
from Process import *

class ProcessInput(object):
	def __init__(self, args): 
		self.args = args
		self.isWc = False
		self.builder = DocumentProcessorBuilder()
		self.fileText = None
		self.parseArgs()
		self.ret = self.doTask() 
		self.print(self.ret)
	def setFile(self, fileName):
		self.fileText = list(open(fileName))
		self.fileText = ''.join(self.fileText)
		self.builder.setText(self.fileText)

	def setwc(self):
		self.isWc = True
		nonAlphabetic = removeNonAlphabet()
		countWords =  CountWords()
		lower = Lower()
		findWords = FindWords()

		self.builder.setLower(lower)
		self.builder.setRemoveNonAlphabet(nonAlphabetic)
		self.builder.setFindWords(findWords)
		self.builder.setCountWords(countWords)

	def print(self,ret):
		if self.isWc:
		 for i in ret:
		 	print (i, ret[i])
		 return
		else:
			print(self.ret)

	def parseArgs(self):
		for i in range(len(self.args)):
			if self.args[i] == "grep":
				self.builder.setFilterLines(FilterLines(self.args[i+1]))
			if ".txt" in self.args[i]:
				self.setFile(self.args[i])
			if self.args[i] == "wc":
				self.setwc()
	def doTask(self):
		documentProcessor = self.builder.build()
		return documentProcessor.process()



def main():
	pi = ProcessInput(sys.argv)



if __name__ == "__main__":main()			
			

