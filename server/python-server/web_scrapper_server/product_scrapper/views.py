from http import HTTPStatus

from django.core.files.uploadedfile import UploadedFile
from django.http import HttpRequest, HttpResponse, JsonResponse
from django.views.decorators.http import require_GET, require_POST
from django.views.decorators.csrf import csrf_exempt

from .service.scrapper import Scrapper
from .service.constants import SS_URLS
from .utils import create_json_response


@require_GET
def health(reqeust):
    print('>>> Health check is requested...')
    return HttpResponse('Server is up.')


def test(request):
    category = 'dairies'
    url = SS_URLS[category]
    worker = Scrapper(url)
    worker.fetch_html()
    products = worker.parse_for_products()

    return JsonResponse({category: products})


@require_GET
def parse_from_url(request: HttpRequest):
    '''
    Endpoint 1: returns a json array of products based on given category
    '''
    category = request.GET['category']
    print('>>> Scraping for products in category: ' + category)

    # Validate category
    if category not in SS_URLS.keys():
        return JsonResponse({"error": f"'{category}' is not a valid category"},
                            status=HTTPStatus.NOT_FOUND)

    # Parse for products
    url = SS_URLS[category]
    scrapper = Scrapper(url)
    scrapper.fetch_html()

    products = scrapper.parse_for_products()
    print(f'>>> Found {len(products)} products.')

    return JsonResponse(create_json_response(category, products))


@require_POST
@csrf_exempt
def parse_from_html(request: HttpRequest):
    '''
    Endpoint 2: returns a json array of products based on given html
    '''
    print('>>> Getting html source from file...')
    
    category = request.POST['category']

    file = request.FILES['file']
    print('>>> File size: ' + str(file.size))
    print('>>> File name: ' + file.name)

    scrapper = Scrapper('')
    scrapper.save_html(file.read())

    # Parse for products
    products = scrapper.parse_for_products()

    return JsonResponse(create_json_response(category, products))


def custom_404_response(request: HttpRequest, exception):
    print('>> 404 handler methods called...')
    return JsonResponse({"error": f"{request.path} is not valid url"},
                        status=HTTPStatus.NOT_FOUND)
