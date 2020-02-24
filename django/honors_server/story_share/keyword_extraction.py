def keyword_extraction_function(summary):
	from rake_nltk import Rake
	r = Rake()
	array_of_keywords = r.extract_keywords_from_text(summary)
	#print(r.extract_keywords_from_text(summary))
	return array_of_keywords