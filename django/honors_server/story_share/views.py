from django.shortcuts import render
from django.http import HttpResponse
import json
from django.views.decorators.csrf import csrf_exempt
from .text_summarization import text_summarization_function

# Create your views here.
@csrf_exempt
def index(request):
	print("The request method is: "+ request.method)
	body_unicode = request.body.decode('utf-8')
	body = json.loads(body_unicode)
	user_id = body['user_id']
	whole_story = body['story_body']
	computed_summary = text_summarization_function(str(whole_story))
	print("The computed story is: ")
	print(computed_summary)

	return HttpResponse(str(computed_summary))
