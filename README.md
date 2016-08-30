## meerkat-webclient

### Overview

The goal of this project is to aid poker bot development for the proprietary
poker trainer software Poker Academy 2.5.

Poker Academy bots are developed as plugins, and are packaged as .jar files.
This architecture makes development difficult, especially when it comes to
debugging, and packaging external libraries with your bot.

The project aims to create a simple client bot, which itself contains no
application logic, but provides a simple way to connect to an actual poker bot,
written as a web service, using your favourite tools.


### Bot usage

As with all Poker Academy bots, you must copy the compiled .jar file, and the .pd file
into the /data/bots folder found in the game's directory.

You must create a new player using this engine in the game menu.

You can setup the server address in the bot's settings.


### Server API

In order to use the dummy bot, your web service must implement the required
API. A sample webserver can be found in the test folder.

Sending is performed with POST requests.


#### Required URLs:

* `<server path>/holecards`: The player's seat and hole cards are sent here

* `<server path>/action`: Actions made at the table are sent here, and a GET
  request should return the bot's next action

* `<server path>/newgame`: Initial state is sent here, including player names,
  seats, and stack sizes, plus button position

* `<server path>/board`: Board cards are sent here

* `<server path>/showdown`: Shown player cards are sent here with seat number.

* `<server path>/gameover`: Winning players with won amounts are sent here. More
  winners exist when split pot, side pot etc.

#### Sent data

Data is sent in XML. Example data:

Hole cards:
```
<holecards>
    <cards>
        <card>4s</card>
        <card>3h</card>
    </cards>
    <seat>4</seat>
</holecards>
```

Action:
```
    <action>
        <seat>8</seat>
        <type>bet</type>
        <amount>2.0</amount>
    </action>
```

New game:
```
    <newgame>
        <players>
            <player>
                <name>Crusoe</name>
                <stack>186.0</stack>
                <seat>1</seat>
            </player>
            <player>
                <name>Ogo Pogo</name>
                <stack>199.0</stack>
                <seat>2</seat>
            </player>
        </players>
        <buttonseat>2</buttonseat>
    </newgame>
```

Showdown:
```
    <showdown>
        <cards>
            <card>4s</card>
            <card>3h</card>
            <seat>4</seat>
        </cards>
        <cards>
            <card>9c</card>
            <card>As</card>
            <seat>3</seat>
        </cards>
    </showdown>
```

Board :
```
    <board>
        <card>7s</card>
        <card>4h</card>
        <card>2d</card>
        <card>Qd</card>
    </board>
```

Game over:
```
    <gameover>
        <winning>
            <seat>3</seat>
            <amount>2.0</amount>
        </winning>
        <winning>
            <seat>4</seat>
            <amount>2.0</amount>
        </winning>
    </gameover>
```

### Implementation

Poker Academy 2.5 is packaged with Java JRE version 1.5, so some newer
features (like JAXP) couldn't be used. The example server uses Python, plus
Flask as a web framework.

### Testing

There is no fully automated test unfortunately, but you can run the JUnit tests
while test_server.py is running. It will print outputs for the tested functions.
