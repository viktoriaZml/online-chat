import Client.ClientChat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.Scanner;

public class ClientTest {
  @Test
  void testGetSett() throws Exception {
    String host = "localhost";
    Integer port = 12321;
    String[] sett = ClientChat.getSett();
    Assertions.assertEquals(sett[0], host);
    Assertions.assertEquals(Integer.parseInt(sett[1]), port);
  }

  @Test
  void testRunChat() throws IOException {
    BufferedReader in = Mockito.mock(BufferedReader.class);
    PrintWriter out = Mockito.mock(PrintWriter.class);
    File file = new File("testClient.txt");
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write("Петя\nПривет\nПока\n/exit");
    fileWriter.flush();
    FileReader reader = new FileReader(file);
    Scanner scanner = new Scanner(reader);
    ClientChat.runChat(in, out, scanner);
    Mockito.verify(out, Mockito.times(1)).println("Петя");
    Mockito.verify(out, Mockito.times(1)).println("Привет");
    Mockito.verify(out, Mockito.times(1)).println("Пока");
    Mockito.verify(out, Mockito.times(1)).println("/exit");
  }
}