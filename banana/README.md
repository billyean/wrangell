# Create a Django project

```shell
django-admin startproject [project name]
```

# Create db

```shell
python3 manage.py migrate
```

# Startup development server

```shell
python3 manage.py runserver
```

run the server in different port.
```shell
python3 manage.py runserver http://localhost:8001
```

# Work on an application

## Create an new application
```shell
django-admin startapp [application name]
```

after that, you should add your [application name] into settings.py as INSTALLED_APPS.

```python
INSTALLED_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'blog',
]
```

## Create model and  a migration

First update [models.py](./blog/models.py) to add your models then migrate your model to database.

```shell
$ python3 manage.py makemigrations blog

# Show sql schema
$ python3 manage.py sqlmigrate blog 0001

# Apply sql schema to database
$ python3 manage.py migrate
```

## Create admin site for models

First you need register model in admin.py

```python
# Register your models here.
admin.site.register(Post)
```

Then you can create a super user
```shell
# create an superuser
$ python3  manage.py createsuperuser
Username (leave blank to use 'xxxxxxxx'): admin
Email address: tristan.yim@gmail.com
Password: 
Password (again): 
This password is too short. It must contain at least 8 characters.
This password is too common.
Bypass password validation and create user anyway? [y/N]: y
Superuser created successfully.

# Run python server again
$ python3 manage.py runserver
```

## Play the db

```shell
$ python3 manage.py shell
Python 3.7.0 (default, Aug 22 2018, 15:22:33) 
[Clang 9.1.0 (clang-902.0.39.2)] on darwin
Type "help", "copyright", "credits" or "license" for more information.
(InteractiveConsole)
>>> from django.contrib.auth.models import User
>>> from blog.models import Post
>>> user = User.objects.get(username='admin')
>>> Post.objects.create(title='Second Post',slug='second-post',body='Post in shell', author=user)
<Post: Second Post>
```