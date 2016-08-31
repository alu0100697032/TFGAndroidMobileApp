def reservas_usuario(request):
    if request.method == 'POST':
        token = request.POST.get("access_token", "")
        if SocialToken.objects.filter(token=token):
            mis_reservas = serializers.serialize("xml", Reserva.objects.filter(usuario=SocialToken.objects.get(token=token).account.user), use_natural_foreign_keys=True)
            return HttpResponse(mis_reservas, content_type='application/xml')