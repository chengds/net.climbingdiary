package net.climbingdiary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.v4.content.AsyncTaskLoader;

/*
 * This class is modeled after CursorLoader, but instead of querying
 * a content provider, it connects to our database through cursors.
 * 
 * Additionally, to make clients aware of database changes, we need
 * to observe the helper and receive notifications of changes.
 */
public abstract class DiaryDataLoader extends AsyncTaskLoader<Cursor> {

  private DataSetObserver mObserver;
  private DataSetObservable source;
  private Cursor mCursor;
  
  public DiaryDataLoader(Context context, DataSetObservable source) {
    super(context);
    this.source = source;
  }
  
  // the actual job to perform
  public abstract Cursor doJob();

  @Override
  public Cursor loadInBackground() {
    // retrieve data from database
    Cursor cursor = doJob();
    
    if (cursor != null) {
      // Ensure the cursor window is filled
      cursor.getCount();
    }
    return cursor;
  }
  
  @Override
  public void deliverResult(Cursor cursor) {
    if (isReset()) {
      // An async query came in while the loader is stopped, invalidate current data
      if (cursor != null) {
        cursor.close();
      }
      return;
    }
    
    // swap in new cursor
    Cursor oldCursor = mCursor;
    mCursor = cursor;
    
    if (isStarted()) {
      super.deliverResult(cursor);
    }
    
    // close old cursor
    if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
      oldCursor.close();
    }
  }
  
  @Override
  protected void onStartLoading() {
    if (mCursor != null) {
      // Deliver any previously loaded data immediately.
      deliverResult(mCursor);
    }
    
    // Begin monitoring the database.
    if (mObserver == null) {
      mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
          DiaryDataLoader.this.onContentChanged();  // set takeContentChanged() = true
        }
        @Override
        public void onInvalidated() {
          DiaryDataLoader.this.onStopLoading();
        }
      };
      source.registerObserver(mObserver);   // register the observer
    }
    
    if (takeContentChanged() || mCursor == null) {
      forceLoad();
    }
  }
  
  @Override
  protected void onStopLoading() {
    // The Loader is in a stopped state, so we should attempt to cancel the 
    // current load (if there is one).
    cancelLoad();
    
    // Note that we leave the observer as is. Loaders in a stopped state
    // should still monitor the data source for changes so that the Loader
    // will know to force a new load if it is ever started again.
  }
  
  @Override
  public void onCanceled(Cursor cursor) {
    // Attempt to cancel the current asynchronous load.
    super.onCanceled(cursor);
    
    // The load has been canceled, so we should release the resources
    // associated with 'data'.
    if (cursor != null && !cursor.isClosed()) {
      cursor.close();
    }
  }
  
  @Override
  protected void onReset() {
    super.onReset();
    
    // Ensure the loader is stopped
    onStopLoading();
    
    // Close the current cursor.
    if (mCursor != null && !mCursor.isClosed()) {
      mCursor.close();
    }
    mCursor = null;
    
    // The Loader is being reset, so we should stop monitoring for changes.
    if (mObserver != null) {
      source.unregisterObserver(mObserver);   // unregister observer
      mObserver = null;
    }

  }
}
