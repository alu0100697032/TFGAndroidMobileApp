def login_by_token(request):
    ret = None
    auth_exception = None
    if request.method == 'POST':
        form = FacebookConnectForm(request.POST)
        if form.is_valid():
            try:
                provider = providers.registry.by_id(FacebookProvider.id)
                login_options = provider.get_fb_login_options(request)
                app = providers.registry.by_id(FacebookProvider.id) \
                    .get_app(request)
                access_token = form.cleaned_data['access_token']
                if login_options.get('auth_type') == 'reauthenticate':
                    info = requests.get(
                        GRAPH_API_URL + '/oauth/access_token_info',
                        params={'client_id': app.client_id,
                                'access_token': access_token}).json()
                    nonce = provider.get_nonce(request, pop=True)
                    ok = nonce and nonce == info.get('auth_nonce')
                else:
                    ok = True
                if ok:
                    token = SocialToken(app=app,
                                        token=access_token)
                    login = fb_complete_login(request, app, token)
                    login.token = token
                    login.state = SocialLogin.state_from_request(request)
                    ret = complete_social_login(request, login)
            except requests.RequestException as e:
                logger.exception('Error accessing FB user profile')
                auth_exception = e
    if not ret:
        ret = render_authentication_error(request,
                                          FacebookProvider.id,
                                          exception=auth_exception)
    return ret