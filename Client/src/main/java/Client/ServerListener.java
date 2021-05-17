package Client;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerListener extends Thread {
  private BufferedReader bufferedReader;
  private Logger log = Logger.getLogger(ServerListener.class.getName());

  public ServerListener(BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
    this.start();
  }

  @Override
  public void run() {
    try {
      while (!this.isInterrupted()) {
        String msg = bufferedReader.readLine();
        System.out.println(msg);
        //log.info(msg);
        ClientChat.writeToLog(msg);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
