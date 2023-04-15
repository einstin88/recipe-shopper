from django.http import HttpResponse, JsonResponse
from django.shortcuts import render

from .service.scrapper import Scrapper
from .service.constants import SS_URLS

# Create your views here.
def index(reqeust):
    print('Check index')
    return HttpResponse('Hello')

def test(request):
    url = SS_URLS['dairies']
    worker = Scrapper(url)
    worker.fetch_html()
    products = worker.parse_for_products()
    
    return JsonResponse({'dairies': products})