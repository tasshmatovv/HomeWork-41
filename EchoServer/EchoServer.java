package month6.Echo.EchoServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
  private final int port;

  private EchoServer(int port) {
    this.port = port;
  }

  public static EchoServer bindToPort(int port) {
    return new EchoServer(port);
  }

  public void run() {
    try (var server = new ServerSocket(port)) {
      try (var clientSocket = server.accept()) {
        handle(clientSocket);
      }
    } catch (IOException e) {
      var formatMsg = "Вероятнее всего порт %s занят.%n";
      System.out.printf(formatMsg, port);
      e.printStackTrace();
    }
  }

  private void handle(Socket socket) throws IOException {
    var input = socket.getInputStream();
    var isr = new InputStreamReader(input, "UTF-8");
    var output = socket.getOutputStream();
    var writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

    try (var scanner = new Scanner(isr)) {
      while (true) {
        var message = scanner.nextLine().strip();
        System.out.printf("Got: %s%n", message);
        var reversedMessage = new StringBuilder(message).reverse().toString();
        writer.println(reversedMessage);
        if (message.toLowerCase().equals("bye")) {
          System.out.println("Bye bye");
          return;
        }
      }
    } catch (NoSuchElementException ex) {
      System.out.println("Client dropped connection");
    }
  }
}
