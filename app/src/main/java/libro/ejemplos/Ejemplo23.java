package libro.ejemplos;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Ejemplo23 extends AppCompatActivity {

    public static final String URL_SCHEMA = "http";
    public static final String URL_CHARSET = "UTF-8";//Para las URL debe ser este juego de caracteres
    public static final String DEBUG_TAG = "Debug-Ejemplo23";
    public int mTotal = 0;

    WebView mResultado = null;
    ProgressDialog mBar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejemplo23);

        mResultado = (WebView) findViewById(R.id.e23_webview);

        final EditText editDominio = (EditText) findViewById(R.id.e23_edit_url);//Dominio y recurso
        final EditText editParametro = (EditText) findViewById(R.id.e23_edit_param);
        final EditText editValor = (EditText) findViewById(R.id.e23_edit_valor);


        mBar = new ProgressDialog(this);
        mBar.setIndeterminate(false);
        mBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        mBar.setTitle(getString(R.string.e23_descargando));

        Button descargar = (Button) findViewById(R.id.e23_descargar);
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dominio = editDominio.getEditableText().toString();
                String parametro="";
                String valor="";
                try {
                    parametro = URLEncoder.encode(editParametro.getEditableText().toString(),URL_CHARSET);
                    valor = URLEncoder.encode(editValor.getEditableText().toString(),URL_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String urlFinal = URL_SCHEMA + "://" +dominio+"?"+parametro+"="+valor;

                mBar.setMessage(urlFinal);
                mBar.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            descargaHttp(urlFinal);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }


    private void descargaHttp(String direccion) throws IOException {
        InputStream is = null;
        URL url = new URL(direccion);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setReadTimeout(2000 /* milisegundos */);
            conn.setConnectTimeout(15000 /* milisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            final int len = conn.getHeaderFieldInt("CONTENT-LENGTH", 1024);
            final String contentType = conn.getHeaderField("CONTENT-TYPE");
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBar.setMax(len);
                }
            });

            final String datos = new String(readStream(is));
            if (datos != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBar.dismiss();
                        mResultado.loadData(datos, contentType, "iso_8859-15");
                    }
                });
            }
            is.close();
        } finally {
           conn.disconnect();
        }
    }

    private void autencicaHttp(String direccion) throws IOException {
        InputStream is = null;
        URL url = new URL(direccion);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try {
            conn.setReadTimeout(2000 /* milisegundos */);
            conn.setConnectTimeout(15000 /* milisegundos */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            final int len = conn.getHeaderFieldInt("CONTENT-LENGTH", 1024);
            final String contentType = conn.getHeaderField("CONTENT-TYPE");
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBar.setMax(len);
                }
            });

            final String datos = new String(readStream(is));
            if (datos != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBar.dismiss();
                        mResultado.loadData(datos, contentType, "iso_8859-15");
                    }
                });
            }
            is.close();
        } finally {
            conn.disconnect();
        }
    }


    public byte[] readStream(InputStream is) {
        byte[] datos = new byte[1024];
        int leidos = 0;

        mTotal = 0;
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
            do {
                leidos = is.read(datos);

                if (leidos > 0) {
                    baos.write(datos);
                    mTotal = mTotal + leidos;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBar.setProgress(mTotal);
                        }
                    });
                }
            } while (leidos > 0);
            return baos.toByteArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
        return null;
    }
}
