
from django.urls import path
from . import views

urlpatterns = [
    path('health', views.health, name='home'),
    path('test', views.test, name='test'),
    path('parse/<str:category>', views.parse_from_url, name='parse-url'),
    path('parse-html', views.parse_from_html, name='parse-html')
]