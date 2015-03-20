package net.climbingdiary.dialogs;

import net.climbingdiary.R;
import net.climbingdiary.data.DiaryDbHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DeleteAscentDialogFragment  extends DialogFragment {
  
  private DiaryDbHelper dbhelper;
  private long ascent_id;

  public DeleteAscentDialogFragment(DiaryDbHelper dbhelper, long ascent_id) {
    this.dbhelper = dbhelper;
    this.ascent_id = ascent_id;
  }
  
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    setRetainInstance(true); // fix rotation bug
    
    // create the dialog window
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder
    .setMessage(R.string.dialog_delete_ascent)
    .setPositiveButton(R.string.dialog_yes,
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
          dbhelper.deleteAscent(ascent_id);
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
