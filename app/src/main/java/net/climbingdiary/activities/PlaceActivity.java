package net.climbingdiary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import net.climbingdiary.R;
import net.climbingdiary.adapters.TabAdapter;
import net.climbingdiary.data.DiaryContract;
import net.climbingdiary.data.DiaryDbHelper;
import net.climbingdiary.fragments.PlaceRoutesFragment;

public class PlaceActivity extends TabbedActivity {

    private long place_id;            // ID of the climbing place
    private String place_name;        // name of the climbing place

    /*****************************************************************************************************
     *                                          LIFECYCLE METHODS
     *****************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // retrieve the entry information to display in the title
        place_id = getIntent().getLongExtra(MainActivity.EXTRA_PLACE_ID,0);
        place_name = getIntent().getStringExtra(MainActivity.EXTRA_PLACE_NAME);

        // package some extra data in the bundle
        Bundle data = new Bundle();
        data.putLong(MainActivity.EXTRA_PLACE_ID, place_id);
        data.putString(MainActivity.EXTRA_PLACE_NAME, place_name);

        // create the pager adapter and initialize the layout
        mAdapter = new TabAdapter(getSupportFragmentManager(), this, data,
            new int[] { R.string.section_routes }) {
                @Override
                public Fragment getItem(int i) {
                  Fragment proutes = new PlaceRoutesFragment();
                  proutes.setArguments(data);
                  return proutes;
                }
            };
        super.onCreate(savedInstanceState);

        // set title
        actionBar.setTitle(place_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() ==  R.id.action_edit) {
            // allow to change the name of the place
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.edit_place);

            // Set up the input
            final EditText input = new EditText(this);
            input.setText(place_name);

            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String new_name = input.getText().toString();
                    if (!new_name.equals(place_name)) {
                        // update the name of the place
                        DiaryContract.Places.Data info = new DiaryContract.Places.Data();
                        info._id = place_id;
                        info.name = new_name;
                        DiaryDbHelper.getInstance(getBaseContext()).updatePlace(info);
                        actionBar.setTitle(new_name);
                        place_name = new_name;
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
