/*
 * @fileoverview    {TunerActivity}
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
import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.XmlFile;
import com.project.dev.soundtomusicsheet.processor.AudioProcessor;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;

/**
 * TODO: Definición de {@code TunerActivity}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class TunerActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private Button btnRecord;
    private Button btnNext;
    private TextView editTextVolume;
    private XYPlot plot;

    /*
     * Variables locales.
     */
    private AudioProcessor mAudioProcessor;                                         // Procesador de audio.
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();        // Usado para crear hilo de ejecución (para procesar el audio en ese hilo).
    private boolean recording = false;                                              // Si en ese momento se está grabando un sonido.
    private Number[] volumeSamples = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Volúmenes de cada muestra de audio.
    private XYSeries volumes;                                                       // Serie de valores que se agregarán al gráfico.
    private LineAndPointFormatter volumesFormat;                                    // Formato de la línea del gráfico.
    private float maxVolume = 0.0f;                                                 // Mayor valor de volumen obtenido.

    /*
     * Variables obtenidas desde un activity anterior.
     */
    private MusicSheet musicSheet;                      // Partitura a la que se le asignará el volumen mínimo.
    private XmlFile xmlFile;                            // Archivo xml cargado o null si se envió desde nueva partitura.

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        btnRecord = findViewById(R.id.btnRecord);
        btnNext = findViewById(R.id.btnNext);
        editTextVolume = findViewById(R.id.editTextVolume);
        plot = findViewById(R.id.plot);

        // Obtiene los elementos enviados desde el activity "new music sheet", "load music sheet" o "edit music sheet" y los asigna a variables locales.
        musicSheet = (MusicSheet) getIntent().getExtras().getSerializable("musicSheet");
        xmlFile = (XmlFile) getIntent().getExtras().getSerializable("xmlFile");

        // Inicialización y creación del gráfico.
        volumes = new SimpleXYSeries(Arrays.asList(volumeSamples), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Volume Samples");
        volumesFormat = new LineAndPointFormatter(this, R.xml.plot_format);
        volumesFormat.setInterpolationParams(new CatmullRomInterpolator.Params(5, CatmullRomInterpolator.Type.Centripetal));
        plot.addSeries(volumes, volumesFormat);

        // Comportamiento del botón grabar y/o parar.
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si está grabando.
                if (recording) {
                    // Escribe en el botón que ya no está grabando y actualiza recording.
                    btnRecord.setText(TunerActivity.this.getString(R.string.btnRecord));
                    recording = false;

                    // Para de grabar.
                    mAudioProcessor.stop();
                } // Si no está grabando (empezará a grabar).
                else {
                    // Escribe en el botón que ya está grabando y actualiza recording.
                    btnRecord.setText(TunerActivity.this.getString(R.string.btnStop));
                    recording = true;

                    // Reinicia el máximo volumen encontrado.
                    maxVolume = 0.0f;

                    // Inicia el proceso de grabación y procesamiento de audio.
                    startAudioProcessing();
                }
            }
        });

        // Comportamiento del botón continuar.
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Almacena el volumen escrito por el usuario o 0 si está vacío o es ".".
                if ("".equals(editTextVolume.getText().toString()) || ".".equals(editTextVolume.getText().toString()))
                    musicSheet.setMinVolume(0);
                else
                    musicSheet.setMinVolume(Float.valueOf(editTextVolume.getText().toString()));

                // Si el volumen es muy alto, escribió "." o no escribió ninguno muestra error.
                if ("".equals(editTextVolume.getText().toString()) || ".".equals(editTextVolume.getText().toString()) || musicSheet.getMinVolume() > 99)
                    Toast.makeText(TunerActivity.this, TunerActivity.this.getString(R.string.volumeError), Toast.LENGTH_SHORT).show();
                // Si el volumen es válido.
                else {
                    // Crea un intent para iniciar el activity de grabación de sonidos.
                    Intent intent = new Intent(TunerActivity.this, RecordMusicSheetActivity.class);

                    // Manda el objeto tipo partitura al nuevo activiy y mensaje de confirmación de creación.
                    intent.putExtra("musicSheet", musicSheet);
                    intent.putExtra("xmlFile", xmlFile);
                    intent.putExtra("sampleIndex", 1);
                    intent.putExtra("action", TunerActivity.this.getString(R.string.musicSheetCreated));

                    // Agrega banderas indicando que se cerrará toda la pila de actividades.
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Inicia el activity.
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * FIXME: Definición de {@code startAudioProcessing}. Comienza con el procesamiento del audio.
     */
    private void startAudioProcessing() {

        // Crea un Procesador de audio y lo inicializa con la frecuencia de muestreo disponible.
        mAudioProcessor = new AudioProcessor();
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
                        // Si el volumen obtenido es el mayor volumen obtenido lo almacena en maxVolume.
                        if (avgIntensity > maxVolume)
                            maxVolume = avgIntensity;

                        // Hace shift a la izquierda una posición a las muestras de volumen obtenidas.
                        System.arraycopy(volumeSamples, 1, volumeSamples, 0, volumeSamples.length - 1);

                        // Almacena la nueva intensidad obtenida y la agrega al gráfico.
                        volumeSamples[volumeSamples.length - 1] = avgIntensity;
                        plot.removeSeries(volumes);
                        volumes = new SimpleXYSeries(Arrays.asList(volumeSamples), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, TunerActivity.this.getString(R.string.txtMaxVolume).concat(String.format(" %.02f%%", maxVolume)));
                        plot.addSeries(volumes, volumesFormat);
                        plot.redraw();
                    }
                });
            }
        });

        // Ejecuta el pocesador de audio en un nuevo hilo.
        mExecutor.execute(mAudioProcessor);
    }
}
