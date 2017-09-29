package com.vannakittikun.whatsfordinner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Rule on 9/29/2017.
 */

public class FragmentRecipe2 extends Fragment {
    TextView text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recipe, container, false);
        text = view.findViewById(R.id.textView6);

        return view;
    }

    public void changeData(){
        text.setText("fdasfads sdga sg asd");
    }
}
