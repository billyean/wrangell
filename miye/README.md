## miYe - Massage Reservation System

## Application Preparation

### Start you local environment

```shell
# Create a virtual environment and activate it
$ python3 -m venv ./venv
$ source venv/bin/activate
$ pip install -r requirements.txt
```

### Prepare your schema and play with it

* Step create your schema
```shell
$ python manage.py makemigrations
$ python manage.py migrate
```

* Play with your data
```shell
$ python manage.py shell_plus --notebook
# This will open a jupyter notebook and you can run it
```

### Create a super user for your application
```shell
$ python manage.py 

```

### Run dev server

```shell
./manage.py runserver createsuperuser
```

### Run production server

> Note: [Gunicorn](https://gunicorn.org) is a Python WSGI HTTP Server, it works as a HTTP gateway for DJango application.

```shell
gunicorn miye.wsgi:application --bind 0.0.0.0:8000 --workers 3
```



## Page Overview

### Login/Logout

### Service administration

### Customer administration

### Reservation


