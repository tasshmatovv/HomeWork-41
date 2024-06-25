package month6.Echo.EchoServer;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InformationProcessor {

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
        System.out.printf("Подключен клиент: %s%n",socket.getPort());

        try(socket;
            Scanner reader = getReader(socket);
            PrintWriter writer = getWriter(socket)) {
            sendResponse("Привет"+ socket.getPort(),writer);
            while (true) {
                String message = reader.nextLine().strip();
                writer.println(message);
                if (isEmptyMsg(message)|| isQuitMsg(message)) {
                    return;
                }
                sendResponse(message.toUpperCase(),writer);
            }

        } catch (NoSuchElementException ex) {
            System.out.println("Client dropped connection");
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.printf("клиент отключен: %s%n",socket.getPort() );
    }
}
