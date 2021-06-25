package net.climbingdiary.fragments;

import net.climbingdiary.R;
import net.climbingdiary.activities.MainActivity;
import net.climbingdiary.activities.PlaceActivity;
import net.climbingdiary.adapters.PlacesAdapter;
import net.climbingdiary.data.DiaryDbHelper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PlacesFragment extends LoaderFragment {

    private Context myContext;

    public void onPlaceSelected(Context context, long id, String name) {
        // pass the id and name of the clicked place to the new activity
        Intent intent = new Intent(context, PlaceActivity.class);
        intent.putExtra(MainActivity.EXTRA_PLACE_ID, id);
        intent.putExtra(MainActivity.EXTRA_PLACE_NAME, name);
        startActivity(intent);
    }


  /*****************************************************************************************************
   *                                          LIFECYCLE METHODS
   *****************************************************************************************************/
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
        TextView name = (TextView) view.findViewById(R.id.place);
        onPlaceSelected(myContext, id, name.getText().toString());
      }
    });
    list_places.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // only a place with no routes can be removed: it is a safeguard
        // and also it is very complicated to eliminate all the routes and ascents
        Cursor c = dbhelper.getRoutes(id);
        if (c.moveToFirst()) {
          // warn that the place still has routes
          new Warning().show(getActivity().getSupportFragmentManager(), "warn_routes");
        } else {
          // ask confirmation
          new RemovePlace(dbhelper, id).show(getActivity().getSupportFragmentManager(), "remove_place");
        }
        return true;
      }
    });

    initLoader(MainActivity.LOADER_PLACES);
    return rootView;
  }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myContext = context;
    }



  /**********************************************************************************************************
   * A {@link DialogFragment} that warns the user about a place being still used.
   */
  public static class Warning extends DialogFragment {
    public Warning() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      setRetainInstance(true); // fix rotation bug
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder
          .setMessage(R.string.dialog_warning_place)
          .setPositiveButton(R.string.ok, null);
      // Create the AlertDialog object and return it
      return builder.create();
    }
  }

  /**********************************************************************************************************
   * A {@link DialogFragment} that ask for confirmation of place removal.
   */
  public static class RemovePlace extends DialogFragment {
    DiaryDbHelper dbhelper;
    long placeid;

    public RemovePlace(DiaryDbHelper dbhelper, long placeid) {
      this.dbhelper = dbhelper;
      this.placeid = placeid;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      setRetainInstance(true); // fix rotation bug
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder
          .setMessage(R.string.dialog_remove_place)
          .setPositiveButton(R.string.dialog_yes,
              new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                  dbhelper.removePlace(placeid);
                }
              })
          .setNegativeButton(R.string.cancel, null);
      // Create the AlertDialog object and return it
      return builder.create();
    }

    // fix for rotation bug
    @Override
    public void onDestroyView() {
      if (getDialog() != null && getRetainInstance())
        getDialog().setOnDismissListener(null);
      super.onDestroyView();
    }
  }

  /*****************************************************************************************************
   *                                          DATA RETRIEVAL
   *****************************************************************************************************/
  @Override
  public Cursor dataRetrieval() {
    return dbhelper.getPlaces();
  }

}
