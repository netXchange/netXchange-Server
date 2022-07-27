import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class BasicClient {

    public static Socket s;
    public static void main(String[] args) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter Server Address:");
            String string = in.readLine();
            s = new Socket(string,31415);
            PrintWriter socketWriter = new PrintWriter(s.getOutputStream(), true);
            UUID uuid = UUID.randomUUID();
            socketWriter.println(uuid);
            System.out.println(uuid);
            Thread t = new Thread(){
                public void run(){
                    try {
                        BufferedReader socketReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        while (true){
                           String line = socketReader.readLine();
                           if(line != null) System.out.println(line);
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            t.start();

            String line = in.readLine();
            while(true){
                if(line != null && !line.equals("")){
                    socketWriter.println(","+line);
                }
                line = in.readLine();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
