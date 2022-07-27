import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

public class Main {
    public static Hashtable<UUID,ConnectionHandler> connectionHandlers = new Hashtable<>();
    public static ArrayList<String> in = new ArrayList<>();
    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(31415);
            Thread t = new Thread(){
                public void run(){
                    try {
                        while(true) {
                            Socket s = server.accept();
                            BufferedReader socketReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                            PrintStream socketWriter = new PrintStream(s.getOutputStream(), true);
                            UUID uuid = UUID.fromString(socketReader.readLine());
                            socketWriter.println("Welcome to the netXchange!");
                            ConnectionHandler handler = new ConnectionHandler(s, socketReader, socketWriter, uuid);
                            handler.start();
                            connectionHandlers.put(uuid, handler);
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Invalid Connection");
                        System.exit(2);
                    }
                }
            };
            t.start();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Port Bound");
            System.exit(1);
        }
        long start = System.currentTimeMillis();
        while(true){
            if(System.currentTimeMillis() - start >= 100) {
                synchronized (in){
                    for (String m : in) {
                        System.out.println(m);
                    }
                    in.clear();
                }
                start = System.currentTimeMillis();
            }
        }
    }
}
