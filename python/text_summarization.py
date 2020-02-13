from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize, sent_tokenize


def get_word_frequency_table(str_whole_text):

	stopWords = set(stopwords.words("english"))
	#Split sentence into words
	words = word_tokenize(str_whole_text)
	#Stemmer to remove everything but the base word
	ps = PorterStemmer()
	word_freq_table = {}

	for word in words:
		word = ps.stem(word)
		
		if word in stopWords:
			continue
		if word in word_freq_table:
			word_freq_table[word] +=1
		else:
			word_freq_table[word] = 1

	return word_freq_table


def score_sentences(str_whole_text, word_freq_table):
	sentences = sent_tokenize(str_whole_text)
	sentence_values = {}
	
	for sentence in sentences:
		numofwords_in_sentence = len(word_tokenize(sentence))
		for word in word_freq_table:
			if word in sentence:
				#Only consider the first 10 characters of a sentence to make the computing time quicker
				if sentence[:10] in sentence_values:
					sentence_values[sentence[:10]] += word_freq_table[word]
				else:
					sentence_values[sentence[:10]] = word_freq_table[word]


		sentence_values[sentence[:10]] = sentence_values[sentence[:10]] // numofwords_in_sentence

	return sentence_values


#Get average score of sentences as a threshold
def get_average_score(sentence_values):
	sumValues = 0
	for one in sentence_values:
		sumValues+= sentence_values[one]

	average = int(sumValues/ len(sentence_values))
	return average


#Select a sentence for summary if the score is greater than the average score
def gen_summary(sentences, sentence_values, threshold):
	numof_sentences = 0
	summary = ""

	for sentence in sentences:
		if sentence[:10] in sentence_values and sentence_values[sentence[:10]] > threshold:
			summary += " "+ sentence
			numof_sentences +=1

	return summary 



with open('firststory.txt', 'r') as file:
	data = file.read().replace('\n','')

word_freq_dict = get_word_frequency_table(data)

sentence_scores = score_sentences(data, word_freq_dict)

threshold_val = get_average_score(sentence_scores)

summary = gen_summary(sent_tokenize(data), sentence_scores, 1.25*threshold_val)

print(summary)

