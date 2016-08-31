class Pista(models.Model):

    PISTA_TYPE = (
        ('BAL', u'Baloncesto'),
        ('FUT', u'Futbol'),
        ('PAD', u'Padel'),
        ('TEN', u'Tenis'),
        ('ATL', u'Atletismo'),
    )

    nombre = models.CharField(max_length=100)

    tipoPista = models.CharField(
        verbose_name="Tipo de pista",
        max_length=3,
        choices=PISTA_TYPE,
        default='BAL'
    )
    def natural_key(self):
        return (self.nombre, self.tipoPista)
