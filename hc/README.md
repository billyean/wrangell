# Create a Django Application

Assume your application name is hc

```shell
$ django-admin startproject hc 
```

# Start apps(create a module)
 
Assume you want to create a new module called blog
 
```shell
$ ./manage.py startapp blog
```

add your module into your application setting(eg: [hc/settings.py](hc/settings.py))

```python3

INSTALLED_APPS = [
    ...,
    # My app
    'blog.apps.BlogConfig',
]
```

# Models

You can edit your [models.py](blog/models.py) to add your database model. Once you've done creation of model. You need create migration and migrate your model.

```python
$ ./manage.py makemigrations
$
$ ./manage.py migrate
```

You can use shell_plus to start your django application
```shell
$ ./manage.py shell_plus
```

You also can use ```shell_plus --notebook``` to start your django application with jupyter notebook
```shell
$ ./manage.py shell_plus --notebook 
```