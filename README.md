# Home Monitor

A private android app that I use for my home tablet, which I hang on the wall.

## Upcoming Features

* [X] Weather
* [ ] Pet feeding 
* [ ] PTV tram timetable
* [ ] Ring doorbell support (sort of)
* [ ] Link with Hue lights
* [ ] Barcode scanner to add items to shopping list
* [ ] Notifications when we need to bring items upstairs

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
```

You'll need the Rapid Ring app installed.

Once you've installed this HomeMonitor app, go to the system Settings -> Apps and Notifications -> Special app access -> Notification access, and ensure that the Home Monitor app is checked.
