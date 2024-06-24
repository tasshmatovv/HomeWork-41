package month6.Echo.EchoServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InformationProcessor {
    public void handle(Socket socket) throws IOException {
        var input = socket.getInputStream();
        var isr = new InputStreamReader(input, "UTF-8");
        var output = socket.getOutputStream();
        var writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true);

        try (var scanner = new Scanner(isr)) {
            while (true) {
                var message = scanner.nextLine().strip();
                System.out.printf("Got: %s%n", message);
                writer.println(message);
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
