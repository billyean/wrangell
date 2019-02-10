from django.shortcuts import render, get_object_or_404
from .models import (Service, Customer)


# Create your views here.
def service_list(request):
    services = Service.objects.all()
    return render(request, 'administration/service/list.html', {'services': services})


def service_detail(request, service_id):
    service = get_object_or_404(Service, id = service_id)
    return render(request, 'administration/service/detail.html', {'service': service})