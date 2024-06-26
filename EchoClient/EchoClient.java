package month6.Echo.EchoClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
  private final int port;
  private final String host;

  private EchoClient(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public static EchoClient connectTo(int port) {
    var localhost = "127.0.0.1";
    return new EchoClient(localhost, port);
  }

  public void run() {
    System.out.printf("напиши 'bye' чтобы выйти%n%n%n");
    try (var socket = new Socket(host, port)) {
      var scanner = new Scanner(System.in, "UTF-8");
      var output = socket.getOutputStream();
      var writer = new PrintWriter(output);
      var reader = new InputStreamReader(socket.getInputStream(), "UTF-8");
      var stringBuilder = new StringBuilder();
      try (scanner; writer) {
        while (true) {
          String message = scanner.nextLine();
          writer.write(message);
          writer.write(System.lineSeparator());
          writer.flush();

          int ch;

          while((ch = reader.read()) != '\n'){
            stringBuilder.append((char) ch);
          }

          String response = stringBuilder.toString().trim();
          stringBuilder.setLength(0);
          System.out.println("Server: "+ response);

          if ("bye".equals(message.toLowerCase())) {
            return;
          }
        }
      }
    } catch (NoSuchElementException ex) {
      System.out.printf("Connection dropped!%n");
    } catch (IOException e) {
      var msg = "Can't connect to %s:%s!%n";
      System.out.printf(msg, host, port);
      e.printStackTrace();
    }
  }

  // прочие методы
}
