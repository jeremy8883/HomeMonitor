# Home Monitor

A private android app that I use for my home tablet, which I hang on the wall.

## Upcoming Features

* [X] Clock
* [X] Weather
* [X] Pet feeding
* [X] PTV tram timetable
* [X] Ring doorbell support (sort of)
* [X] Link with Hue lights
* [ ] Barcode scanner to add items to shopping list
* [X] Notifications when we need to bring items upstairs
* [X] Calendar
* [X] Vacuum

## Getting started

This app was really made for myself, and won't be much use to you. I also haven't worked in android in a long time, so any source code is going to be rushed and hacky. But, if you would like to pull the code down... 

Create a private configuration under [`/app/src/main/java/net/jeremycasey/homemonitor/private/config.kt`](./app/src/main/java/net/jeremycasey/homemonitor/private/config.kt)

```
// Open weather map keys
// https://openweathermap.org/api
val cityId = "2158177"
val apiKey = "abcdef12345"
val units = "metric"

// PTV api keys
// https://www.ptv.vic.gov.au/footer/data-and-reporting/datasets/ptv-timetable-api/
val ptvDevId = "123456"
val ptvApiKey = "00000000-aaaa-bbbb-cccc-dddddddddddd"
val ptvWatchedStops = listOf(
  TimetableInfo(
    routeColor = Color(0xfff58023),
    routeId = 111,
    stopId = 222,
    routeType = RouteType.tram,
    directionId = 3
  ),
  // ...
)
val ptvWatchPeriods = listOf(
  WatchPeriod(4, LocalTime(8, 0), LocalTime(9, 30)),
  // ...
)

// Message board config
val messageBoardItems = mapOf<String, MessageItemConfig>(
  "baby_supplies" to MessageItemConfig(
    code = "baby_supplies",
    type = "supplies",
    message = "Baby stock up required",
    imageResource = R.drawable.message_board_baby_supplies,
  ),
)

// Philips Hue lights
val hueHostAddress = "http://10.0.0.111/"
val hueUsername = "abcdef1234" // get from here: https://developers.meethue.com/develop/get-started-2/
val hueGroupIds = arrayOf("1", "5", "4")

// The background will match whatever colour these lights are set to
val mainGroupId = "1"

// Roomba vacumm
// Run the `getpassword` script here: https://github.com/koalazak/dorita980
val roombaBlid = "12ABC456"
val roombaPassword = ":1:1rsteincyu238903"
val roombaAddress = "192.168.0.100"
```

### Doorbell Widget

You'll need the Rapid Ring app installed.

Once you've installed this HomeMonitor app, go to the system Settings -> Apps and Notifications -> Special app access -> Notification access, and ensure that the Home Monitor app is checked.

### Message Board Widget

To post items on the message board, you'll need to send an android intent with the following info:

Action: `net.jeremycasey.homemonitor.POST_MESSAGE`
Extras:
* Key: `code`
* Value: To match the value in your private `messageBoardItems`
Package: `net.jeremycasey.homemonitor`
Target: Activity
  
I've done this by ordering some [Flic](https://flic.io/) smart buttons, which allows you to link the button to android intents.

Typical use case, I'm upstairs, I run out of nappies, so I press the smart button, and it will show a reminder on the monitor downstairs to bring them up.
