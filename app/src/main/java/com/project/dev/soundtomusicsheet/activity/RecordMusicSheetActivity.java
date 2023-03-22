/*
 * @fileoverview    {RecordMusicSheetActivity}
 *
 * @version         2.0
 *
 * @author          Dyson Arley Parra Tilano <dysontilano@gmail.com>
 *
 * @copyright       Dyson Parra
 * @see             github.com/DysonParra
 *
 * History
 * @version 1.0     Implementación realizada.
 * @version 2.0     Documentación agregada.
 */
package com.project.dev.soundtomusicsheet.activity;

import com.project.dev.soundtomusicsheet.R;
import com.project.dev.soundtomusicsheet.creator.MeasureCreator;
import com.project.dev.soundtomusicsheet.eraser.MeasureEraser;
import com.project.dev.soundtomusicsheet.eraser.NoteEraser;
import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.XmlFile;
import com.project.dev.soundtomusicsheet.struct.symbol.Sound;
import com.project.dev.soundtomusicsheet.processor.AudioProcessor;
import com.project.dev.soundtomusicsheet.processor.NoteProcessor;
import com.project.dev.soundtomusicsheet.processor.SoundProcessor;
import com.project.dev.soundtomusicsheet.processor.TempoProcessor;
import com.project.dev.soundtomusicsheet.writer.MusicSheetFileWriter;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * TODO: Definición de {@code RecordMusicSheetActivity}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class RecordMusicSheetActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private Button btnRecord;
    private Button btnEditInformation;
    private Button btnEndMusicSheet;
    private CheckBox chkBoxDeleteRestAtStart;
    private Button btnShow;

    /*
     * Variables locales.
     */
    private AudioProcessor mAudioProcessor;                                     // Procesador de audio.
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();    // Usado para crear hilo de ejecución (para procesar el audio en ese hilo).
    private boolean recording = false;                                          // Si en ese momento se está grabando un sonido.

    /*
     * Variables obtenidas desde un activity anterior.
     */
    private MusicSheet musicSheet;                      // Partitura a la que se le irán agregando sonidos.
    private XmlFile xmlFile;                            // Archivo xml cargado o null si se envió desde nueva partitura.
    private int sampleIndex;                            // Subindice del archivo de muestras a guardar.
    private String action;                              // Mensaje a mostrar al iniciar el oncreate.

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_music_sheet);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        btnRecord = findViewById(R.id.btnRecord);
        btnEditInformation = findViewById(R.id.btnEditInformation);
        btnEndMusicSheet = findViewById(R.id.btnEndMusicSheet);
        chkBoxDeleteRestAtStart = findViewById(R.id.chkBoxDeleteRestAtStart);
        btnShow = findViewById(R.id.btnShow);

        // Obtiene los elementos enviados desde el activity "Tuner" o "Edit music sheet" y los asigna a variables locales.
        musicSheet = (MusicSheet) getIntent().getExtras().getSerializable("musicSheet");
        xmlFile = (XmlFile) getIntent().getExtras().getSerializable("xmlFile");
        sampleIndex = (int) getIntent().getExtras().getSerializable("sampleIndex");
        action = (String) getIntent().getExtras().getSerializable("action");

        // Indica que no es posible editar tempo, compás o terminar la partitura acabando de iniciar el activity.
        btnEditInformation.setEnabled(false);
        btnEndMusicSheet.setEnabled(false);
        chkBoxDeleteRestAtStart.setEnabled(false);
        //btnShow.setVisibility(View.INVISIBLE);

        // Mensaje al iniciar el activity.
        new AlertDialog.Builder(this)
                .setTitle(RecordMusicSheetActivity.this.getString(R.string.message))
                .setMessage(action)
                .setNegativeButton(android.R.string.ok, null).show();

        // Comportamiento del botón grabar y/o parar.
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si está grabando.
                if (recording) {
                    // Escribe en el botón que ya no está grabando y actualiza recording.
                    btnRecord.setText(RecordMusicSheetActivity.this.getString(R.string.btnRecord));
                    recording = false;

                    // Para de grabar.
                    mAudioProcessor.stop();

                    // Indica que luego de grabar un sonido ya es posible acabar la partitura y editar tempo y/o compás.
                    btnEditInformation.setEnabled(true);
                    btnEndMusicSheet.setEnabled(true);
                    chkBoxDeleteRestAtStart.setEnabled(true);
                } // Si no está grabando (empezará a grabar).
                else {
                    // Escribe en el botón que ya está grabando y actualiza recording.
                    btnRecord.setText(RecordMusicSheetActivity.this.getString(R.string.btnStop));
                    recording = true;

                    // Inicia el proceso de grabación y procesamiento de audio.
                    startAudioProcessing();

                    // Indica que mientras graba un sonido no es posible acabar la partitura y editar tempo y/o compás.
                    btnEditInformation.setEnabled(false);
                    btnEndMusicSheet.setEnabled(false);
                    chkBoxDeleteRestAtStart.setEnabled(false);

                    // Crea directorio "sound-to-music-sheet" en memoria interna.
                    File folder = new File(Environment.getExternalStorageDirectory().getPath(), "sound-to-music-sheet");
                    folder.mkdir();
                }
            }
        });

        // Comportamiento del botón editar información.
        btnEditInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un intent para iniciar el activiy de editar información..
                Intent intent = new Intent(RecordMusicSheetActivity.this, EditMusicSheetActivity.class);

                // Manda el objeto tipo partitura al activiy de editar información.
                intent.putExtra("musicSheet", musicSheet);
                intent.putExtra("xmlFile", xmlFile);
                intent.putExtra("sampleIndex", sampleIndex);

                // Inicia el activity.
                startActivity(intent);
            }
        });

        // Comportamiento del botón Terminar partitura.
        btnEndMusicSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TempoProcessor tempoProcessor = new TempoProcessor();                               // Crea instancia de procesador de tempo (para obtener el último tempo si se cargó una partitura).
                SoundProcessor soundProcessor = new SoundProcessor();                               // Crea instancia de procesador de Sonidos para analaizar cada sonido grabado.
                NoteProcessor noteProcessor = new NoteProcessor();                                  // Crea instancia de procesador de notas para procesar los nodos tipo nota del objeto de partitura.
                NoteEraser noteEraser = new NoteEraser();                                           // Crea instancia de borrador de notas para borrar al principio y/o al final según indique el usuario.
                MeasureEraser measureEraser = new MeasureEraser();                                  // Crea instancia de borrador de compases para borrar compases de separación si se cargó una partitura xml.
                MeasureCreator measureCreator = new MeasureCreator();                               // Crea instancia de creador de compases para agregar compases de separación a la partitura antes de terminarla.
                MusicSheetFileWriter writer = new MusicSheetFileWriter();                           // Crea instancia de escritor de partitura para escribir el objeto de tipo partitura en un archivo xml.

                MusicSheet sheetAux = new MusicSheet(440f);                                         // Partitura cargada (Si la hay).

                if (xmlFile != null) {                                                              // Si se cargó una partitura.
                    sheetAux = xmlFile.convertToMusicSheet();                                       // Convierte el objeto xmlFile en un objeto tipo partitura.
                    measureEraser.deleteSeparationMeasures(sheetAux);                               // Borra compases de separación de sheetAux.
                    noteProcessor.convertDivisionsToThirtyTwo(sheetAux);                            // Pasa sheetAux a divisions = 32.
                    noteProcessor.uniteTies(sheetAux);                                              // Une las notas con ligadura de sheetAux.

                    if (xmlFile.isIgnoreRestsAtEnd())                                              // Si se indicó ignorar silencios al final.
                        noteEraser.deleteRestsAtEnd(sheetAux);                                      // Borra silencios al final de sheetAux.

                    // Si el primer nodo no es un nodo tipo tempo y el segundo tampoco (Se dio no editar información) agrega tempo a la nueva partitura.
                    if (!"Tempo".equals(musicSheet.getFirst().getClass().getSimpleName()) && !"Tempo".equals(musicSheet.getFirst().getNext().getClass().getSimpleName()))
                        musicSheet.addSymbol(tempoProcessor.findLastTempo(sheetAux), musicSheet.getFirst(), false);
                }

                Log.d("printList", "01");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                soundProcessor.convertSoundsToNotes(musicSheet);                                    // Convierte los sonidos grabados en notas musicales.
                Log.d("printList", "02");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                noteProcessor.uniteNotes(musicSheet);                                               // Une las notas iguales en una misma nota.
                Log.d("printList", "03");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                if (chkBoxDeleteRestAtStart.isChecked())                                             // Si está chequeado eliminar descansos al incio.
                    noteEraser.deleteRestsAtStart(musicSheet);                                      // Borra los descansos al inicio de la partitura.

                Log.d("printList", "04");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                if (xmlFile != null)                                                                 // Si se cargó una partitura.
                    sheetAux.addMusicSheet(musicSheet);                                             // Agrega la partitura a la que se añadieron sonidos al final de sheetAux.

                Log.d("printList", "05");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                noteProcessor.divideNotes(musicSheet);                                              // Divide las notas obtenidas en n-notas con ligadura.
                Log.d("printList", "06");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                measureCreator.addSeparationMeasures(musicSheet);                                   // Agrega compases de separación a la partitura.
                Log.d("printList", "07");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                musicSheet.refreshListNumbers();                                                    // Actualiza los indices de cada nodo.
                Log.d("printList", "08");
                musicSheet.printMusicSheet();                                                       // Muestra los elementos de la lista.

                writer.writeMusicSheetToXmlFile(musicSheet);                                        // Escribe el objeto de tipo partitura en un archivo.

                Intent intent = new Intent((RecordMusicSheetActivity.this), MainActivity.class);    // Crea un intent para iniciar el activity principal
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);     // Agrega banderas indicando que se cerrará toda la pila de actividades.
                startActivity(intent);                                                              // Inicia el activity.

                // Muestra donde se guardó la partitura.
                Toast.makeText(RecordMusicSheetActivity.this, RecordMusicSheetActivity.this.getString(R.string.xmlCreated) + musicSheet.getSource(), Toast.LENGTH_LONG).show();

            }
        });

        // Comportamiento del botón mostrar.
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xmlFile == null)
                    Log.d("printList", "No xmlFile");
                else {
                    Log.d("printList", xmlFile.getSource());
                    Log.d("printList", String.valueOf(xmlFile.isIgnoreRestsAtEnd()));
                }
                musicSheet.printMusicSheet();
            }
        });
    }

    /**
     * FIXME: Definición de {@code onKeyDown}. Comportamiento del botón atrás.
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Si el botón es el de atrás.
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Crea un mensaje de alerta preguntando si desea salir sin guardar.
            new AlertDialog.Builder(this)
                    .setTitle(RecordMusicSheetActivity.this.getString(R.string.backBtn))
                    .setMessage(RecordMusicSheetActivity.this.getString(R.string.cancelRecord))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        // Si se indica si salir sin guardar.
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Crea un intent para iniciar el activity principal
                            Intent intent = new Intent(RecordMusicSheetActivity.this, MainActivity.class);

                            // Agrega banderas indicando que se cerrará toda la pila de actividades.
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            // Inicia el activity.
                            startActivity(intent);
                        }
                    }).show();
        }
        return true;
    }

    /**
     * FIXME: Definición de {@code startAudioProcessing}. Comienza con el procesamiento del audio.
     */
    private void startAudioProcessing() {

        // Crea un Procesador de audio y lo inicializa con la frecuencia de muestreo disponible.
        mAudioProcessor = new AudioProcessor(musicSheet.getA4Affination());
        mAudioProcessor.init();

        // Implementa SamplesListener.
        mAudioProcessor.setSamplesListener(new AudioProcessor.SamplesListener() {
            @Override
            // Invocado cuando detecta un sonido en SamplesListener.
            public void soundDetected(final float freq, final float duration, final float avgIntensity) {
                // Crea un Runnable que se ejecuta en el hilo principal (para que pueda editar la interfaz de usuario).
                runOnUiThread(new Runnable() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void run() {
                        // Aumenta sampleIndex.
                        sampleIndex++;

                        // Crea un nodo de tipo sound con la información obtenida del procesador de audio.
                        musicSheet.addSymbol(new Sound(freq, duration, (avgIntensity)), musicSheet.getLast(), true);
                    }
                });
            }
        });

        // Ejecuta el pocesador de audio en un nuevo hilo.
        mExecutor.execute(mAudioProcessor);
    }
}
