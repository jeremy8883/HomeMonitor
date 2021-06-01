package net.jeremycasey.homemonitor.widgets.ptv

import net.jeremycasey.homemonitor.widgets.ptv.api._buildTtapiUrl
import org.junit.Test

import org.junit.Assert.*


class PtvApiTest {
  @Test
  fun generatesASignedUrl() {
    val result = _buildTtapiUrl("/v3/stops/1122/route_type/1", "1003441", "9c132d31-6a30-4cac-8d8b-8a1970834799")

    assertEquals(
      "https://timetableapi.ptv.vic.gov.au/v3/stops/1122/route_type/1?devid=1003441&signature=D5D8772F11AC06AB1EC8624AFA65575F47D60ABC",
      result
    )
  }
}
