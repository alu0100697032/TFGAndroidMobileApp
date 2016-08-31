def google_complete_login(request, app, token):
    provider = providers.registry.by_id(GoogleProvider.id)
    resp = requests.get('https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=' + token.token)
    resp.raise_for_status()
    extra_data = resp.json()
    extra_data["id"] = extra_data["sub"]
    del extra_data["sub"]
    login = provider.sociallogin_from_response(request, extra_data)
    return login