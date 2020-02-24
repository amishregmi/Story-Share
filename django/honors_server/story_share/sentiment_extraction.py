def sentiment_extraction_function(summary):
	import paralleldots
	import json
	response_from_module = {}
	
	add_to_categories = []
	paralleldots.set_api_key("PBgqFCR9T70FVQh7AJ688gugecn5doufgAiSmz3137A")
	response=paralleldots.emotion(summary)
	#print("COMPUTED EMOTIONS IS: ")
	emotion_and_scores = response['emotion']
	for one in emotion_and_scores.keys():
		if emotion_and_scores[one] > 0.2:
			add_to_categories.append(one)

	response_from_module['categories'] = add_to_categories
	response_from_module['computed_sentiments'] = response
	
	return response_from_module
	#{'categories':['Angry','Fear', 'Sad'],
	#'computed_sentiments': {'emotion': {'Happy: 0.15', 'Angry': 0.something}}
	#}
