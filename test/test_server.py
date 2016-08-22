from flask import Flask
from flask import request

app = Flask (__name__)

@app.route ("/")
def index ():
    return "MeerkatWebClient test webservice"


@app.route ("/holecards", methods=["GET", "POST"])
def hole_cards ():
    if request.method == 'POST':
        print (request.data)
        return request.data
    else:
        return "HoleCards"


@app.route ("/getaction")
def getaction ():
    return "Action"


@app.route ("/action")
def action_event ():
    return "ActionEvent"


@app.route ("/gamestate")
def gamestate ():
    return "GameState"


@app.route ("/showdown")
def showdown_event ():
    return "ShowdownEvent"


@app.route ("/stage")
def stage_event ():
    return "StageEvent"


@app.route ("/win")
def win_event ():
    return "WinEvent"


app.run ()
