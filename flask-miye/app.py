from flask import Flask

app = Flask(__name__)


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/hello/<str:name>')
def hello(name):
   return f'Hello {name}!'


if __name__ == '__main__':
    app.run()
