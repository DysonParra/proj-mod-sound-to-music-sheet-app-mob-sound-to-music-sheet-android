/*
 * @fileoverview    {LoadMusicSheetActivity}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementation done.
 * @version 2.0     Documentation added.
 */
package com.project.dev.soundtomusicsheet.activity;

import com.project.dev.soundtomusicsheet.R;
import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.XmlFile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * TODO: Description of {@code LoadMusicSheetActivity}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class LoadMusicSheetActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private Button btnAddXmlFile;
    private Button btnValidateXmlFile;
    private Button btnContinue;
    private CheckBox chkBoxDeleteRestAtEnd;

    /*
     * Variables locales.
     */
    private MusicSheet musicSheet;                                      // Contiene los diferentes simbolos musicales.
    private XmlFile xmlFile;                                            // Contiene la ruta de la partitura a cargar.
    private int xmlLoaded = 1;                                          // Si se cargó exitosamente el archivo xml.

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_music_sheet);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        btnAddXmlFile = findViewById(R.id.btnAddXmlFile);
        btnValidateXmlFile = findViewById(R.id.btnValidateXmlFile);
        btnContinue = findViewById(R.id.btnContinue);
        chkBoxDeleteRestAtEnd = findViewById(R.id.chkBoxDeleteRestAtEnd);

        // Desactiva todos los botones exceptuando el de seleccionar archivo xml.
        btnValidateXmlFile.setEnabled(false);
        btnContinue.setEnabled(false);
        chkBoxDeleteRestAtEnd.setEnabled(false);

        // Inicializa el objeto de tipo partitura y el objeto de tipo archivo xml.
        musicSheet = new MusicSheet(440f);
        xmlFile = new XmlFile();

        // Comportamiento del botón seleccionar archivo xml.
        btnAddXmlFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un intent para seleccionar archivos.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Indica que solo se podrán seleccionar archivos xml.
                intent.setType("text/xml");

                // Inicia el intent de selcción de archivo.
                startActivityForResult(Intent.createChooser(intent, "Choose File"), xmlLoaded);
            }
        });

        // Comportamiento del botón validar Xml.
        btnValidateXmlFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int validate = xmlFile.validateXml();                                   // A validate le lleva si la partitura es válida.

                // Evalúa el resultado obtenido y muestra mensaje dependiendo del valor.
                switch (validate) {
                    case 1:
                        Toast.makeText(LoadMusicSheetActivity.this,
                                LoadMusicSheetActivity.this.getString(R.string.validXmlFile),
                                Toast.LENGTH_SHORT).show();
                        btnContinue.setEnabled(true);
                        chkBoxDeleteRestAtEnd.setEnabled(true);
                        break;
                    case -1:
                        Toast.makeText(LoadMusicSheetActivity.this,
                                LoadMusicSheetActivity.this.getString(R.string.invalidXmlFile1),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case -2:
                        Toast.makeText(LoadMusicSheetActivity.this,
                                LoadMusicSheetActivity.this.getString(R.string.invalidXmlFile2),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case -3:
                        Toast.makeText(LoadMusicSheetActivity.this,
                                LoadMusicSheetActivity.this.getString(R.string.invalidXmlFile3),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case -4:
                        Toast.makeText(LoadMusicSheetActivity.this,
                                LoadMusicSheetActivity.this.getString(R.string.invalidXmlFile4),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case -5:
                        Toast.makeText(LoadMusicSheetActivity.this,
                                LoadMusicSheetActivity.this.getString(R.string.invalidXmlFile5),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // Comportamiento del botón de comenzar.
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // A xmlFile le lleva si se ignorarán silencios al final.
                xmlFile.setIgnoreRestsAtEnd(chkBoxDeleteRestAtEnd.isChecked());
                //musicSheet.getList().printList();

                // Crea un mensaje de alerta para preguntar si se edita información en la partitura.
                new AlertDialog.Builder(LoadMusicSheetActivity.this)
                        .setTitle(LoadMusicSheetActivity.this.getString(R.string.editInfoTitle))
                        .setMessage(LoadMusicSheetActivity.this.getString(R.string.editInfoMessage))
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            // Si se indica no editar información.
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Crea un intent para iniciar el activiy de afinación.
                                Intent intent = new Intent(LoadMusicSheetActivity.this, TunerActivity.class);

                                // Manda el objeto tipo partitura y el objeto tipo archivo xml al activity de afinación.
                                intent.putExtra("musicSheet", musicSheet);
                                intent.putExtra("xmlFile", xmlFile);

                                // Inicia el activity.
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            // Si se indica si editar información.
                            @Override
                            public void onClick(DialogInterface dialog, int j) {
                                // Crea un intent para iniciar el activiy de editar información..
                                Intent intent = new Intent(LoadMusicSheetActivity.this, EditMusicSheetActivity.class);

                                // Manda el objeto tipo partitura y el objeto tipo archivo xml al activiy de editar información.
                                intent.putExtra("musicSheet", musicSheet);
                                intent.putExtra("xmlFile", xmlFile);
                                intent.putExtra("sampleIndex", 1);

                                // Inicia el activity.
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    /**
     * FIXME: Description of {@code onActivityResult}. Comportamiento luego de salir del intent de
     * seleccionar archivo xml.
     *
     * @param requestCode
     * @param data
     * @param resultCode
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Obtiene el resultado de seleccionar el achivo.
        super.onActivityResult(requestCode, resultCode, data);

        // Si no se seleccionó un archivo.
        if (resultCode == RESULT_CANCELED) {
        } // Si se seleccionó un archivo.
        else if ((resultCode == RESULT_OK) && (requestCode == xmlLoaded)) {
            // Obtiene los datos del archivo.
            Uri uri = data.getData();

            // Muestra mensaje indicando que el archivo se cargó exitosamente.
            Toast.makeText(LoadMusicSheetActivity.this, LoadMusicSheetActivity.this.getString(R.string.fileLoaded), Toast.LENGTH_SHORT).show();

            // Habilita el botón de validar y desactiva el de continuar.
            btnValidateXmlFile.setEnabled(true);
            btnContinue.setEnabled(false);

            // Crea array para almacenar la ruta del archivo sin "Documents:"
            String[] splitSource = DocumentsContract.getDocumentId(uri).split(":");

            // Si el archivo está en la memoria interna
            if ("primary".equalsIgnoreCase(splitSource[0]))
                xmlFile.setSource(Environment.getExternalStorageDirectory() + "/" + splitSource[1]);          // Almacena en el nodo tipo xmlFile la ruta absoluta del archivo cargardo.
            // Si el archivo está en la sd.
            else
                xmlFile.setSource(System.getenv("SECONDARY_STORAGE") + "/" + splitSource[1]);          // Almacena en el nodo tipo xmlFile la ruta absoluta del archivo cargardo.
        }
    }
}
