from django.contrib.auth.decorators import login_required
from django.shortcuts import render, get_object_or_404
from .models import (Service)


# Create your views here.
@login_required
def service_list(request):
    services = Service.objects.all()
    return render(request, 'service/list.html', {'services': services})


@login_required
def service_detail(request, service_id):
    service = get_object_or_404(Service, id = service_id)
    return render(request, 'service/detail.html', {'service': service})