def listar_pistas(request):
    if request.method == 'POST':
        token = request.POST.get("access_token", "")
        if SocialToken.objects.filter(token=token):
            pistasXml = serializers.serialize("xml", Pista.objects.all())
        return HttpResponse(pistasXml, content_type='application/xml')
        
def listar_ya_reservadas(request):
    if request.method == 'POST':
        pista_id = request.POST.get("pista_id", "")
        reservas = Reserva.objects.filter(pista=Pista.objects.get(pk=pista_id))
        if not reservas:
            return HttpResponseNotFound("")
        else:
            lista = []
            for r in reservas:
                lista.append(r.long_fecha)
            return HttpResponse(json.dumps(lista), content_type='application/json')
