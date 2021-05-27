package net.jeremycasey.homemonitor.weather

import org.junit.Test

import org.junit.Assert.*


class MapWeatherForescastXmlTest {
  @Test
  fun maps_the_xml_to_an_array() {
    val xmlMock =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
          "<product version=\"1.7\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"http://www.bom.gov.au/schema/v1.7/product.xsd\">\n" +
          "    <amoc>\n" +
          "        <source>\n" +
          "            <sender>Australian Government Bureau of Meteorology</sender>\n" +
          "            <region>Victoria</region>\n" +
          "            <office>VICRO</office>\n" +
          "            <copyright>http://www.bom.gov.au/other/copyright.shtml</copyright>\n" +
          "            <disclaimer>http://www.bom.gov.au/other/disclaimer.shtml</disclaimer>\n" +
          "        </source>\n" +
          "        <identifier>IDV10450</identifier>\n" +
          "        <issue-time-utc>2021-05-26T19:05:00Z</issue-time-utc>\n" +
          "        <issue-time-local tz=\"EST\">2021-05-27T05:05:00+10:00</issue-time-local>\n" +
          "        <sent-time>2021-05-26T19:05:07Z</sent-time>\n" +
          "        <expiry-time>2021-05-27T19:05:00Z</expiry-time>\n" +
          "        <validity-bgn-time-local tz=\"EST\">2021-05-27T00:00:00+10:00</validity-bgn-time-local>\n" +
          "        <validity-end-time-local tz=\"EST\">2021-06-02T23:59:59+10:00</validity-end-time-local>\n" +
          "        <next-routine-issue-time-utc>2021-05-27T06:20:00Z</next-routine-issue-time-utc>\n" +
          "        <next-routine-issue-time-local tz=\"EST\">2021-05-27T16:20:00+10:00</next-routine-issue-time-local>\n" +
          "        <status>O</status>\n" +
          "        <service>WSP</service>\n" +
          "        <sub-service>FCT</sub-service>\n" +
          "        <product-type>F</product-type>\n" +
          "        <phase>NEW</phase>\n" +
          "    </amoc>\n" +
          "    <forecast>\n" +
          "        <area aac=\"VIC_FA001\" description=\"Victoria\" type=\"region\">\n" +
          "            <forecast-period start-time-local=\"2021-05-27T05:05:08+10:00\" end-time-local=\"2021-05-27T05:05:08+10:00\" start-time-utc=\"2021-05-26T19:05:08Z\" end-time-utc=\"2021-05-26T19:05:08Z\">\n" +
          "                <text type=\"warning_summary_footer\">For latest warnings go to www.bom.gov.au, subscribe to RSS feeds, call 1300 659 210* or listen for warnings on relevant TV and radio broadcasts.</text>\n" +
          "                <text type=\"product_footer\">* Calls to 1300 numbers cost around 27.5c incl. GST, higher from mobiles or public phones.</text>\n" +
          "            </forecast-period>\n" +
          "        </area>\n" +
          "        <area aac=\"VIC_ME001\" description=\"Melbourne\" type=\"metropolitan\" parent-aac=\"VIC_FA001\">\n" +
          "            <forecast-period index=\"0\" start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00Z\" end-time-utc=\"2021-05-27T14:00:00Z\">\n" +
          "                <text type=\"forecast\">Cloudy. Very high (90%) chance of showers, most likely from late this morning. Winds westerly 20 to 30 km/h turning southwesterly 15 to 25 km/h in the late afternoon.</text>\n" +
          "                <text type=\"uv_alert\">Sun protection not recommended, UV Index predicted to reach 2 [Low]</text>\n" +
          "            </forecast-period>\n" +
          "            <forecast-period index=\"1\" start-time-local=\"2021-05-28T00:00:00+10:00\" end-time-local=\"2021-05-29T00:00:00+10:00\" start-time-utc=\"2021-05-27T14:00:00Z\" end-time-utc=\"2021-05-28T14:00:00Z\">\n" +
          "                <text type=\"forecast\">Cloudy. High (80%) chance of showers in the northeast suburbs, medium (40%) chance elsewhere. Winds southwesterly 15 to 25 km/h tending southerly 20 to 30 km/h in the morning then becoming light in the evening.</text>\n" +
          "            </forecast-period>\n" +
          "        </area>\n" +
          "        <area aac=\"VIC_PT042\" description=\"Melbourne\" type=\"location\" parent-aac=\"VIC_ME001\">\n" +
          "            <forecast-period index=\"0\" start-time-local=\"2021-05-27T05:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T19:00:00Z\" end-time-utc=\"2021-05-27T14:00:00Z\">\n" +
          "                <element type=\"forecast_icon_code\">11</element>\n" +
          "                <element type=\"precipitation_range\">2 to 4 mm</element>\n" +
          "                <element type=\"air_temperature_maximum\" units=\"Celsius\">15</element>\n" +
          "                <text type=\"precis\">Showers.</text>\n" +
          "                <text type=\"probability_of_precipitation\">90%</text>\n" +
          "            </forecast-period>\n" +
          "            <forecast-period index=\"1\" start-time-local=\"2021-05-28T00:00:00+10:00\" end-time-local=\"2021-05-29T00:00:00+10:00\" start-time-utc=\"2021-05-27T14:00:00Z\" end-time-utc=\"2021-05-28T14:00:00Z\">\n" +
          "                <element type=\"forecast_icon_code\">11</element>\n" +
          "                <element type=\"precipitation_range\">0 to 1 mm</element>\n" +
          "                <element type=\"air_temperature_minimum\" units=\"Celsius\">9</element>\n" +
          "                <element type=\"air_temperature_maximum\" units=\"Celsius\">14</element>\n" +
          "                <text type=\"precis\">Shower or two.</text>\n" +
          "                <text type=\"probability_of_precipitation\">50%</text>\n" +
          "            </forecast-period>\n" +
          "        </area>\n" +
          "        <area aac=\"VIC_PT025\" description=\"Geelong\" type=\"location\" parent-aac=\"VIC_ME001\">\n" +
          "            <forecast-period index=\"0\" start-time-local=\"2021-05-27T05:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T19:00:00Z\" end-time-utc=\"2021-05-27T14:00:00Z\">\n" +
          "                <element type=\"air_temperature_maximum\" units=\"Celsius\">16</element>\n" +
          "            </forecast-period>\n" +
          "        </area>\n" +
          "        <area aac=\"VIC_PT037\" description=\"Laverton\" type=\"location\" parent-aac=\"VIC_ME001\">\n" +
          "            <forecast-period index=\"0\" start-time-local=\"2021-05-27T05:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T19:00:00Z\" end-time-utc=\"2021-05-27T14:00:00Z\">\n" +
          "                <element type=\"air_temperature_maximum\" units=\"Celsius\">16</element>\n" +
          "            </forecast-period>\n" +
          "        </area>\n" +
          "    </forecast>\n" +
          "</product>\n"

    val result = mapWeatherForecastXml(xmlMock)

    assertEquals(
      arrayOf(
        WeatherDailyForecast(
          airTemperatureMinimum = null,
          airTemperatureMaximum = 15.0,
          endTimeLocal = "2021-05-28T00:00:00+10:00",
          endTimeUtc = "2021-05-28T00:00:00+10:00",
          forecastIconCode = "11",
          index = 0,
          startTimeLocal = "2021-05-27T05:00:00+10:00",
          startTimeUtc = "2021-05-26T19:00:00Z",
          precis = "Showers.",
          probabilityOfPrecipitation = "90%"
        ),
        WeatherDailyForecast(
          airTemperatureMinimum = 9.0,
          airTemperatureMaximum = 14.0,
          endTimeLocal = "2021-05-29T00:00:00+10:00",
          endTimeUtc = "2021-05-29T00:00:00+10:00",
          forecastIconCode = "11",
          index = 1,
          startTimeLocal = "2021-05-28T00:00:00+10:00",
          startTimeUtc = "2021-05-27T14:00:00Z",
          precis = "Shower or two.",
          probabilityOfPrecipitation = "50%"
        )
      ),
      result
    )
  }
}