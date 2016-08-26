from flask import Flask
from flask import request
from flask import Response

app = Flask (__name__)

@app.route ('/')
def index ():
    return 'MeerkatWebClient test webservice'


@app.route ('/holecards', methods=['POST'])
def hole_cards ():
    print(request.data)
    print(request.mimetype)
    return 'HoleCards'


#amount element is necessary only for raise
@app.route ('/getaction', methods=['GET'])
def getaction ():
    xml = '<?xml version="1.0" encoding="UTF-8"?>\
        <action><type>fold</type><amount>0</amount></action>'
    return Response(xml, mimetype='text/xml');


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
