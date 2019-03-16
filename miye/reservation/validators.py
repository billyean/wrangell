import datetime
from django.core.exceptions import ValidationError
from reservation.models import Reservation


def validate_start_time(start_time):
    validate_time('start_time', start_time)


def validate_end_time(end_time):
    validate_time('end_time', end_time)


def validate_time(name, time):
    if time < datetime.time(hour=8, minute=0, second=0) or time > datetime.time(hour=20, minute=0, second=0):
        raise ValidationError("reservation time should be between 8:00am to 8:00pm", params={name: time})


def validate_self(validating):
    left = Reservation.objects.filter(customer=validating.customer).filter(date=validating.date).filter(
        start_time__lte=validating.start_time).filter(end_time__gt=validating.start_time)
    right = Reservation.objects.filter(customer=validating.customer).filter(date=validating.date).filter(
        start_time__lt=validating.end_time).filter(end_time__gte=validating.start_time)

    if left.count() + right.count() > 0:
        raise ValidationError(f"Multiple reservation at the same time is not allowed.")


def validate_other(validating):
    limit = validating.reservation_service.limit

    check_time = validating.start_time

    while check_time < validating.end_time:
        overlap = Reservation.objects.filter(reservation_service=validating.reservation_service).filter(
            date=validating.date).filter(start_time__lte=check_time).filter(end_time__gt=check_time)
        if overlap.count() >= limit:
            raise ValidationError(f"Reservation numbers at the {check_time} is beyond limit {limit}.")
        check_time += + datetime.timedelta(minutes=30)

