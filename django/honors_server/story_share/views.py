from django.shortcuts import render
from django.http import HttpResponse
import json
from django.views.decorators.csrf import csrf_exempt
from .text_summarization import text_summarization_function
from .keyword_extraction import keyword_extraction_function
from .sentiment_extraction import sentiment_extraction_function
from django.http import JsonResponse

# Create your views here.
@csrf_exempt
def index(request):
	print("The request method is: "+ request.method)
	json_response = {}
	body_unicode = request.body.decode('utf-8')
	body = json.loads(body_unicode)
	user_id = body['user_id']
	whole_story = body['story_body']
	computed_summary = text_summarization_function(str(whole_story))
	print("summary computed")
	#print("The computed summary is: ")
	#print(computed_summary)
	#array_of_keywords = keyword_extraction_function(str(computed_summary))
	categories_and_sentiments = sentiment_extraction_function(str(computed_summary))
	#json_response['extracted_tags'] = array_of_keywords
	json_response['categories_and_sentiments'] = categories_and_sentiments
	json_response['summary'] = computed_summary

	#print("THE JSON RESPONSE IS: ")
	#print(json_response)

	#return HttpResponse(str(computed_summary))
	return JsonResponse(json_response)
