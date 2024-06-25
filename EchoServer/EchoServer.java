package month6.Echo.EchoServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class EchoServer {
  private final ExecutorService pool = Executors.newCachedThreadPool();
  private final int port;

  public EchoServer(int port) {
    this.port = port;
  }

  public static EchoServer bindToPort(int port) {
    return new EchoServer(port);
  }


  public void run() {
    try (var server = new ServerSocket(port)) {
      while (!server.isClosed()){
        Socket socket = server.accept();
        pool.submit(() ->handle(socket));
      }
    } catch (IOException e) {
      var formatMsg = "Вероятнее всего порт %s занят.%n";
      System.out.printf(formatMsg, port);
      e.printStackTrace();
    }
  }

  private void handle(Socket clientSocket){
    InformationProcessor p = new InformationProcessor();
      p.handle(clientSocket);
  }

}
