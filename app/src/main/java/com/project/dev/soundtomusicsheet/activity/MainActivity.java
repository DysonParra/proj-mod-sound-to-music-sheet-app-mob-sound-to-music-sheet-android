/*
 * @fileoverview {MainActivity} se encarga de realizar tareas especificas.
 *
 * @version             1.0
 *
 * @author              Dyson Arley Parra Tilano <dysontilano@gmail.com>
 * Copyright (C) Dyson Parra
 *
 * @History v1.0 --- La implementacion de {MainActivity} fue realizada el 31/07/2022.
 * @Dev - La primera version de {MainActivity} fue escrita por Dyson A. Parra T.
 */
package com.project.dev.soundtomusicsheet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.project.dev.soundtomusicsheet.R;

/**
 * TODO: Definición de {@code MainActivity}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class MainActivity extends AppCompatActivity {

    /*
     * Variables asociadas con elementos la vista.
     */
    private Button btnNewMusicSheet;
    private Button btnEditMusicSheet;
    private Button btnExit;

    /**
     * Invocado cuando se crea el activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Crea instancia del activity y la asocia con la vista.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Asocia variables locales con elementos de la vista.
        btnNewMusicSheet = findViewById(R.id.btnNewMusicSheet);
        btnEditMusicSheet = findViewById(R.id.btnEditMusicSheet);
        btnExit = findViewById(R.id.btnExit);

        // Comportamiento del botón nueva partitura.
        btnNewMusicSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un intent para iniciar el activity de nueva partitura.
                Intent intent = new Intent(MainActivity.this, NewMusicSheetActivity.class);

                // Inicia el activit
                startActivity(intent);
            }
        });

        // Comportamiento del botón editar partitura.
        btnEditMusicSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un intent para iniciar el activity de cargar partitura.
                Intent intent = new Intent(MainActivity.this, LoadMusicSheetActivity.class);

                // Inicia el activity.
                startActivity(intent);
            }
        });

        // Comportamiento del botón salir.
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sale del programa.
                finish();
            }
        });
    }
}
