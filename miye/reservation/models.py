from django.db import models
from administration.models import Customer
from administration.models import Service
from django.db.models import (
    IntegerField,
    DateTimeField
)
import datetime


# Create your models here.
class Reservation(models.Model):
    customer = models.ForeignKey(Customer, on_delete=models.CASCADE)
    start_datetime = DateTimeField()
    end_datetime = DateTimeField()
    reservation_service = models.ForeignKey(Service, on_delete=models.CASCADE)

    objects = models.Manager()

    def clean(self):
        self.objects.filter(reservation_date_time__lte=self.reservation_date_time)\
            .filter(reservation_date_time__gt=self.reservation_date_time)

