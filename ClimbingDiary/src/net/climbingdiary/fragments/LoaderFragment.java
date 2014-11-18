package net.climbingdiary.fragments;

import net.climbingdiary.data.DiaryDataLoader;
import net.climbingdiary.data.DiaryDbHelper;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

public abstract class LoaderFragment extends Fragment implements LoaderCallbacks<Cursor> {
  
  protected CursorAdapter mAdapter;   // adapter that will be connected to the data cursor
  protected DiaryDbHelper dbhelper;   // reference to database helper

  public LoaderFragment() {
    // keep a reference to the active database
    this.dbhelper = DiaryDbHelper.getInstance(getActivity());
  }
    
  // initialize the loader with the given code
  public void initLoader(int code) {
    getLoaderManager().initLoader(code, null, this);
  }
  
  /*****************************************************************************************************
   *                                          DATA JOB
   * Implement this function with the actual data retrieval to perform.
   *****************************************************************************************************/
  public abstract Cursor dataRetrieval();
  
  /*****************************************************************************************************
   *                                          LOADERS
   *****************************************************************************************************/
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    // create the ascents data loader
    return new DiaryDataLoader(getActivity(), dbhelper.getSource()) {
      @Override
      public Cursor doJob() {
        return dataRetrieval();
      }
    };
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
    // swap in a newly up-to-date cursor
    mAdapter.swapCursor(c);
  }

  @Override
  public void onLoaderReset(Loader<Cursor> c) {
    mAdapter.swapCursor(null);
  }
}
