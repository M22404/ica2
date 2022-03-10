package com.example.ica2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ListViewAdapter(context: Context, resource: Int, items: List<Location>) :
    ArrayAdapter<Location>(context, resource, items) {
    private var resourceLayout = resource;
    private var mContext = context;

    @Override
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = if (convertView == null) {
            val inflater = LayoutInflater.from(mContext);
            inflater.inflate(resourceLayout, null);
        } else {
            convertView
        }

        val p = getItem(position);

        if (p != null) {
            v.findViewById<TextView>(R.id.card_text_view)?.text = p.description
        }

        return v;
    }

}