/*
 * @fileoverview {FileName} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {FileName} fue realizada el 31/07/2022.
 * @Dev - La primera version de {FileName} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.soundtomusicsheet.activity;

import com.project.dev.soundtomusicsheet.R;
import com.project.dev.soundtomusicsheet.adapter.DisabledAdapter;
import com.project.dev.soundtomusicsheet.adapter.ImageAdapter;
import com.project.dev.soundtomusicsheet.creator.MeasureCreator;
import com.project.dev.soundtomusicsheet.creator.TempoCreator;
import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;

import java.io.Serializable;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * TODO: Definición de {@code NewMusicSheetActivity}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class NewMusicSheetActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private EditText editTextA4;
    private Spinner spnNoteQuantity;
    private Spinner spnNoteType;
    private Spinner spnKey;
    private Spinner spnKeyLine;
    private Spinner spnTranspose;
    private Spinner spnSignature;
    private Spinner spnTempo;
    private EditText editTextTempo;
    private Button btnStart;

    /*
     * Variables locales.
     */
    private float A4;                                   // Indica la afinación de La4.
    private short tempo;                                // Indica el tempo de la partitura.
    private DisabledAdapter adapter;                    // Usada para convertir un spinner en desabilitable.

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_music_sheet);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        editTextA4 = findViewById(R.id.editTextA4);
        spnNoteQuantity = findViewById(R.id.spnNoteQuantity);
        spnNoteType = findViewById(R.id.spnNoteType);
        spnKey = findViewById(R.id.spnKey);
        spnKeyLine = findViewById(R.id.spnKeyLine);
        spnTranspose = findViewById(R.id.spnTranspose);
        spnSignature = findViewById(R.id.spnSignature);
        spnTempo = findViewById(R.id.spnTempo);
        editTextTempo = findViewById(R.id.editTextTempo);
        btnStart = findViewById(R.id.btnStart);

        // Convierte el spinner con la línea de la clave en desabilitable.
        adapter = DisabledAdapter.createFromResource(this, R.array.keyLine);
        spnKeyLine.setAdapter(adapter);

        // Agrega las imágenes al Spinner con la figura del tempo.
        int[] spinnerImages = new int[]{R.drawable.half_note, R.drawable.quarter_note, R.drawable.eighth_note};
        ImageAdapter mCustomAdapter = new ImageAdapter(NewMusicSheetActivity.this, spinnerImages);
        spnTempo.setAdapter(mCustomAdapter);

        // Inicializa los spinner en las posiciones indicadas.
        spnNoteQuantity.setSelection(2);
        spnKeyLine.setSelection(1);
        spnTempo.setSelection(1);

        // Comportamiento del spinner con la clave.
        spnKey.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {                                    // Evalúa la clave.
                    case 0:                                     // G (Sol).
                        if (spnKeyLine.getSelectedItemId() > 1) // Si la línea de clave es mayor a 2.
                            spnKeyLine.setSelection(0);         // Pone en la línea 1.
                        adapter.enableItem((0));                // Habilita línea 1.
                        adapter.enableItem((1));                // Habilita línea 2.
                        adapter.disableItem((2));               // Deshabilita línea 3.
                        adapter.disableItem((3));               // Deshabilita línea 4.
                        adapter.disableItem((4));               // Deshabilita línea 5.
                        break;                                  // Sale del case.

                    case 1:                                     // F (Fa).
                        if (spnKeyLine.getSelectedItemId() < 2) // Si la línea de clave es menor a 3.
                            spnKeyLine.setSelection(2);         // Pone en la línea 3.
                        adapter.disableItem((0));               // Deshabilita línea 1.
                        adapter.disableItem((1));               // Deshabilita línea 2.
                        adapter.enableItem((2));                // Habilita línea 3.
                        adapter.enableItem((3));                // Habilita línea 4.
                        adapter.enableItem((4));                // Habilita línea 5.
                        break;                                  // Sale del case.

                    case 2:                                     // C (Do)
                        adapter.enableItem((0));                // Habilita línea 1.
                        adapter.enableItem((1));                // Habilita línea 2.
                        adapter.enableItem((2));                // Habilita línea 3.
                        adapter.enableItem((3));                // Habilita línea 4.
                        adapter.enableItem((4));                // Habilita línea 5.
                        break;                                  // Sale del case.
                }
            }

            // Invocado cuando no se selecciona ningún elemento (No necesario).
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Comportamiento del spinner con la línea de la clave.
        spnKeyLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Si la clave es G2 ó F4 activa el spinner de transporte de por octavas.
                if ((spnKey.getSelectedItemId() == 0 && i == 1) || (spnKey.getSelectedItemId() == 1 && i == 3)) {
                    spnTranspose.setEnabled(true);
                    spnTranspose.setClickable(true);
                } // Caso contrario desactiva el spinner.
                else {
                    spnTranspose.setEnabled(false);
                    spnTranspose.setClickable(false);
                    spnTranspose.setSelection(0);
                }
            }

            // Invocado cuando no se selecciona ningún elemento (No necesario).
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Comportamiento del botón comenzar.
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // A A4 le lleva la afinación escrita por el usuario o 0 si está vacía o es ".".
                if ("".equals(editTextA4.getText().toString()) || ".".equals(editTextA4.getText().toString()))
                    A4 = 0.0f;
                else
                    A4 = Float.valueOf(editTextA4.getText().toString());

                // Almacena el tempo escrito por el usuario o 0 si está vacío.
                if ("".equals(editTextTempo.getText().toString()))
                    tempo = 0;
                else
                    tempo = Short.valueOf(editTextTempo.getText().toString());

                // Si la afinació de A4 es muy alta o muy baja, y si el tempo es muy alto o muy bajo muestra error,
                if (A4 < 30 || A4 > 3520)
                    Toast.makeText(NewMusicSheetActivity.this, NewMusicSheetActivity.this.getString(R.string.A4Error), Toast.LENGTH_SHORT).show();
                else if (tempo < 10 || tempo > 210)
                    Toast.makeText(NewMusicSheetActivity.this, NewMusicSheetActivity.this.getString(R.string.tempoError), Toast.LENGTH_SHORT).show();
                // Caso contario pasa a la siguiente ventana.
                else {
                    MeasureCreator measureCreator = new MeasureCreator();                           // Crea instancia de creador de compás.
                    TempoCreator tempoCreator = new TempoCreator();                                 // Crea instancia de creador de tempo.

                    // Crea un nodo de tipo compas con la información seleccionada por el usuario.
                    MusicSymbol firstMeasure = measureCreator.createMeasureFromActivity(spnNoteQuantity.getSelectedItem().toString(), spnNoteType.getSelectedItem().toString(),
                            spnKey.getSelectedItemId(), spnKeyLine.getSelectedItemId(), spnTranspose.getSelectedItem().toString(), spnSignature.getSelectedItemId());

                    // Crea un nodo de tipo tempo con la información seleccionada por el usuario.
                    MusicSymbol firstTempo = tempoCreator.createTempoFromActivity(spnTempo.getSelectedItemId(), tempo);

                    // Crea el objeto de tipo partitura y agrega compás y tempo.
                    MusicSheet musicSheet = new MusicSheet(A4);                                     // Crea un objeto de tipo partitura con la afinación de A4 escrita por el usuario.
                    musicSheet.addSymbol(firstMeasure, musicSheet.getLast(), (true));              // Agrega el compás indicado por el usuario a la partitura como objeto tipo measure.
                    musicSheet.addSymbol(firstTempo, musicSheet.getLast(), (true));              // Agrega el tempo indicado por el usuario a la partitura como objeto tipo tempo.

                    // Crea un intent para iniciar el activiy de afinación.
                    Intent intent = new Intent(NewMusicSheetActivity.this, TunerActivity.class);

                    // Manda el objeto tipo partitura al nuevo activiy y null en el obbjeto de tipo archivo xml (No se cargó una partitura).
                    intent.putExtra("musicSheet", musicSheet);
                    intent.putExtra("xmlFile", (Serializable) null);

                    // Inicia el activity.
                    startActivity(intent);
                }
            }
        });
    }
}
