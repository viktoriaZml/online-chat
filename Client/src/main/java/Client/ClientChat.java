package Client;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {
  private static final String settingsFile = "settings.txt";
  private static final Logger log = Logger.getLogger(ClientChat.class.getName());
  private static final String logFile = "./Client/file.log";
  private static FileWriter fileWriter;

  public static void main(String[] args) throws Exception {
    String[] settings = getSett();
    // Определяем сокет сервера
    Socket socket = new Socket(settings[0], Integer.parseInt(settings[1]));
    fileWriter = new FileWriter(new File(logFile));
    // Получаем входящий и исходящий потоки информации
    try (BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
         PrintWriter out = new PrintWriter(
                 new OutputStreamWriter(socket.getOutputStream()), true);
         Scanner scanner = new Scanner(System.in)) {
      runChat(in, out, scanner);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void runChat(BufferedReader in, PrintWriter out, Scanner scanner) throws IOException {
    String name = null;
    ServerListener serverListener = null;
    String msg;
    System.out.println("Введите свой ник (/exit для выхода):");
    while (true) {
      msg = scanner.nextLine();
      if ("".equals(msg)) continue;
      out.println(msg);
      if ("/exit".equals(msg)) {
        if (serverListener != null)
          serverListener.interrupt();
        //Thread.sleep(1000);
        break;
      }
      if (name == null) {
        name = msg;
        serverListener = new ServerListener(in);
      }
    }
  }

  // читаем настройки из файла
  public static String[] getSett() throws Exception {
    String[] str = null;
    try (BufferedReader reader = new BufferedReader(new FileReader(settingsFile))) {
      str = reader.readLine().split(" ");
      if (str.length < 2) {
        throw new Exception("Некорректные настройки в файле " + settingsFile);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return str;
  }

  public static void writeToLog(String msg) {
    try {
      fileWriter.write(msg + "\n");
      fileWriter.flush();
    } catch (Exception e) {
      log.info("write to log-file error");
    }
  }
}
