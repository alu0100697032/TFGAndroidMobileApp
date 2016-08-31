def google_login_by_token(request):
    ret = None
    auth_exception = None
    if request.method == 'POST':
        form = GoogleConnectForm(request.POST)
        if form.is_valid():
            try:
                app = providers.registry.by_id(GoogleProvider.id) \
                    .get_app(request)
                id_token = form.cleaned_data['access_token']
                info = requests.get('https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=' + id_token).json()
                app = SocialApp.objects.get(pk=2)
                if info.get('aud') == app.client_id:
                    token = SocialToken(app=app,
                                        token=id_token)
                    login = google_complete_login(request, app, token)
                    login.token = token
                    login.state = SocialLogin.state_from_request(request)
                    ret = complete_social_login(request, login)
            except requests.RequestException as e:
                logger.exception('Error accessing Google user profile')
                auth_exception = e
    if not ret:
        ret = render_authentication_error(request,
                                          GoogleProvider.id,
                                          exception=auth_exception)
    return ret