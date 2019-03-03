from django.contrib.auth.decorators import login_required
from django.shortcuts import render
from django.views.generic.base import TemplateView

# Create your views here.
@login_required
def reservation_base(request):
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
