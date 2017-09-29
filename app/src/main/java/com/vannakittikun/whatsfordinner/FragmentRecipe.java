package com.vannakittikun.whatsfordinner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Rule on 9/29/2017.
 */

public class FragmentRecipe extends Fragment {
    ListView list;
    Communicator communicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recipe, container, false);
        list = view.findViewById(R.id.listView);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.types, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        list.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        return view;
    }

    public void setCommunicator(Communicator communicator){
        this.communicator = communicator;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
        communicator.respond(i);
    }

    public interface Communicator {
        public void respond(int index);
    }
}
