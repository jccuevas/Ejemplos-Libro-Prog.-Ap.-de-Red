package libro.ejemplos;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServicioConectarNuevoProceso extends Service {
    public static final String EXTRA_IP = "servicio_ip";
    public static final String EXTRA_PORT = "servicio_puerto";
    protected String mIp = "192.158.1.157";
    protected int mPuerto = 6000;
    private NotificationManager mNM;
    private int mId = 1;
    private String mRespuesta="";

    public ServicioConectarNuevoProceso() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Toast.makeText(this, getString(R.string.nuevo_servicio), Toast.LENGTH_SHORT).show();

        int n=0;
        while(n<10) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            n++;
            Toast.makeText(this, "ciclo "+n, Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }


    /**
     * HebraConectarSimple
     * Esta clase permite la conexión con un servidor TCP y la recepcion de la respuesta del mismo
     */
    public class HebraConectarSimple implements Runnable {

        String mResponse = "";
        String mRespuesta = "";
        private InetSocketAddress mIp = null;

        public HebraConectarSimple(InetSocketAddress ip) {
            mIp = ip;
        }

        @Override
        public void run() {
            Socket cliente;
            try {
                //Se crea el socket TCP
                cliente = new Socket();
                //Se realiza la conexión al servidor
                cliente.connect(mIp);
                //Se leen los datos del buffer de entrada
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                mRespuesta = bis.readLine();

                os.write(new String("USER USER\r\n").getBytes());
                os.flush();
                mRespuesta = bis.readLine();

                os.write(new String("PASS 12345\r\n").getBytes());
                os.flush();
                mRespuesta = bis.readLine();
                if (mRespuesta != null) {
                    if (mRespuesta.startsWith("OK"))
                        mRespuesta = "Autenticado correctamente";
                    else {
                        mRespuesta = "Error de autenticación";
                    }
                }

                for (int n = 0; n < 10; n++) {
                    os.write(new String("ECHO " + n + "\r\n").getBytes());
                    os.flush();
                    mRespuesta = bis.readLine();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                os.write(new String("QUIT\r\n").getBytes());
                os.flush();
                mRespuesta = bis.readLine();

                bis.close();
                os.close();
                cliente.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
                mRespuesta = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                mRespuesta = "IOException: " + e.toString();
            } finally {
                stopForeground(true);
            }

        }
    }
}
