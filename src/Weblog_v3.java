import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Weblog_v3 {

    public static class Task implements Runnable {
        private String riga;

        public Task(String riga) {
            this.riga = riga;
        }

        public void run() {

            String[] parole = this.riga.split(" - - ");
            try {
                parole[0] = (InetAddress.getByName(parole[0])).getHostName();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            String riga_modificata = parole[0] + " - - " + parole[1];
            System.out.println(riga_modificata);

        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 1)
            System.out.println("Devi inserire il file di log!");

        FileReader file = null;
        try {
            file = new FileReader(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("File non trovato!");
            e.printStackTrace();
            System.exit(1);
        }

        ExecutorService service = Executors.newCachedThreadPool();

        BufferedReader buf = new BufferedReader(file);
        String riga = "";
        while ((riga = buf.readLine()) != null)
            service.execute(new Task(riga));

        service.shutdown();

        if (!service.awaitTermination(60000, TimeUnit.SECONDS))
            System.err.println("I thread non sono stati completati entro i tempi!");

        System.exit(0);


    }



}
