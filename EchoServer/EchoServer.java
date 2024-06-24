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
//      try (var clientSocket = server.accept()) {
//        handle(clientSocket);
//      }
      while (!server.isClosed()){
        Socket socket = server.accept();
        handle(socket);
      }
    } catch (IOException e) {
      var formatMsg = "Вероятнее всего порт %s занят.%n";
      System.out.printf(formatMsg, port);
      e.printStackTrace();
    }
  }

  private void handle(Socket clientSocket){
    InformationProcessor p = new InformationProcessor();
    try {
      p.handle(clientSocket);
    }catch (IOException e){
      System.out.println("Ошибка при подключении");
      e.printStackTrace();
    }
  }

}
