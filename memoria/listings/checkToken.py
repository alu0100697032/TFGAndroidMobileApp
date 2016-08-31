def fb_complete_login(request, app, token):
    provider = providers.registry.by_id(FacebookProvider.id)
    resp = requests.get(
        'https://graph.facebook.com/me',
        params={
            'fields': ','.join(provider.get_fields()),
            'access_token': token.token,
            'appsecret_proof': compute_appsecret_proof(app, token)
        })
    resp.raise_for_status()
    extra_data = resp.json()
    login = provider.sociallogin_from_response(request, extra_data)
    return login