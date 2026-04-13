package com.example.ise;

import android.content.Context;
import android.view.*;
import android.widget.*;

public class LabAdapter extends BaseAdapter {

    Context context;

    String[] labNames = {
            "Java Programming Lab", "Advanced Database System Lab", "Database Engineering Lab",
            "R Programming Lab", "Project Lab", "Research Lab",
            "Web Technology Lab", "Python Programming Lab", "C Programming Lab",
            "System Programming Lab", "Mobile Application Development Lab", "C++ Programming Lab"
    };

    int[] images = {
            R.drawable.lab1,R.drawable.lab2,R.drawable.lab3,
            R.drawable.lab4,R.drawable.lab5,R.drawable.lab6,
            R.drawable.lab7,R.drawable.lab8,R.drawable.lab9,
            R.drawable.lab10,R.drawable.lab11,R.drawable.lab12
    };

    LabAdapter(Context c){
        context = c;
    }

    public int getCount(){
        return images.length;
    }

    public Object getItem(int position){
        return null;
    }

    public long getItemId(int position){
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lab_card,null);

        ImageView img = view.findViewById(R.id.labImage);
        TextView txt = view.findViewById(R.id.labText);

        img.setImageResource(images[position]);
        txt.setText(labNames[position]);

        return view;
    }
}