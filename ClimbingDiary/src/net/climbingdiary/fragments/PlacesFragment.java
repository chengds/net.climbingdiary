package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.activities.PlaceActivity;
import net.climbingdiary.adapters.PlacesAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PlacesFragment extends LoaderFragment {
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    // create the layout, the list of climbing places
    View rootView = inflater.inflate(R.layout.fragment_places, container, false);

    // connect the list view with the adapter containing places and visits
    final ListView list_places = (ListView) rootView.findViewById(R.id.list_places);
    mAdapter = new PlacesAdapter(getActivity(), null, 0, R.layout.item_places);
    list_places.setAdapter(mAdapter);

    // setup callbacks
    list_places.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // pass the id and name of the clicked place to the new activity
        Intent intent = new Intent(getActivity(), PlaceActivity.class);
        intent.putExtra(MainActivity.EXTRA_PLACE_ID, id);
        TextView name = (TextView) view.findViewById(R.id.place);
        intent.putExtra(MainActivity.EXTRA_PLACE_NAME, name.getText().toString());
        startActivity(intent);
      }
    });
    
    initLoader(MainActivity.LOADER_PLACES);
    return rootView;
  }
  
  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getPlaces();
  }

}
