package net.jeremycasey.homemonitor.api.bom

import net.jeremycasey.homemonitor.utils.printObject
import org.junit.Test

import org.junit.Assert.*


class MapWeatherSummaryXmlTest {
  @Test
  fun maps_the_xml_to_an_array() {
    val xmlMock =
      "<?xml version=\"1.0\"?>\n" +
      "<product xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"v1.7.1\" xsi:noNamespaceSchemaLocation=\"http://www.bom.gov.au/schema/v1.7/product.xsd\">\n" +
      "  <amoc>\n" +
      "    <source>\n" +
      "      <sender>Australian Government Bureau of Meteorology</sender>\n" +
      "      <region>Victoria</region>\n" +
      "      <office>VICRO</office>\n" +
      "      <copyright>http://www.bom.gov.au/other/copyright.shtml</copyright>\n" +
      "      <disclaimer>http://www.bom.gov.au/other/disclaimer.shtml</disclaimer>\n" +
      "    </source>\n" +
      "    <identifier>IDV60920</identifier>\n" +
      "    <issue-time-utc>2021-05-27T12:31:03+00:00</issue-time-utc>\n" +
      "    <issue-time-local tz=\"EST\">2021-05-27T22:31:03+10:00</issue-time-local>\n" +
      "    <sent-time>2021-05-27T12:33:23+00:00</sent-time>\n" +
      "    <status>O</status>\n" +
      "    <service>WSP</service>\n" +
      "    <product-type>O</product-type>\n" +
      "    <phase>NEW</phase>\n" +
      "  </amoc>\n" +
      "  <observations>\n" +
      "    <station wmo-id=\"95936\" bom-id=\"086338\" tz=\"Australia/Melbourne\" stn-name=\"MELBOURNE (OLYMPIC PARK)\" stn-height=\"7.53\" type=\"AWS\" lat=\"-37.8255\" lon=\"144.9816\" forecast-district-id=\"VIC_PW007\" description=\"Melbourne (Olympic Park)\">\n" +
      "      <period index=\"0\" time-utc=\"2021-05-27T12:30:00+00:00\" time-local=\"2021-05-27T22:30:00+10:00\" wind-src=\"metar_10\">\n" +
      "        <level index=\"0\" type=\"surface\">\n" +
      "          <element units=\"Celsius\" type=\"apparent_temp\">10.4</element>\n" +
      "          <element units=\"Celsius\" type=\"delta_t\">0.4</element>\n" +
      "          <element units=\"km/h\" type=\"gust_kmh\">15</element>\n" +
      "          <element units=\"knots\" type=\"wind_gust_spd\">8</element>\n" +
      "          <element units=\"Celsius\" type=\"air_temperature\">11.5</element>\n" +
      "          <element units=\"Celsius\" type=\"dew_point\">10.7</element>\n" +
      "          <element units=\"hPa\" type=\"pres\">1027.7</element>\n" +
      "          <element units=\"hPa\" type=\"msl_pres\">1027.7</element>\n" +
      "          <element units=\"hPa\" type=\"qnh_pres\">1027.7</element>\n" +
      "          <element units=\"mm\" type=\"rain_hour\">0.4</element>\n" +
      "          <element units=\"mm\" type=\"rain_ten\">0.0</element>\n" +
      "          <element units=\"%\" type=\"rel-humidity\">95</element>\n" +
      "          <element units=\"km\" type=\"vis_km\">10</element>\n" +
      "          <element type=\"wind_dir\">WSW</element>\n" +
      "          <element units=\"deg\" type=\"wind_dir_deg\">249</element>\n" +
      "          <element units=\"km/h\" type=\"wind_spd_kmh\">7</element>\n" +
      "          <element units=\"knots\" type=\"wind_spd\">4</element>\n" +
      "          <element start-time-local=\"2021-05-27T09:00:00+10:00\" end-time-local=\"2021-05-27T22:33:00+10:00\" duration=\"813\" start-time-utc=\"2021-05-26T23:00:00+00:00\" end-time-utc=\"2021-05-27T12:33:00+00:00\" units=\"mm\" type=\"rainfall\">0.4</element>\n" +
      "          <element start-time-local=\"2021-05-26T09:00:00+10:00\" end-time-local=\"2021-05-27T09:00:00+10:00\" duration=\"1440\" start-time-utc=\"2021-05-25T23:00:00+00:00\" end-time-utc=\"2021-05-26T23:00:00+00:00\" units=\"mm\" type=\"rainfall_24hr\">0.4</element>\n" +
      "          <element start-time-local=\"2021-05-27T06:00:00+10:00\" end-time-local=\"2021-05-27T21:00:00+10:00\" start-time-utc=\"2021-05-26T20:00:00+00:00\" end-time-utc=\"2021-05-27T11:00:00+00:00\" units=\"Celsius\" type=\"maximum_air_temperature\" instance=\"running\" time-utc=\"2021-05-27T01:15:00+00:00\" time-local=\"2021-05-27T11:15:00+10:00\">15.0</element>\n" +
      "          <element start-time-local=\"2021-05-27T18:00:00+10:00\" end-time-local=\"2021-05-28T09:00:00+10:00\" start-time-utc=\"2021-05-27T08:00:00+00:00\" end-time-utc=\"2021-05-27T23:00:00+00:00\" units=\"Celsius\" type=\"minimum_air_temperature\" instance=\"running\" time-utc=\"2021-05-27T12:30:00+00:00\" time-local=\"2021-05-27T22:30:00+10:00\">11.5</element>\n" +
      "          <element start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00+00:00\" end-time-utc=\"2021-05-27T14:00:00+00:00\" units=\"knots\" type=\"maximum_gust_spd\" instance=\"running\" time-utc=\"2021-05-27T05:27:00+00:00\" time-local=\"2021-05-27T15:27:00+10:00\">18</element>\n" +
      "          <element start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00+00:00\" end-time-utc=\"2021-05-27T14:00:00+00:00\" units=\"km/h\" type=\"maximum_gust_kmh\" instance=\"running\" time-utc=\"2021-05-27T05:27:00+00:00\" time-local=\"2021-05-27T15:27:00+10:00\">33</element>\n" +
      "          <element start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00+00:00\" end-time-utc=\"2021-05-27T14:00:00+00:00\" type=\"maximum_gust_dir\" instance=\"running\" time-utc=\"2021-05-27T05:27:00+00:00\" time-local=\"2021-05-27T15:27:00+10:00\">SW</element>\n" +
      "        </level>\n" +
      "      </period>\n" +
      "    </station>\n" +
      "    <station wmo-id=\"94866\" bom-id=\"086282\" tz=\"Australia/Melbourne\" stn-name=\"MELBOURNE AIRPORT\" stn-height=\"113.40\" type=\"AWS\" lat=\"-37.6654\" lon=\"144.8322\" forecast-district-id=\"VIC_PW007\" description=\"Melbourne Airport\">\n" +
      "      <period index=\"0\" time-utc=\"2021-05-27T12:30:00+00:00\" time-local=\"2021-05-27T22:30:00+10:00\" wind-src=\"OMD\">\n" +
      "        <level index=\"0\" type=\"surface\">\n" +
      "          <element units=\"Celsius\" type=\"apparent_temp\">5.8</element>\n" +
      "          <element type=\"cloud\">Mostly clear</element>\n" +
      "          <element type=\"cloud_oktas\">2</element>\n" +
      "          <element units=\"Celsius\" type=\"delta_t\">0.6</element>\n" +
      "          <element units=\"km/h\" type=\"gust_kmh\">28</element>\n" +
      "          <element units=\"knots\" type=\"wind_gust_spd\">15</element>\n" +
      "          <element units=\"Celsius\" type=\"air_temperature\">10.5</element>\n" +
      "          <element units=\"Celsius\" type=\"dew_point\">9.3</element>\n" +
      "          <element units=\"hPa\" type=\"pres\">1028.0</element>\n" +
      "          <element units=\"hPa\" type=\"msl_pres\">1028.0</element>\n" +
      "          <element units=\"hPa\" type=\"qnh_pres\">1028.0</element>\n" +
      "          <element units=\"%\" type=\"rel-humidity\">92</element>\n" +
      "          <element units=\"km\" type=\"vis_km\">41</element>\n" +
      "          <element type=\"wind_dir\">SW</element>\n" +
      "          <element units=\"deg\" type=\"wind_dir_deg\">229</element>\n" +
      "          <element units=\"km/h\" type=\"wind_spd_kmh\">24</element>\n" +
      "          <element units=\"knots\" type=\"wind_spd\">13</element>\n" +
      "          <element start-time-local=\"2021-05-27T09:00:00+10:00\" end-time-local=\"2021-05-27T22:33:00+10:00\" duration=\"813\" start-time-utc=\"2021-05-26T23:00:00+00:00\" end-time-utc=\"2021-05-27T12:33:00+00:00\" units=\"mm\" type=\"rainfall\">1.4</element>\n" +
      "          <element start-time-local=\"2021-05-26T09:00:00+10:00\" end-time-local=\"2021-05-27T09:00:00+10:00\" duration=\"1440\" start-time-utc=\"2021-05-25T23:00:00+00:00\" end-time-utc=\"2021-05-26T23:00:00+00:00\" units=\"mm\" type=\"rainfall_24hr\">0.8</element>\n" +
      "          <element start-time-local=\"2021-05-27T06:00:00+10:00\" end-time-local=\"2021-05-27T21:00:00+10:00\" start-time-utc=\"2021-05-26T20:00:00+00:00\" end-time-utc=\"2021-05-27T11:00:00+00:00\" units=\"Celsius\" type=\"maximum_air_temperature\" instance=\"running\" time-utc=\"2021-05-27T01:06:00+00:00\" time-local=\"2021-05-27T11:06:00+10:00\">13.7</element>\n" +
      "          <element start-time-local=\"2021-05-27T18:00:00+10:00\" end-time-local=\"2021-05-28T09:00:00+10:00\" start-time-utc=\"2021-05-27T08:00:00+00:00\" end-time-utc=\"2021-05-27T23:00:00+00:00\" units=\"Celsius\" type=\"minimum_air_temperature\" instance=\"running\" time-utc=\"2021-05-27T12:28:00+00:00\" time-local=\"2021-05-27T22:28:00+10:00\">10.4</element>\n" +
      "          <element start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00+00:00\" end-time-utc=\"2021-05-27T14:00:00+00:00\" units=\"knots\" type=\"maximum_gust_spd\" instance=\"running\" time-utc=\"2021-05-27T00:12:00+00:00\" time-local=\"2021-05-27T10:12:00+10:00\">28</element>\n" +
      "          <element start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00+00:00\" end-time-utc=\"2021-05-27T14:00:00+00:00\" units=\"km/h\" type=\"maximum_gust_kmh\" instance=\"running\" time-utc=\"2021-05-27T00:12:00+00:00\" time-local=\"2021-05-27T10:12:00+10:00\">52</element>\n" +
      "          <element start-time-local=\"2021-05-27T00:00:00+10:00\" end-time-local=\"2021-05-28T00:00:00+10:00\" start-time-utc=\"2021-05-26T14:00:00+00:00\" end-time-utc=\"2021-05-27T14:00:00+00:00\" type=\"maximum_gust_dir\" instance=\"running\" time-utc=\"2021-05-27T00:12:00+00:00\" time-local=\"2021-05-27T10:12:00+10:00\">W</element>\n" +
      "        </level>\n" +
      "      </period>\n" +
      "    </station>\n" +
      "  </observations>\n" +
      "</product>\n"

    val result = mapWeatherSummary(xmlMock)

    printObject(result)

    assertEquals(
      WeatherSummary(
        apparentTemp = 10.4,
        deltaT = 0.4,
        gustKmh = 15.0,
        windGustSpeed = 8.0,
        airTemperature = 11.5,
        dewPoint = 10.7,
        pres = 1027.7,
        mslPres = 1027.7,
        qnhPres = 1027.7,
        rainHour = 0.4,
        rainTen = 0.0,
        relHumidity = 95.0,
        visKm = 10.0,
        windDir = "WSW",
        windDirDeg = 249.0,
        windSpeedKmh = 7.0,
        windSpeed = 4.0,
      ), result)
  }
}