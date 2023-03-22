/*
 * @fileoverview    {EditMusicSheetActivity}
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
import com.project.dev.soundtomusicsheet.adapter.DisabledAdapter;
import com.project.dev.soundtomusicsheet.adapter.ImageAdapter;
import com.project.dev.soundtomusicsheet.creator.MeasureCreator;
import com.project.dev.soundtomusicsheet.creator.TempoCreator;
import com.project.dev.soundtomusicsheet.struct.MusicSheet;
import com.project.dev.soundtomusicsheet.struct.symbol.MusicSymbol;
import com.project.dev.soundtomusicsheet.struct.XmlFile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * TODO: Definición de {@code EditMusicSheetActivity}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class EditMusicSheetActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private RadioButton radioBtnMeasureYes;
    private RadioButton radioBtnMeasureNo;
    private RelativeLayout relativeLayoutMeasure;
    private Spinner spnNoteQuantity;
    private Spinner spnNoteType;
    private Spinner spnKey;
    private Spinner spnKeyLine;
    private Spinner spnTranspose;
    private Spinner spnSignature;
    private RadioButton radioBtnTempoYes;
    private RadioButton radioBtnTempoNo;
    private Spinner spnTempo;
    private EditText editTextTempo;
    private Button btnApplyChanges;

    /*
     * Variables locales.
     */
    private short tempo;                                // Tempo de la partitura.
    private DisabledAdapter adapter;                    // Usada para convertir un spinner en desabilitable.

    /*
     * Variables obtenidas desde un activity anterior.
     */
    private MusicSheet musicSheet;                      // Partitura a la que se le editará tempo, compás o ambos.
    private XmlFile xmlFile;                            // Archivo xml cargado o null si se envió desde nueva partitura.
    private int sampleIndex;                            // Subindice del archivo de muestras a guardar.

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_music_sheet);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        radioBtnMeasureYes = findViewById(R.id.radioBtnMeasureYes);
        radioBtnMeasureNo = findViewById(R.id.radioBtnMeasureNo);
        relativeLayoutMeasure = findViewById(R.id.relativeLayoutMeasure);
        spnNoteQuantity = findViewById(R.id.spnNoteQuantity);
        spnNoteType = findViewById(R.id.spnNoteType);
        spnKey = findViewById(R.id.spnKey);
        spnKeyLine = findViewById(R.id.spnKeyLine);
        spnTranspose = findViewById(R.id.spnTranspose);
        spnSignature = findViewById(R.id.spnSignature);
        radioBtnTempoYes = findViewById(R.id.radioBtnTempoYes);
        radioBtnTempoNo = findViewById(R.id.radioBtnTempoNo);
        spnTempo = findViewById(R.id.spnTempo);
        editTextTempo = findViewById(R.id.editTextTempo);
        btnApplyChanges = findViewById(R.id.btnStart);

        // Convierte el spinner con la línea de la clave en desabilitable.
        adapter = DisabledAdapter.createFromResource(this, R.array.keyLine);
        spnKeyLine.setAdapter(adapter);

        // Agrega las imágenes al Spinner con la figura del tempo.
        int[] spinnerImages = new int[]{R.drawable.half_note, R.drawable.quarter_note, R.drawable.eighth_note};
        ImageAdapter mCustomAdapter = new ImageAdapter(EditMusicSheetActivity.this, spinnerImages);
        spnTempo.setAdapter(mCustomAdapter);

        // Inicializa los spinner en las posiciones indicadas.
        spnNoteQuantity.setSelection(2);
        spnKeyLine.setSelection(1);
        spnTempo.setSelection(1);

        // Obtiene los elementos enviados desde el activity "Record music sheet" o "Load music sheet" y los asigna a variables locales.
        musicSheet = (MusicSheet) getIntent().getExtras().getSerializable("musicSheet");
        xmlFile = (XmlFile) getIntent().getExtras().getSerializable("xmlFile");
        sampleIndex = (int) getIntent().getExtras().getSerializable("sampleIndex");

        // Comportamiento de los radioButtons.
        // Si selecciona editar compas habilita todos los elementos de compás.
        radioBtnMeasureYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < relativeLayoutMeasure.getChildCount(); i++) {
                    View child = relativeLayoutMeasure.getChildAt(i);
                    child.setEnabled(true);
                }
            }
        });

        // Si selecciona no editar compas deshabilita todos los elementos de compas.
        radioBtnMeasureNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < relativeLayoutMeasure.getChildCount(); i++) {
                    View child = relativeLayoutMeasure.getChildAt(i);
                    child.setEnabled(false);
                }
            }
        });

        // Si selecciona editar tempo habilita los elementos del tempo.
        radioBtnTempoYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnTempo.setEnabled(true);
                editTextTempo.setEnabled(true);
            }
        });

        // Si selecciona no editar tempo deshabilita los elementos del tempo.
        radioBtnTempoNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnTempo.setEnabled(false);
                editTextTempo.setEnabled(false);
            }
        });

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

        // Comportamiento del boton de aplicar cambios.
        btnApplyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si dio aplicar cambios pero seleccionó no cambiar compás ni tempo muestra error.
                if (radioBtnMeasureNo.isChecked() && radioBtnTempoNo.isChecked())
                    Toast.makeText(EditMusicSheetActivity.this, EditMusicSheetActivity.this.getString(R.string.editMeasureOrTempoError), Toast.LENGTH_SHORT).show();
                // Si no seleccionó en no cambiar tempo y no cambiar compás.
                else {
                    // Almacena el tempo escrito por el usuario o 0 si está vacío.
                    if ("".equals(editTextTempo.getText().toString()))
                        tempo = 0;
                    else
                        tempo = Short.valueOf(editTextTempo.getText().toString());

                    // Si el tempo es muy alto o muy bajo y se seleccionó cambiar tempo muestra error.
                    if ((tempo < 10 || tempo > 210) && radioBtnTempoYes.isChecked())
                        Toast.makeText(EditMusicSheetActivity.this, EditMusicSheetActivity.this.getString(R.string.tempoError), Toast.LENGTH_SHORT).show();
                    // Caso contario guarda el tempo y el compás.
                    else {
                        MeasureCreator measureCreator = new MeasureCreator();                           // Crea instancia de creador de compás.
                        TempoCreator tempoCreator = new TempoCreator();                                 // Crea instancia de creador de tempo.

                        // Crea un nodo de tipo compás con la información seleccionada por el usuario.
                        MusicSymbol newMeasure = measureCreator.createMeasureFromActivity(spnNoteQuantity.getSelectedItem().toString(), spnNoteType.getSelectedItem().toString(),
                                spnKey.getSelectedItemId(), spnKeyLine.getSelectedItemId(), spnTranspose.getSelectedItem().toString(), spnSignature.getSelectedItemId());

                        // Crea un nodo de tipo tempo con la información seleccionada por el usuario.
                        MusicSymbol newTempo = tempoCreator.createTempoFromActivity(spnTempo.getSelectedItemId(), tempo);

                        // Si se seleccionó cambiar compás.
                        if (radioBtnMeasureYes.isChecked()) {
                            // Si se seleccionó cambiar compás pero no tempo.
                            if (radioBtnTempoNo.isChecked())
                                musicSheet.addSymbol(newMeasure, musicSheet.getLast(), true);   // Agrega el compás indicado por el usuario a la partitura como objeto tipo measure.
                            // Si se seleccionó cambiar tempo y compás.
                            else {
                                musicSheet.addSymbol(newMeasure, musicSheet.getLast(), true);   // Agrega el compás indicado por el usuario a la partitura como objeto tipo measure.
                                musicSheet.addSymbol(newTempo, musicSheet.getLast(), true);   // Agrega el tempo indicado por el usuario a la partitura como objeto tipo tempo.
                            }

                        } // Si se seleccionó no cambiar compás y se sabe que no puede estar en
                        // "no cambiar" tempo y "no cambiar" compás entonces solo se cambiará el tempo.
                        else
                            musicSheet.addSymbol(newTempo, musicSheet.getLast(), true);         // Agrega el tempo indicado por el usuario a la partitura como objeto tipo tempo.

                        // Si se ingresó al activity luego de dar cargar partitura.
                        if (sampleIndex == 1) {
                            // Crea un intent para iniciar el activiy de afinación.
                            Intent intent = new Intent(EditMusicSheetActivity.this, TunerActivity.class);

                            // Manda el objeto tipo partitura al nuevo activiy.
                            intent.putExtra("musicSheet", musicSheet);
                            intent.putExtra("xmlFile", xmlFile);

                            // Inicia el activity.
                            startActivity(intent);

                        } // Si se ingresó al activity luego de dar editar información.
                        else {
                            // Crea un intent para iniciar el activiy de grabación de sonidos.
                            Intent intent = new Intent(EditMusicSheetActivity.this, RecordMusicSheetActivity.class);

                            // Manda el objeto tipo partitura al nuevo activiy y mensaje de confirmación de cambios.
                            intent.putExtra("musicSheet", musicSheet);
                            intent.putExtra("xmlFile", xmlFile);
                            intent.putExtra("sampleIndex", sampleIndex);
                            intent.putExtra("action", EditMusicSheetActivity.this.getString(R.string.musicSheetEdited));

                            // Inicia el activity.
                            startActivity(intent);

                            // Agrega banderas indicando que se cerrará toda la pila de actividades.
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            // Cierra la ventana actual.
                            EditMusicSheetActivity.this.finish();
                        }
                    }
                }
            }
        });
    }
}
