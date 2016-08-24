from flask import Flask
from flask import request

app = Flask (__name__)

@app.route ('/')
def index ():
    return 'MeerkatWebClient test webservice'


@app.route ('/holecards', methods=['POST'])
def hole_cards ():
    print(request.data)
    print(request.mimetype)
    return 'HoleCards'


@app.route ('/getaction', methods=['GET'])
def getaction ():
    return '<?xml version="1.0" encoding="UTF-8"?><action>FOLD</action>'


@app.route ('/action', methods=['POST'])
def action_event ():
    print(request.data)
    print(request.mimetype)
    return 'ActionEvent'


@app.route ('/gamestate', methods=['POST'])
def gamestate ():
    print(request.data)
    print(request.mimetype)
    return 'GameState'


@app.route ('/showdown', methods=['POST'])
def showdown_event ():
    print(request.data)
    print(request.mimetype)
    return 'ShowdownEvent'


@app.route ('/stage', methods=['POST'])
def stage_event ():
    print(request.data)
    print(request.mimetype)
    return 'StageEvent'


@app.route ('/win', methods=['POST'])
def win_event ():
    print(request.data)
    print(request.mimetype)
    return 'WinEvent'


app.run ()
