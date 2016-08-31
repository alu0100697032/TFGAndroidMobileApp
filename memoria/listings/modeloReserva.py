class Reserva(models.Model):
    pista = models.ForeignKey(Pista, editable=False)
    usuario = models.ForeignKey(User, editable=False)
    fecha = models.DateTimeField()

    class Meta:
        unique_together = (("pista", "fecha"),)