from django.contrib.auth.decorators import login_required
from django.shortcuts import render, get_object_or_404
from .models import (Service)
from django.http import JsonResponse
from django.core.exceptions import ValidationError

# Create your views here.
@login_required
def service_base(request):
    return render(request, 'service/main.html', None)


@login_required
def service_list(request):
    data = dict()
    try:
        if request.method == 'GET':
            services = list(Service.objects.all().values())
            data['services'] = services
        else:
            service = Service(name=request.POST['name'],
                              time_type=request.POST['time_type'],
                              rate=request.POST['rate'])
            service.full_clean()
            service.save()

            data['service'] = {
                    'id': service.id,
                    'name': service.name,
                    'description': service.description,
                    'time_type': service.time_type,
                    'rate': service.rate
                }
        data['ret'] = 0
    except ValidationError as e:
        data['ret'] = 1
        data['message'] = str(e)
    return JsonResponse(data)


@login_required
def service_detail(request, service_id):
    data = dict()
    try:
        service = Service.objects.get(id=service_id)

        if request.method == 'POST':
            if request.POST['name'] is not None:
                service.name = request.POST['name']
            if request.POST['description'] is not None:
                service.description = request.POST['description']
            if request.POST['time_type'] is not None:
                service.time_type = request.POST['time_type']
            if request.POST['rate'] is not None:
                service.rate = request.POST['rate']
            service.full_clean()
            service.save()
        elif request.method == 'DELETE':
            service.delete()

        data['ret'] = 0
        data['service'] = {
                'id': service.id,
                'name': service.name,
                'description': service.description,
                'time_type': service.time_type,
                'rate': service.rate
            }
        return JsonResponse(data)
    except ValidationError as e:
        data['ret'] = 1
        data['message'] = str(e)
    return JsonResponse(data)
