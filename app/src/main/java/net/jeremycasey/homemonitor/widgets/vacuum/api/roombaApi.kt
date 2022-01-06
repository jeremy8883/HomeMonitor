package net.jeremycasey.homemonitor.widgets.vacuum.api
import VacuumCommand
import VacuumState
import net.jeremycasey.homemonitor.private.roombaAddress
import net.jeremycasey.homemonitor.private.roombaBlid
import net.jeremycasey.homemonitor.private.roombaPassword
import org.eclipse.paho.client.mqttv3.MqttException

import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.joda.time.DateTime
import java.util.*

private fun connect(
  blid: String, password: String, host: String,
  onSuccess: (state: VacuumState) -> Unit, onError: (ex: MqttException) -> Unit
) {
  val posibleCap = listOf("pose", "ota", "multiPass", "carpetBoost", "pp", "binFullDetect", "langOta", "maps", "edge", "eco", "svcConf")
  val emitIntervalTime = 800
  var robotState = {}
  var cap = null
  var missionInterval: Long? = null

  val url = "tcp://$host:8883"

  // ---

//  val topic = "MQTT Examples"
//  val content = "Message from MqttPublishSample"
//  val qos = 2
//  val broker = "tcp://mqtt.eclipse.org:1883"
//  val clientId = "JavaSample"
  val persistence = MemoryPersistence()

  try {
    val client = MqttClient(url, blid, persistence)
    val connOpts = MqttConnectOptions()
    connOpts.password = password.toCharArray()
//    connOpts.rejectUnauthorized: false,
//    connOpts.protocolId = 'MQTT'
//    connOpts.protocolVersion = 4
    connOpts.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
    val sslProperties = Properties()
    sslProperties.setProperty("com.ibm.ssl.protocol", "tls")
//    connOpts.ciphers = process.env.ROBOT_CIPHERS || 'AES128-SHA256'
    sslProperties.setProperty("com.ibm.ssl.enabledCipherSuites", "AES128-SHA256")
    connOpts.sslProperties = sslProperties
    connOpts.isCleanSession = false

    println("Connecting to broker: $blid")
    client.connect(connOpts)
    println("Connected")
    println("Publishing message: clean")
    val message = MqttMessage("{\"command\": \"clean\", \"time\": ${DateTime().millis / 1000}, \"initiator\": \"localApp\"}".toByteArray())
    message.qos = 2
    client.publish("cmd", message)
    println("Message published")
    client.disconnect()
    println("Disconnected")
    onSuccess(VacuumState(batPct = 100, lastCommand = VacuumCommand(
      command = "dock",
      initiator = "rmtApp",
      time = 1625916983,
    )))
  } catch (ex: MqttException) {
    println("reason " + ex.reasonCode)
    println("msg " + ex.message)
    println("loc " + ex.localizedMessage)
    println("cause " + ex.cause)
    println("excep $ex")
    ex.printStackTrace()
    onError(ex)
  }
}

fun fetchStatus(onSuccess: (state: VacuumState) -> Unit, onError: (ex: MqttException) -> Unit) {
  return connect(roombaBlid, roombaPassword, roombaAddress, onSuccess, onError)
}
