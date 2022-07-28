import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectionHandler extends Thread{
    private Socket socket;
    private BufferedReader input;
    private PrintStream output;
    public UUID uuid;
    public ConnectionHandler(Socket s, BufferedReader reader, PrintStream writer, UUID uuid){
        this.socket = s;
        this.input = reader;
        this.output = writer;
        this.uuid = uuid;
    }

    public boolean send(String message){
        try {
            synchronized (output) {
                output.println(message);
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void run(){
        try {
            String line = input.readLine();
            while(true){
                if(line != null && !line.equals("")){
                    String[] data = line.split(",");
                    if (data.length < 2){
                        synchronized (Main.in){
                            Main.in.add(data[0]);
                        }
                    } else if(data[0].equals("")){
                        synchronized (Main.in){
                            Main.in.add(data[1]);
                        }
                    }else if(data.length >= 2){
                        String to = data[0];
                        String message = data[1];
                        synchronized (Main.connectionHandlers) {
                            ConnectionHandler endpoint = Main.connectionHandlers.get(UUID.fromString(to));
                            Main.debug("Endpoint found and synced, sending: \""+message+"\" to "+data[0]);
                            endpoint.send(message);
                        }
                    }
                }
                line = input.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("USR IO");
            System.exit(3);
        }
    }
}
