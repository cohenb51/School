class DocumentProcessorBuilder(object):
	"""docstring for DocumentProcessorBuilder"""
	def __init__(self):
		self.Lower = None
		self.removeNonAlphabet = None
		self.FilterLines = None
		self.FindWords = None
		self.CountWords = None
		self.text = None;
	def setLower(self,lower):
		self.Lower = lower
	def setRemoveNonAlphabet(self,removeNonAlphabet):
		self.removeNonAlphabet = removeNonAlphabet
	def setFilterLines(self,FilterLines):
		self.FilterLines = FilterLines
	def setFindWords(self,FindWords):
		self.FindWords = FindWords
	def setCountWords(self,CountWords):
		self.CountWords = CountWords
	def setText(self, text):
		self.text = text

	def build(self):
		return DocumentProcessor(self)

class DocumentProcessor(object):
	def __init__(self, Builder):
		self.lower = Builder.Lower
		self.removeNonAlphabet = Builder.removeNonAlphabet
		self.FilterLines = Builder.FilterLines
		self.FindWords = Builder.FindWords
		self.CountWords = Builder.CountWords
		self.text = Builder.text
		self.builder = [ self.FilterLines, self.lower, self.FindWords, self.removeNonAlphabet, self.CountWords]


	def process(self):
		temp = self.text
		for i in self.builder:
			if i != None:
				temp = i.process(temp)

		return temp


