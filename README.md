## meerkat-webclient
The goal of this project is to aid poker bot development for the proprietary
poker trainer software Poker Academy 2.

Poker Academy bots are originally developed as plugins, and are packaged as
.jar files. This architecture makes development difficult, especially when it
comes to debugging, and packaging external libraries with your bot. The project
aims to create a simple client bot, which itself contains no application logic,
but provides a simple way to connect to an actual poker bot, written as a
web service, in any programming language, using any tool etc.
