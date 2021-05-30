package net.jeremycasey.homemonitor.api.bom

import net.jeremycasey.homemonitor.utils.fetchFromFtp
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

suspend fun fetchWeatherSummary(): WeatherSummary {
//  val xml = fetchFromFtp("ftp.bom.gov.au", "/anon/gen/fwo/IDV60920.xml")
  val xml = ""
  println("fetched")
  return mapWeatherSummary(xml)
}

suspend fun fetchWeatherForecast(): List<WeatherDailyForecast> {
//  val xml = fetchFromFtp("ftp.bom.gov.au", "/anon/gen/fwo/IDV10753.xml")
  return mapWeatherForecastXml(xml)
}