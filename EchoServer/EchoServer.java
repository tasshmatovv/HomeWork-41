package month6.Echo.EchoServer;

import com.sun.source.doctree.SeeTree;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.*;

public class EchoServer {
  private final ExecutorService pool = Executors.newCachedThreadPool();
  private final int port;
  private final Set<Socket>clients = ConcurrentHashMap.newKeySet();

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
        clients.add(socket);
        pool.submit(() ->handle(socket));
      }
    } catch (IOException e) {
      var formatMsg = "Вероятнее всего порт %s занят.%n";
      System.out.printf(formatMsg, port);
      e.printStackTrace();
    }
  }

  private void handle(Socket clientSocket){
    InformationProcessor p = new InformationProcessor(this,clientSocket);
      p.handle(clientSocket);
  }

  public void broadcastMessage(String message, Socket sender){
    for (Socket client: clients){
      if (!client.equals(sender)){
        try {
          PrintWriter writer = new PrintWriter( new OutputStreamWriter(client.getOutputStream(), "UTF-8"), true);
          System.out.println(message);
      }catch (IOException e){
          e.printStackTrace();
        }
      }
    }
  }

  public void removeClient(Socket socket) {
    clients.remove(socket);
  }
}
