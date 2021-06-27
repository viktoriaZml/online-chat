package Server;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
  private Socket socket;
  private String login;
  private PrintWriter out;
  private BufferedReader in;

  public ClientThread(Socket socket) throws IOException {
    this.socket = socket;
    this.out = new PrintWriter(socket.getOutputStream(), true);
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.start();
  }

  @Override
  public void run() {
    String line;
    try {
      while ((line = in.readLine()) != null) {
        if (line.startsWith("/exit")) {
          break;
        } else if (this.login == null) {
          this.login = line;
          line = "Подключился " + line;
        } else {
          line = login + ": " + line;
        }
        Server.sendMessageToAllClients(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      this.close();
    }
  }

  private void close() {
    String msg = login + " вышел из чата.";
    //System.out.println(msg);
    Server.sendMessageToAllClients(msg);
    Server.removeClientThread(this);
    try {
      this.socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendMsg(String msg) {
    out.println(msg);
  }
}