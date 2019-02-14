Reservation System

# Start you local environment

```shell
# Create a virtual environment and activate it
$ python3 -m venv ./venv
$ chmod +x venv/bin/activate # not sure why needs this, but it happens in my local
$ source venv/bin/activate
$ pip install -r requirements.txt
```

# Prepare your schema and play with it

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

# Run dev server

```shell
./manage.py runserver
```


# 
