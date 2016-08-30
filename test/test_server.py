# This is an example server. It is also used to test the client bot.

from flask import Flask
from flask import request
from flask import Response

app = Flask(__name__)

@app.route('/', methods=['GET'])
def index():
    return 'MeerkatWebClient test webservice'


@app.route('/holecards', methods=['POST'])
def hole_cards():
    print(request.data)
    return 'HoleCards'


@app.route('/action', methods=['GET', 'POST'])
def action():
    if request.method == 'GET':
        xml = '<?xml version="1.0" encoding="UTF-8"?>\
            <action><type>fold</type><amount>0</amount></action>'
        return Response(xml, mimetype='text/xml');
    else:
        print(request.data)
        return 'Action'


@app.route('/newgame', methods=['POST'])
def newgame():
    print(request.data)
    print(request.mimetype)
    return 'NewGame'


@app.route('/showdown', methods=['POST'])
def showdown_event():
    print(request.data)
    return 'Showdown'


@app.route('/board', methods=['POST'])
def stage_event():
    print(request.data)
    return 'Board'


@app.route('/gameover', methods=['POST'])
def win_event():
    print(request.data)
    return 'GameOver'


app.run()
