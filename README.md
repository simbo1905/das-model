# das-model

ZK Spreadsheet demo using SBT and Scala.

This is opensource Java and Scala parsing a Excel file to generate a grid webpage which emulates the look, feel and logic of the Excel file. It also has some event handlers that call out to Scala logic to do calcuations to update the Excel. Basically Excel is XML and this project just uses that as a spectification to build a webpage. There is also a drill down chart lifted from a demo which isn't yet hooked up to the Excel data.

Next steps is iterate on this to create a few real models of how we might want to do funding in DAS and get feedback. 

```sh
# Run locally with jetty
sbt jetty:start
```

```sh
# Push to Heroku
# Requires you install toolbelt and set API key variable as per heroku docs and name your own app
# Also see https://github.com/earldouglas/sbt-heroku-deploy
sbt deployHeroku
```
