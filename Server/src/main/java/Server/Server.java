package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.*;

public class Server {
  private static final String host = "localhost";
  private static final int port = 12321;
  private static final String settingsFile = "settings.txt";
  private static List<ClientThread> clients = new ArrayList<ClientThread>();
  private static final Logger log = Logger.getLogger(Server.class.getName());
  private static final String logFile = "./Server/file.log";
  private static FileWriter fileWriter;
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a");

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      createSettingsFile();
      log.info("Сервер запущен!");
      fileWriter = new FileWriter(logFile, true);
      while (true) {
        Socket socket = serverSocket.accept();
        try {
          ClientThread client = new ClientThread(socket);
          clients.add(client);
        } catch (IOException e) {
          socket.close();
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // пишем настройки в файл
  public static void createSettingsFile() throws IOException {
    FileWriter file = new FileWriter(settingsFile, false);
    file.write(host + " " + port);
    file.flush();
  }

  // отправляем сообщение всем клиентам
  public static void sendMessageToAllClients(String msg) {
    log.info(msg);
    msg = dateFormat.format(new Date()) + " " + msg;
    writeToLog(msg);
    for (ClientThread o : clients) {
      o.sendMsg(msg);
    }
  }

  public static void removeClientThread(ClientThread clientThread) {
    clients.remove(clientThread);
  }

  public static void writeToLog(String msg) {
    try {
      fileWriter.write(msg + "\n");
      fileWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
