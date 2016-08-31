def reservar_pista(request):
    if request.method == 'POST':
        rform = ReservaForm(data=request.POST)
        if rform.is_valid():
            pista_pk = request.POST.get("pista_pk", "")
            token = request.POST.get("token", "")
            date  = request.POST.get("date", "") + ""
            pista = Pista.objects.get(pk=pista_pk)
            st = SocialToken.objects.get(token=token)
            l_date = long(date)

            seconds = l_date/1000
            date = time.strftime('%Y-%m-%d %H:%M:%SZ', time.gmtime(seconds))
            date = datetime.datetime.strptime(date, '%Y-%m-%d %H:%M:%SZ')
            if Reserva.objects.filter(usuario=st.account.user, fecha=date):
                return HttpResponse("La pista ya esta reservada este dia")
            else:
                reserva = Reserva(pista=pista, usuario=st.account.user, fecha=date, long_fecha=l_date)
                reserva.save()
                return HttpResponse("Reserva realizada con exito")
        else:
            return HttpResponseBadRequest()