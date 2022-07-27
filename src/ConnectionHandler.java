import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectionHandler extends Thread{

    ArrayList<String> in;
    Socket socket;
    BufferedReader input;
    PrintStream output;
    UUID id;
    public ConnectionHandler(Socket s, BufferedReader reader, PrintStream writer, UUID id){
        this.socket = s;
        this.input = reader;
        this.output = writer;
        this.id = id;
        this.in = new ArrayList<>();
    }

    public void run(){
        Thread t = new Thread(){
            public void run(){
                for(int i = 0; i < in.size(); i++){
                    output.println(in.get(i));
                    System.out.println(in.get(i));
                }
                in.clear();
            }
        };
        t.start();
        try {
            String line = input.readLine();
            while(true){
                if(line != null && !line.equals("")){
                    String[] data = line.split(",");
                    if (data.length < 3){
                    } else if(data[1].equals("")){
                        synchronized (Main.in){
                            Main.in.add(data[1]);
                        }
                    }else if(data.length >= 3){
                        String to = data[1];
                        String message = data[2];
                        synchronized (Main.connectionHandlers) {
                            ConnectionHandler endpoint = Main.connectionHandlers.get(UUID.fromString(to));
                            synchronized (endpoint.in){
                                endpoint.in.add(message);
                            }
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
