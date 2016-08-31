from django.conf.urls import include, url
from django.contrib import admin
from cv import views
from django.views.decorators.csrf import csrf_exempt
from cv.views import login_by_token, google_login_by_token

urlpatterns = [
    url(r'^$', 'cv.views.index'),
    url(r'^admin/', admin.site.urls),
    url(r'^cv/', include('cv.urls')),

    url(r'^accounts/', include('allauth.urls')),

    url(r'^account/facebook/login/token/$', csrf_exempt(login_by_token)),
    url(r'^account/google/login/token/$', csrf_exempt(google_login_by_token)),
]