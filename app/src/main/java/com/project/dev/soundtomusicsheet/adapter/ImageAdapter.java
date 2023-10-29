/*
 * @fileoverview    {ImageAdapter}
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

import com.project.dev.soundtomusicsheet.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * TODO: Description of {@code ImageAdapter}.
 *
 * @author Dyson Parra
 * @since 11
 */
public class ImageAdapter extends ArrayAdapter<String> {

    private int[] spinnerImages;                                        // Array con los indices de las imágenes.
    private Context mContext;                                           // Contexto donde se aplicará el adapter.

    /**
     * TODO: Description of {@code ImageAdapter}.
     *
     * @param context
     * @param images
     */
    public ImageAdapter(@NonNull Context context, int[] images) {
        super(context, R.layout.custom_spinner_row);                    // Inicializa con la configuración indicada en el layout indicado.
        this.spinnerImages = images;                                    // Asigna valor al array de imágenes.
        this.mContext = context;                                        // Asigna valor al contexto.
    }

    /**
     * FIXME: Description of {@code getDropDownView}. Obtiene la vista del item desplegado (al hacer
     * click en el spinner).
     *
     * @param position    Indica la posición del item.
     * @param convertView Indica el convertView del item.
     * @param parent      Indica el parent del item.
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    /**
     * FIXME: Description of {@code getCount}. Obtiene la cantidad de elementos del spinner.
     *
     * @return
     */
    @Override
    public int getCount() {
        return spinnerImages.length;                                    // Devuelve la longitud del array de imágenes.
    }

    /**
     * FIXME: Description of {@code getView}. Obtiene la vista del item (antes de dar click).
     *
     * @param position    Indica la posición del item.
     * @param convertView Indica el convertView del item.
     * @param parent      Indica el parent del item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.custom_spinner_row, parent, false);

            mViewHolder.mFlag = convertView.findViewById(R.id.spinnerImage);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        mViewHolder.mFlag.setImageResource(spinnerImages[position]);

        return convertView;
    }

    /**
     * FIXME: Description of {@code ViewHolder}. Obtiene el ViewHolder del spinner.
     */
    private static class ViewHolder {

        ImageView mFlag;
    }
}
