package com.parse.starter.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.Transform;
import com.parse.starter.R;
import com.parse.starter.RowItem;

import java.util.List;

/**
 * Created by Helena on 4/23/2015.
 */
public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId, List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtIme;
        TextView txtPasmina;
        TextView txtVrijeme;
        TextView txtMjesto;
    }

    @Override
    public int getPosition(RowItem item) {
        return super.getPosition(item);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_lost, null);
            holder = new ViewHolder();
            holder.txtIme = (TextView) convertView.findViewById(R.id.listaIme);
            holder.txtPasmina = (TextView) convertView.findViewById(R.id.listaPasmina);
            holder.txtMjesto = (TextView) convertView.findViewById(R.id.listaMjesto);
            holder.txtVrijeme = (TextView) convertView.findViewById(R.id.listaVrijeme);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);


        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtIme.setText(rowItem.getIme());
        holder.txtPasmina.setText(rowItem.getPasmina());
        holder.txtMjesto.setText(rowItem.getMjesto());
        holder.txtVrijeme.setText(rowItem.getVrijeme());

        Ion.with(holder.imageView)
                .transform((new Transform()
                {

                    @Override
                    public Bitmap transform(Bitmap source) {
                        int size = Math.min(source.getWidth(), source.getHeight());

                        int x = (source.getWidth() - size) / 2;
                        int y = (source.getHeight() - size) / 2;

                        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                        if (squaredBitmap != source) {
                            source.recycle();
                        }

                        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                        Canvas canvas = new Canvas(bitmap);
                        Paint paint = new Paint();
                        BitmapShader shader = new BitmapShader(squaredBitmap,
                                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                        paint.setShader(shader);
                        paint.setAntiAlias(true);

                        float r = size / 2f;
                        canvas.drawCircle(r, r, r, paint);

                        squaredBitmap.recycle();
                        return bitmap;
                    }

                    @Override
                    public String key() {
                        return "circle";
                    }
                }))
                .load(rowItem.getImageId());

        return convertView;
    }

}
