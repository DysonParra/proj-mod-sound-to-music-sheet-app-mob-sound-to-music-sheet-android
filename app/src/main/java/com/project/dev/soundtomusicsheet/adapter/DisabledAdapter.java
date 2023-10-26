/*
 * @fileoverview    {DisabledAdapter}
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
package com.project.dev.soundtomusicsheet.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * TODO: Definición de {@code DisabledAdapter}.
 *
 * @author Dyson Parra
 * @since 1.8
 */
public class DisabledAdapter extends ArrayAdapter<CharSequence> {

    private boolean enabledItems[];                                 // Atributos para adapter de desabilitable.

    /**
     * TODO: Definición de {@code DisabledAdapter}.
     *
     * @param context
     * @param strings
     * @param textViewResId
     */
    private DisabledAdapter(Context context, int textViewResId, CharSequence[] strings) {
        super(context, textViewResId, strings);
        this.enabledItems = new boolean[strings.length];

        for (int i = 0; i < enabledItems.length; i++)
            enabledItems[i] = true;
    }

    /**
     * FIXME: Definición de {@code createFromResource}. Crea un adapter desabilitable en base a un
     * array en values.xml
     *
     * @param context        Indica el contexto donde se creará el adapter.
     * @param textArrayResId Indica el id del array en values.
     * @return
     */
    public static DisabledAdapter createFromResource(Context context, int textArrayResId) {
        Resources resources = context.getResources();
        CharSequence[] strings = resources.getTextArray(textArrayResId);
        return new DisabledAdapter(context, android.R.layout.simple_spinner_item, strings);
    }

    /**
     * FIXME: Definición de {@code areAllItemsEnabled}. Devuelve si tiene todos los items
     * habilitados.
     *
     * @return
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;                                                   // Devuelve que todos los items no están habilitados.
    }

    /**
     * FIXME: Definición de {@code isEnabled}. Obtiene si el item está habilitado.
     *
     * @param position Indica la posición del item.
     * @return
     */
    @Override
    public boolean isEnabled(int position) {
        return enabledItems[position];                                  // Devuelve el array de items habilitados en la posición indicada.
    }

    /**
     * FIXME: Definición de {@code getDropDownView}. Obtiene la vista del item desplegado (al hacer
     * click en el spinner).
     *
     * @param position    Indica la posición del item.
     * @param convertView Indica el convertView del item.
     * @param parent      Indica el parent del item.
     */
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);

        TextView text = (TextView) view;

        if (isEnabled(position))
            text.setTextColor(Color.BLACK);
        else
            text.setTextColor(Color.GRAY);

        return view;
    }

    /**
     * FIXME: Definición de {@code enableItem}. Habilita el item indicado.
     *
     * @param position Indica la posición del item.
     */
    public void enableItem(int position) {
        if (position < enabledItems.length)
            enabledItems[position] = true;
    }

    /**
     * FIXME: Definición de {@code disableItem}. Deshabilita el item indicado.
     *
     * @param position Indica la posición del item.
     */
    public void disableItem(int position) {
        if (position < enabledItems.length)
            enabledItems[position] = false;
    }
}
