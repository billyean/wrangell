from django.contrib.auth.decorators import login_required
from django.core.exceptions import ValidationError
from django.http import JsonResponse
from django.shortcuts import render
from django.views.generic.base import TemplateView
from reservation.models import Reservation
from administration.models import Customer
from administration.models import Service
import datetime


# Create your views here.
@login_required
def reservation(request):
    return render(request, 'reservation.html', None)


class DayHtml(TemplateView):
    template_name = "calendar/day.html"


class DayHtml(TemplateView):
    template_name = "calendar/day.html"


class EventsListHtml(TemplateView):
    template_name = "calendar/events-list.html"


class ModalHtml(TemplateView):
    template_name = "calendar/modal.html"


class MonthHtml(TemplateView):
    template_name = "calendar/month.html"


class MonthDayHtml(TemplateView):
    template_name = "calendar/month-day.html"


class WeekHtml(TemplateView):
    template_name = "calendar/week.html"


class WeekDaysHtml(TemplateView):
    template_name = "calendar/week-days.html"


class YearHtml(TemplateView):
    template_name = "calendar/year.html"


class YearMonthHtml(TemplateView):
    template_name = "calendar/year-month.html"


@login_required
def reservations(request):
    data = dict()
    try:
        if request.method == 'GET':
            valid_reservations = Reservation.objects.filter(start_datetime__gte=datetime.datetime.now())
            data['reservations'] = [dumpJson(r) for r in valid_reservations]
        else:
            customer = Customer.objects.filter(id == request.POST['customer_id'])
            reservation_service = Service.objects.filter(id == request.POST['service_id'])
            reservation_length = request.POST['reservation_length']
            reservation_date_time = datetime.datetime.strptime(request.POST['reservation_time'], '%Y-%m-%d %H:%M')
            reservation_obj = Reservation(customer=customer,
                                          reservation_date_time=reservation_date_time,
                                          reservation_length=reservation_length,
                                          reservation_service=reservation_service)
            reservation_obj.save()

            data['service'] = dumpJson(reservation_obj)
        data['ret'] = 0
    except ValidationError as e:
        data['ret'] = 1
        data['message'] = str(e)
    return JsonResponse(data)


@login_required
def new_reservation(request):
    data = dict()
    try:
        if request.method == 'POST':
            customer = Customer.objects.get(id=request.POST['customer_id'])
            reservation_service = Service.objects.get(id=request.POST['service_id'])
            reservation_length = int(request.POST['reservation_length'])
            start_datetime = datetime.datetime.strptime(request.POST['reservation_time'], '%Y-%m-%d %H:%M')
            print(reservation_length)
            end_datetime = start_datetime + datetime.timedelta(minutes=reservation_length)
            reservation_obj = Reservation(customer=customer,
                                          start_datetime=start_datetime,
                                          end_datetime=end_datetime,
                                          reservation_service=reservation_service)
            reservation_obj.save()

            # data['service'] = dumpJson(reservation_obj)
        data['ret'] = 0
    except ValidationError as e:
        data['ret'] = 1
        data['message'] = str(e)
    return JsonResponse(data)


def dumpJson(reservation_obj):
    print(reservation_obj)
    customer = reservation_obj.customer
    service = reservation_obj.reservation_service
    start_datetime = reservation_obj.start_datetime + datetime.timedelta(hours=8)
    end_datetime = reservation_obj.end_datetime + datetime.timedelta(hours=8)

    return {
            'id': reservation_obj.id,
            'customer': {
                'id': customer.id,
                'first_name': customer.first_name,
                'last_name': customer.last_name
            },
            'datetime_start': start_datetime,
            'start_date': start_datetime.date(),
            'start_time': start_datetime.time().strftime('%H:%M'),
            'end_datetime': end_datetime,
            'end_date': end_datetime.date(),
            'end_time': end_datetime.time().strftime('%H:%M'),
            'datetime_start_ms': start_datetime.strftime('%s') + '000',
            'datetime_end_ms': end_datetime.strftime('%s') + '000',
            'reservation_service': {
                'id': service.id,
                'name': service.name,
                'description': service.description,
                'time_type': service.time_type,
                'rate': service.rate
            }
    }
