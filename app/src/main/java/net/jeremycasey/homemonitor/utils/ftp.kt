package net.jeremycasey.homemonitor.utils

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.*

suspend fun fetchFromFtp(server: String, path: String): String {
  val port = 21
//  val user = "user"
//  val pass = "pass"

  println("$server, $path")

  val ftpClient = FTPClient()
  var bufferedOutputStream: OutputStream? = null
  var byteArrayOutputStream: ByteArrayOutputStream? = null
  var inputStream: InputStream? = null
  try {
    ftpClient.connect(server)
//    ftpClient.login(user, pass)
//    ftpClient.login(null, null)
//    ftpClient.enterLocalPassiveMode()
    ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

    println("world")

    byteArrayOutputStream = ByteArrayOutputStream()
    val success: Boolean = ftpClient.retrieveFile(path, BufferedOutputStream(byteArrayOutputStream))
    println("success $success")
    if (success) {
      return byteArrayOutputStream.toString()
    } else {
      throw Exception("Failed to fetch ftp file")
    }

//    byteArrayOutputStream = ByteArrayOutputStream()
//    bufferedOutputStream = BufferedOutputStream(byteArrayOutputStream)
//    inputStream = ftpClient.retrieveFileStream(path)
//    val bytesArray = ByteArray(4096)
//    var bytesRead = -1
//    while (true) {
////    while (inputStream.read(bytesArray).also { bytesRead = it } != -1) {
//      println(bytesArray)
//      bytesRead = inputStream.read(bytesArray)
//      if (bytesRead == -1) {
//        break
//      }
//      println("read...")
//      bufferedOutputStream.write(bytesArray, 0, bytesRead)
//    }
//    val success = ftpClient.completePendingCommand()
//    if (success) {
//      println("success!")
//      return byteArrayOutputStream.toString()
//    } else {
//      throw Exception("Failed to fetch ftp file")
//    }

    // APPROACH #1: using retrieveFile(String, OutputStream)
//    val remoteFile1 = "/test/video.mp4"
//    val downloadFile1 = File("D:/Downloads/video.mp4")
//    val outputStream1: OutputStream = BufferedOutputStream(FileOutputStream(downloadFile1))
//    var success: Boolean = ftpClient.retrieveFile(remoteFile1, outputStream1)
//    outputStream1.close()
//    if (success) {
//      println("File #1 has been downloaded successfully.")
//    }

    // APPROACH #2: using InputStream retrieveFileStream(String)
//    val remoteFile2 = "/test/song.mp3"
//    val downloadFile2 = File("D:/Downloads/song.mp3")
//    val outputStream2: OutputStream = BufferedOutputStream(FileOutputStream(downloadFile2))
//    val inputStream: InputStream = ftpClient.retrieveFileStream(remoteFile2)
//    val bytesArray = ByteArray(4096)
//    var bytesRead = -1
//    while (inputStream.read(bytesArray).also { bytesRead = it } != -1) {
//      outputStream2.write(bytesArray, 0, bytesRead)
//    }
//    success = ftpClient.completePendingCommand()
//    if (success) {
//      println("File #2 has been downloaded successfully.")
//    }
//    outputStream2.close()
//    inputStream.close()
  } catch (ex: java.lang.Exception) {
    println(ex)
    ex.printStackTrace()
    throw ex
  } finally {
    bufferedOutputStream?.close()
    byteArrayOutputStream?.close()
    inputStream?.close()

    if (ftpClient.isConnected()) {
      ftpClient.logout()
      ftpClient.disconnect()
    }
  }
}
