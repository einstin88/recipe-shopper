
from django.urls import path
from . import views

urlpatterns = [
    path('health', views.health, name='health-check'),
    path('test', views.test, name='test'),
    path('parse-url', views.parse_from_url, name='parse-url'),
    path('parse-html', views.parse_from_html, name='parse-html'),
]