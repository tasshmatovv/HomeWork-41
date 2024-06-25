package month6.Echo.EchoServer;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class InformationProcessor {

    private final EchoServer server;
    private final String name;

    public InformationProcessor(EchoServer server, Socket socket, String name) {
        this.server = server;
        this.name = name;
    }

    private boolean isEmptyMsg(String message){
        return message == null || message.isBlank();
    }

    private void sendResponse(String response, Writer writer) throws IOException {
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

    private Scanner getReader(Socket socket) throws IOException {
        InputStream input = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(input, "UTF-8");
        return new Scanner(isr);
    }

    private PrintWriter getWriter(Socket socket)throws IOException{
        OutputStream  output = socket.getOutputStream();
        var writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);
        return new PrintWriter(writer);
    }

    private boolean isQuitMsg(String message){
            return "bye".equalsIgnoreCase(message);
    }

    public void handle(Socket socket)  {
        System.out.printf("Подключен клиент: %s%n",socket.getPort(),name);

        try(socket;
            Scanner reader = getReader(socket);
            PrintWriter writer = getWriter(socket)) {
            sendResponse("Привет " + name, writer);
            while (true) {
                String message = reader.nextLine().strip();
                if (isEmptyMsg(message) || isQuitMsg(message)) {
                    return;
                }
                String responce = name + ":" + message;
                server.broadcastMessage(responce,socket);
            }
        }catch (NoSuchElementException ex) {
            System.out.println("Client dropped connection");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            server.releaseName(name);
            server.removeClient(socket);
            System.out.printf("Клиент %s отключился",name );
        }
    }
}
