from django.db import models
from django.db.models import (
    CharField,
    SlugField,
    DecimalField,
    DateTimeField,
    BooleanField,
    EmailField,
    AutoField
)
import json


# Create your models here.
class Service(models.Model):
    id = AutoField
    name = CharField(max_length=255, unique=True)
    slug = SlugField(max_length=255)
    time_type = CharField(max_length=255)
    rate = DecimalField(blank=False, null=False, decimal_places=2, max_digits=5)
    create_time = DateTimeField(auto_now_add=True)
    update_time = DateTimeField(auto_now=True)

    def time_list(self):
        return json.loads(self.time_type)

    def __str__(self):
        return f"name: {self.name}; slug: {self.slug}"


class ActiveManager(models.Manager):
    def get_queryset(self):
        return super(ActiveManager, self).get_queryset().filter(active=True)


class Customer(models.Model):
    MALE = 'M'
    FEMALE = 'F'
    UNKNOWN = 'F'

    GENDER = ((MALE, 'Male'), (FEMALE, 'Female'), (UNKNOWN, 'Unknown'))
    id = AutoField
    first_name = CharField(max_length=255)
    middle_name = CharField(max_length=255, null=True)
    last_name = CharField(max_length=255)
    gender = CharField(max_length=1, choices=GENDER, default=UNKNOWN)
    active = BooleanField(default=True)
    tel = CharField(max_length=13, default=True)
    email = EmailField(null=True)
    create_time = DateTimeField(auto_now_add=True)
    update_time = DateTimeField(auto_now=True)

    objects = models.Manager()
    actives = ActiveManager()

    def __str__(self):
        return f"Customer id: {self.id}, name: {self.first_name} {self.last_name}, sex: {self.gender}, {self.active}"
