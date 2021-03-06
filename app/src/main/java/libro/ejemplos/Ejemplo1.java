package libro.ejemplos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * Ejemplo 1. Creación de una nueva actividad
 *
 * En este ejemplo se muestra como pasar parámetros a la nueva
 * actividad a crear tras la pulsación de un botón que hace que se
 * ejecute el método nuevoUusario()
 */
public class Ejemplo1 extends AppCompatActivity {

    public static final String EXTRA_NOMBRE = "nombre";
    public static final String EXTRA_CLAVE = "clave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejemplo01);

    }
    public void nuevoUsuario(View view) {
        Intent intent = new Intent(this, NuevoUsuarioActivity.class);
        //Se obtienen los valores de los campos de texto
        EditText campo_nombre = (EditText) findViewById(R.id.e01_edit_name);
        EditText campo_clave = (EditText) findViewById(R.id.e01_edit_pass);
        String nombre = campo_nombre.getText().toString();
        String clave = campo_clave.getText().toString();
        //Se pasan a la nueva actividad
        intent.putExtra(EXTRA_NOMBRE, nombre);
        intent.putExtra(EXTRA_CLAVE, clave);
        startActivity(intent);

    }
}
