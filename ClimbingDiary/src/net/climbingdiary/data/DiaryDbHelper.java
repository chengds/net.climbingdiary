package net.climbingdiary.data;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import net.climbingdiary.data.DiaryContract.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DiaryDbHelper extends SQLiteOpenHelper {
  
  private static DiaryDbHelper instance;                              // Singleton pattern  
  private static final String DATABASE_NAME = "ClimbingDiary.db";     // filename and version of the database
  private static final int DATABASE_VERSION = 6;
  private SQLiteDatabase db = null;               // SQLite database
  private String[] cache_types = null;            // caches
  private String[] cache_places = null;
  private Cursor cache_asctypes = null;
  private Cursor cache_grades = null;
  
  /*****************************************************************************************************
   *                                          OBSERVER pattern
   *****************************************************************************************************/
  DataSetObservable mObservable = new DataSetObservable();  // observer pattern for database changes
  public DataSetObservable getSource() {
    return mObservable;
  }
  
  /*****************************************************************************************************
   *                                          SQL QUERIES
   *****************************************************************************************************/
  // SQL query to get one entry
  private final static String QUERY_ENTRY =
      "SELECT e." + DiaryEntry.COLUMN_DATE
      + ", e." + DiaryEntry.COLUMN_TYPE_ID + ", e." + DiaryEntry.COLUMN_PLACE_ID
      + ", c." + ClimbingTypes.COLUMN_DESCRIPTION + ", p." + Places.COLUMN_NAME
      + " FROM " + DiaryEntry.TABLE_NAME + " e"
      + " LEFT JOIN " + ClimbingTypes.TABLE_NAME + " c ON e." + DiaryEntry.COLUMN_TYPE_ID + " = c._id"
      + " LEFT JOIN " + Places.TABLE_NAME + " p ON e." + DiaryEntry.COLUMN_PLACE_ID + " = p._id"
      + " WHERE e." + DiaryEntry._ID + " = ?";
  
  // SQL query to get one route
  private final static String QUERY_ROUTE =
      "SELECT r." + Routes.COLUMN_NAME
      + ", r." + Routes.COLUMN_GRADE_ID + ", r." + Routes.COLUMN_PLACE_ID
      + ", r." + Routes.COLUMN_NOTES + ", p." + Places.COLUMN_NAME
      + ", g." + Grades.COLUMN_GRADE_YDS + ", g." + Grades.COLUMN_GRADE_FR
      + " FROM " + Routes.TABLE_NAME + " r"
      + " LEFT JOIN " + Places.TABLE_NAME + " p ON r." + Routes.COLUMN_PLACE_ID + " = p." + Places._ID
      + " LEFT JOIN " + Grades.TABLE_NAME + " g ON r." + Routes.COLUMN_GRADE_ID + " = g." + Grades._ID
      + " WHERE r." + Routes._ID + " = ?";
  
  // SQL query to get one route
  private final static String QUERY_ASCENT =
      "SELECT a." + Ascents.COLUMN_ENTRY_ID + ", a." + Ascents.COLUMN_ROUTE_ID
      + ", a." + Ascents.COLUMN_TYPE_ID + ", a." + Ascents.COLUMN_NOTES 
      + ", r." + Routes.COLUMN_PLACE_ID + ", r." + Routes.COLUMN_NAME
      + " FROM " + Ascents.TABLE_NAME + " a"
      + " LEFT JOIN " + Routes.TABLE_NAME + " r ON a." + Ascents.COLUMN_ROUTE_ID + " = r._id"
      + " WHERE a." + Ascents._ID + " = ?";
  
  // SQL query to get all entries
  private final static String QUERY_ENTRIES =
      "SELECT e." + DiaryEntry._ID + ", e." + DiaryEntry.COLUMN_DATE
      + ", c." + ClimbingTypes.COLUMN_DESCRIPTION + ", p." + Places.COLUMN_NAME 
      + " FROM " + DiaryEntry.TABLE_NAME + " e"
      + " LEFT JOIN " + ClimbingTypes.TABLE_NAME + " c ON e." + DiaryEntry.COLUMN_TYPE_ID + " = c._id"
      + " LEFT JOIN " + Places.TABLE_NAME + " p ON e." + DiaryEntry.COLUMN_PLACE_ID + " = p._id"
      + " ORDER BY e." + DiaryEntry.COLUMN_DATE;
  
  // SQL query to get number of visits, and the number of routes, per place
  private final static String QUERY_VISITS =
      "SELECT p." + Places._ID + ", p." + Places.COLUMN_NAME
      + ", (SELECT COUNT(e." + DiaryEntry._ID + ") FROM " + DiaryEntry.TABLE_NAME + " e"
              + " WHERE e." + DiaryEntry.COLUMN_PLACE_ID + " = p." + Places._ID + ") visits"
      + ", (SELECT COUNT(r." + Routes._ID + ") FROM " + Routes.TABLE_NAME + " r"
              + " WHERE r." + Routes.COLUMN_PLACE_ID + " = p." + Places._ID + ") routes"
  		+ " FROM " + Places.TABLE_NAME + " p"
      + " ORDER BY p." + Places.COLUMN_NAME;
  
  // SQL query to get the ascents of a given visit
  private final static String QUERY_ASCENTS =
      "SELECT a." + Ascents._ID + ", t." + AscentTypes.COLUMN_NAME
      + ", g." + Grades.COLUMN_GRADE_YDS + ", g." + Grades.COLUMN_GRADE_FR
      + ", r." + Routes.COLUMN_NAME + ", a." + Ascents.COLUMN_NOTES 
      + " FROM " + Ascents.TABLE_NAME + " a"
      + " LEFT JOIN " + AscentTypes.TABLE_NAME + " t ON a." + Ascents.COLUMN_TYPE_ID + " = t._id"
      + " LEFT JOIN " + Routes.TABLE_NAME + " r ON a." + Ascents.COLUMN_ROUTE_ID + " = r._id"
      + " LEFT JOIN " + Grades.TABLE_NAME + " g ON r." + Routes.COLUMN_GRADE_ID + " = g._id"
      + " WHERE a." + Ascents.COLUMN_ENTRY_ID + " = ?";
  
  // SQL query to get all the routes in a given place (also counting ascents)
  private final static String QUERY_ROUTES_BY_PLACE =
      "SELECT r." + Routes._ID + ", r." + Routes.COLUMN_NAME + ", g." + Grades.COLUMN_GRADE_YDS
      + ", g." + Grades.COLUMN_GRADE_FR + ", r." + Routes.COLUMN_NOTES
      + ", COUNT(a._id) ascents, GROUP_CONCAT(a." + Ascents.COLUMN_TYPE_ID + ") status"
      + " FROM " + Routes.TABLE_NAME + " r"
      + " LEFT JOIN " + Grades.TABLE_NAME + " g ON r." + Routes.COLUMN_GRADE_ID + " = g." + Grades._ID
      + " LEFT JOIN " + Ascents.TABLE_NAME + " a ON a." + Ascents.COLUMN_ROUTE_ID + " = r." + Routes._ID
      + " WHERE r." + Routes.COLUMN_PLACE_ID + " = ?"
      + " GROUP BY r." + Routes._ID
      + " ORDER BY g." + Grades.COLUMN_GRADE_FR + ", g." + Grades.COLUMN_GRADE_YDS;
  
  // SQL query to get the ascents of a given route
  private final static String QUERY_ROUTE_ASCENTS =
      "SELECT a." + Ascents._ID + ", e." + DiaryEntry.COLUMN_DATE
      + ", t." + AscentTypes.COLUMN_NAME + ", a." + Ascents.COLUMN_NOTES 
      + " FROM " + Ascents.TABLE_NAME + " a"
      + " LEFT JOIN " + DiaryEntry.TABLE_NAME + " e ON a." + Ascents.COLUMN_ENTRY_ID + " = e." + DiaryEntry._ID
      + " LEFT JOIN " + AscentTypes.TABLE_NAME + " t ON a." + Ascents.COLUMN_TYPE_ID + " = t." + AscentTypes._ID
      + " LEFT JOIN " + Routes.TABLE_NAME + " r ON a." + Ascents.COLUMN_ROUTE_ID + " = r." + Routes._ID
      + " LEFT JOIN " + Grades.TABLE_NAME + " g ON r." + Routes.COLUMN_GRADE_ID + " = g." + Grades._ID
      + " WHERE r." + Routes._ID + " = ?"
      + " ORDER BY e." + DiaryEntry.COLUMN_DATE;
  
  // SQL query to get completed ascents of a given grade
  private final static String QUERY_COMPLETED_ASCENTS_BY_GRADE_BY_TYPE = 
      "SELECT a." + Ascents._ID + ", t." + AscentTypes.COLUMN_NAME
      + ", r." + Routes._ID + ", e." + DiaryEntry.COLUMN_DATE
      + " FROM " + Ascents.TABLE_NAME + " a"
      + " LEFT JOIN " + AscentTypes.TABLE_NAME + " t ON a." + Ascents.COLUMN_TYPE_ID + " = t." + AscentTypes._ID
      + " LEFT JOIN " + Routes.TABLE_NAME + " r ON a." + Ascents.COLUMN_ROUTE_ID + " = r." + Routes._ID
      + " LEFT JOIN " + DiaryEntry.TABLE_NAME + " e ON a." + Ascents.COLUMN_ENTRY_ID + " = e." + DiaryEntry._ID
      + " LEFT JOIN " + ClimbingTypes.TABLE_NAME + " c ON e." + DiaryEntry.COLUMN_TYPE_ID + " = c." + ClimbingTypes._ID
      + " WHERE a." + Ascents.COLUMN_TYPE_ID + " IN (" + AscentTypes.completed + ")"
      + " AND r." + Routes.COLUMN_GRADE_ID + " = ?"
      + " AND c." + ClimbingTypes.COLUMN_DESCRIPTION + " = ?"
      + " ORDER BY e." + DiaryEntry.COLUMN_DATE;
  
/*  private final static String QUERY_GRADE_ASCENTS_FILTERED = 
      "SELECT g." + Grades._ID + ", g." + Grades.COLUMN_GRADE_YDS
      + ", g." + Grades.COLUMN_GRADE_FR
      + ", (SELECT GROUP_CONCAT(t." + AscentTypes.COLUMN_NAME + ") FROM " + Ascents.TABLE_NAME + " a"
        + " LEFT JOIN " + AscentTypes.TABLE_NAME + " t ON a." + Ascents.COLUMN_TYPE_ID + " = t." + AscentTypes._ID
        + " LEFT JOIN " + Routes.TABLE_NAME + " r ON a." + Ascents.COLUMN_ROUTE_ID + " = r." + Routes._ID
        + " LEFT JOIN " + DiaryEntry.TABLE_NAME + " e ON a." + Ascents.COLUMN_ENTRY_ID + " = e." + DiaryEntry._ID
        + " LEFT JOIN " + ClimbingTypes.TABLE_NAME + " c ON e." + DiaryEntry.COLUMN_TYPE_ID + " = c." + ClimbingTypes._ID
        + " WHERE r." + Routes.COLUMN_GRADE_ID + " = g." + Grades._ID
        + " AND c." + ClimbingTypes.COLUMN_DESCRIPTION + " = ?"
        + " ORDER BY e." + DiaryEntry.COLUMN_DATE
        + ") ascents"
      + " FROM " + Grades.TABLE_NAME + " g"
      + " WHERE g." + Grades.COLUMN_GRADE_FR + " <= ?"
      + " ORDER BY g." + Grades.COLUMN_GRADE_FR + " DESC, g." + Grades.COLUMN_GRADE_YDS + " DESC";
  */
  
  /*****************************************************************************************************
   *                                          SINGLETON pattern
   *****************************************************************************************************/
  public static DiaryDbHelper getInstance(Context context) {
    if (instance == null) {
      instance = new DiaryDbHelper(context);
    }
    return instance;
  }
  
  // CONSTRUCTOR: create or open the database
  private DiaryDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    db = getWritableDatabase();
  }
  
  /*****************************************************************************************************
   *                                          CREATE/UPGRADE
   *****************************************************************************************************/
  @Override
  public void onCreate(SQLiteDatabase db) {
    Places.create(db);            // create all the tables
    ClimbingTypes.create(db);
    DiaryEntry.create(db);
    Grades.create(db);
    Routes.create(db);
    AscentTypes.create(db);
    Ascents.create(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // deal with incremental upgrades to the database (cascade all changes)
    switch (oldVersion + 1) {
      default:                      // default for version < 6
        DiaryEntry.destroy(db);
        ClimbingTypes.destroy(db);
        Places.destroy(db);
        onCreate(db);
        break;
      case 6: // add ascents data
        Log.v("debug", "upgrading database to v6");
        Grades.create(db);
        Routes.create(db);
        AscentTypes.create(db);
        Ascents.create(db);
      case 7: // future version undefined yet
      case 8: // future version undefined yet
    }
  }
  
  /*****************************************************************************************************
   *                                          CACHES HANDLERS
   *****************************************************************************************************/
  // update the cache with climbing types
  private void updateTypesCache() {
    // query the database for all the climbing types
    Cursor c = db.query(ClimbingTypes.TABLE_NAME, null, null, null, null, null, null);
    if (c.moveToFirst()) {
      cache_types = new String[c.getCount()];
      int i = 0;
      do {
        cache_types[i++] = c.getString(c.getColumnIndex(ClimbingTypes.COLUMN_DESCRIPTION));
      } while (c.moveToNext());
    } else {
      cache_types = new String[] {};
    }
  }

  // update the cache with climbing places
  private void updatePlacesCache() {
    // query the database for all the climbing places
    Cursor c = db.query(Places.TABLE_NAME, null, null, null, null, null, Places.COLUMN_NAME);
    if (c.moveToFirst()) {
      cache_places = new String[c.getCount()];
      int i = 0;
      do {
        cache_places[i++] = c.getString(c.getColumnIndex(Places.COLUMN_NAME));
      } while (c.moveToNext());
    } else {
      cache_places = new String[] {};
    }
  }

  // update the cache with ascent types
  private void updateAscentTypesCache() {
    // query the database for all the ascent types
    cache_asctypes = db.query(AscentTypes.TABLE_NAME, null, null, null, null, null, null);
  }

  // update the cache with grades
  private void updateGradesCache() {
    // query the database for all the climbing grades, in ascending order
    cache_grades = db.query(Grades.TABLE_NAME, null, null, null, null, null,
        Grades.COLUMN_GRADE_FR + "," + Grades.COLUMN_GRADE_YDS);
  }

  // return a list of all the climbing types
  public String[] getAllTypes() {
    if (cache_types == null) updateTypesCache();
    return cache_types;
  }

  // return a list of all the climbing places
  public String[] getAllPlaces() {
    if (cache_places == null) updatePlacesCache();
    return cache_places;
  }
  
  // return a cursor with all ascent types
  public Cursor getAscentTypes() {
    if (cache_asctypes == null) updateAscentTypesCache();
    return cache_asctypes;
  }

  // return a list of all the climbing grades
  public Cursor getGrades() {
    if (cache_grades == null) updateGradesCache();
    return cache_grades;
  }

  // return a list of all the routes names in a given place
  public String[] getAllRoutes(long place_id) {
    // query the database for all the routes in the given place
    Cursor c = db.query(Routes.TABLE_NAME, new String[]{ Routes.COLUMN_NAME },
        Routes.COLUMN_PLACE_ID + " = ?", new String[]{ String.valueOf(place_id) }, null, null, null);
    String[] routes = new String[] {};
    if (c.moveToFirst()) {
      routes = new String[c.getCount()];
      int i = 0;
      do {
        routes[i++] = c.getString(0);
      } while (c.moveToNext());
    }
    return routes;
  }
  
  // returns a cursor with all the routes in a given place
  public Cursor getRoutes(long place_id) {
    return db.rawQuery(QUERY_ROUTES_BY_PLACE, new String[]{ String.valueOf(place_id) });
  }
  
  // returns the climbing pyramid for the given type of climbing
  public ArrayList<ArrayList<String>> getPyramid(String ctype) {
    ArrayList<ArrayList<String>> pyramid = null; 

    // check each grade, in descending order
    Cursor grades = getGrades();
    grades.moveToLast();
    do {
      // the current grade
      long id = grades.getLong(grades.getColumnIndex(Grades._ID));
      
      // find completed ascents
      Cursor a = db.rawQuery(QUERY_COMPLETED_ASCENTS_BY_GRADE_BY_TYPE,
                             new String[]{ String.valueOf(id), ctype });

      // create a list of the completed ascents types with the grade first
      ArrayList<String> ascents = new ArrayList<String>();
      ascents.add(grades.getString(grades.getColumnIndex(Grades.COLUMN_GRADE_YDS)));
      
      if (a.moveToFirst()) {
        do {
          ascents.add(a.getString(a.getColumnIndex(AscentTypes.COLUMN_NAME)));
        } while (a.moveToNext());
        
        // add the list to the pyramid
        if (pyramid == null) {
          pyramid = new ArrayList<ArrayList<String>>();
        }
        pyramid.add(ascents);
      } else {
        if (pyramid != null) {
          // log an empty set of ascents for this grade
          pyramid.add(ascents);
        }
      }
      
    } while (grades.moveToPrevious());
        
    return pyramid;
  }

  /*****************************************************************************************************
   *                                          ADD DATA
   *****************************************************************************************************/
  /**
   * Add a new place. Update the cache of places.
   */
  public long addPlace(String name) {
    ContentValues values = new ContentValues();
    values.put(Places.COLUMN_NAME, name);
    long id = db.insert(Places.TABLE_NAME, null, values);
    updatePlacesCache();
    mObservable.notifyChanged();
    return id;
  }
  
  /**
   * Add a new entry in the diary.
   */
  public long addEntry(Date date, String type, String place) {
    ContentValues values = new ContentValues();
    
    // get type id
    long typeid = 1;
    Cursor c = db.query(ClimbingTypes.TABLE_NAME, null,
        ClimbingTypes.COLUMN_DESCRIPTION + " = ? ",
        new String[] { type }, null, null, null);
    if (c.moveToFirst()) {
      typeid = c.getLong(0);
    } else {
      Log.v("DIOBOIA", "unrecog climb type");
    }
    
    // get place id
    long placeid = 1;
    c = db.query(Places.TABLE_NAME, new String[]{ Places._ID,
        Places.COLUMN_NAME }, Places.COLUMN_NAME + " = ?",
        new String[]{ place }, null, null, null);
    if (c.moveToFirst()) {
      placeid = c.getLong(0);
    } else {
      placeid = addPlace(place); // add the new place
    }
    
    // insert the new entry in the database
    values.put(DiaryEntry.COLUMN_DATE, date.getTime());
    values.put(DiaryEntry.COLUMN_TYPE_ID, typeid);
    values.put(DiaryEntry.COLUMN_PLACE_ID, placeid);
    long route_id = db.insert(DiaryEntry.TABLE_NAME, null, values);
    mObservable.notifyChanged();
    return route_id;
  }
  
  /**
   * Add a new ascent in the diary, returns -1 if the route already existed,
   * or the newly created route_id otherwise.
   */
  public long addAscent(long entry_id, long place_id, String route_name, long asctype_id, String notes) {
    Log.v("addAscent", "entry:" + entry_id + " place:" + place_id + " route:" + route_name
        + " type:" + asctype_id + " notes:" + notes);
    ContentValues values = new ContentValues();
    long res = -1;
    
    // get route id
    long route_id;
    Cursor c = db.query(Routes.TABLE_NAME, new String[]{ Routes._ID },
        Routes.COLUMN_NAME + " = ?", new String[]{ route_name }, null, null, null);
    if (c.moveToFirst()) {
      route_id = c.getLong(0);    // retrieve the route ID
    } else {
      route_id = addRoute(place_id,route_name);
      res = route_id;
    }
    
    // insert the new ascent in the database
    values.put(Ascents.COLUMN_ENTRY_ID, entry_id);
    values.put(Ascents.COLUMN_ROUTE_ID, route_id);
    values.put(Ascents.COLUMN_TYPE_ID, asctype_id);
    values.put(Ascents.COLUMN_NOTES, notes);
    db.insert(Ascents.TABLE_NAME, null, values);
    mObservable.notifyChanged();
    return res;
  }
  
  /**
   * Add a new route in the diary, using default details.
   */
  public long addRoute(long place_id, String name) {
    ContentValues values = new ContentValues();
    values.put(Routes.COLUMN_NAME, name);
    values.put(Routes.COLUMN_GRADE_ID, 1);
    values.put(Routes.COLUMN_PLACE_ID, place_id);
    values.put(Routes.COLUMN_NOTES, "");
    long id = db.insert(Routes.TABLE_NAME, null, values);
    mObservable.notifyChanged();
    return id;
  }
  
  /*****************************************************************************************************
   *                                          REMOVE DATA
   *****************************************************************************************************/
  /**
   * Remove an entry from the diary.
   */
  public void removeEntry(long id) {
    db.delete(DiaryEntry.TABLE_NAME, "_id = ?", new String[] { String.valueOf(id) });
    mObservable.notifyChanged();
  }
  
  // delete the given route and all related ascents.
  public void deleteRoute(long route_id) {
    String[] whereArgs = new String[]{ String.valueOf(route_id) };
    db.delete(Ascents.TABLE_NAME, Ascents.COLUMN_ROUTE_ID + "=?", whereArgs);
    db.delete(Routes.TABLE_NAME, Routes._ID + "=?", whereArgs);
    mObservable.notifyChanged();
  }
  
  // delete the given route and all related ascents.
  public void deleteAscent(long ascent_id) {
    String[] whereArgs = new String[]{ String.valueOf(ascent_id) };
    db.delete(Ascents.TABLE_NAME, Ascents._ID + "=?", whereArgs);
    mObservable.notifyChanged();
  }
  
  /*****************************************************************************************************
   *                                          RETRIEVE/UPDATE DATA
   *****************************************************************************************************/
  // retrieve all the diary entries
  public Cursor getEntries() {
    return db.rawQuery(QUERY_ENTRIES, null);
  }

  // retrieve all the places, including number of visits for each place
  public Cursor getPlaces() {
    return db.rawQuery(QUERY_VISITS, null);
  }

  // retrieve the ascents on a given visit
  public Cursor getAscents(long entry_id) {
    return db.rawQuery(QUERY_ASCENTS, new String[]{ String.valueOf(entry_id) });
  }

  // retrieve the ascents on a given route
  public Cursor getRouteAscents(long route_id) {
    return db.rawQuery(QUERY_ROUTE_ASCENTS, new String[]{ String.valueOf(route_id) });
  }

  // Retrieve the entry info by id.
  public DiaryEntry.Data getEntry(long id) {
    DiaryEntry.Data info = null;
    Cursor c = db.rawQuery(QUERY_ENTRY, new String[]{ String.valueOf(id) });
    if (c.moveToFirst()) {
      DateFormat df = DateFormat.getDateInstance();
      info = new DiaryEntry.Data();
      info._id = id;
      info.date = df.format(new Date(c.getLong(c.getColumnIndex(DiaryEntry.COLUMN_DATE))));
      info.type_id = c.getLong(c.getColumnIndex(DiaryEntry.COLUMN_TYPE_ID));
      info.place_id = c.getLong(c.getColumnIndex(DiaryEntry.COLUMN_PLACE_ID));
      info.type_desc = c.getString(c.getColumnIndex(ClimbingTypes.COLUMN_DESCRIPTION));
      info.place_name = c.getString(c.getColumnIndex(Places.COLUMN_NAME));
    }
    return info;
  }

  // retrieve the route info by id
  public Routes.Data getRoute(long route_id) {
    Routes.Data info = null;
    Cursor c = db.rawQuery(QUERY_ROUTE, new String[]{ String.valueOf(route_id) });
    if (c.moveToFirst()) {
      info = new Routes.Data();
      info._id = route_id;
      info.name = c.getString(c.getColumnIndex(Routes.COLUMN_NAME));
      info.grade_id = c.getLong(c.getColumnIndex(Routes.COLUMN_GRADE_ID));
      info.place_id = c.getLong(c.getColumnIndex(Routes.COLUMN_PLACE_ID));
      info.notes = c.getString(c.getColumnIndex(Routes.COLUMN_NOTES));
      info.place_name = c.getString(c.getColumnIndex(Places.COLUMN_NAME));
      info.grade_yds = c.getString(c.getColumnIndex(Grades.COLUMN_GRADE_YDS));
      info.grade_fr = c.getString(c.getColumnIndex(Grades.COLUMN_GRADE_FR));
    }
    return info;
  }
  
  // update the route info
  public void updateRoute(Routes.Data info) {
    // prep the info
    ContentValues values = new ContentValues();
    values.put(Routes.COLUMN_NAME, info.name);
    values.put(Routes.COLUMN_GRADE_ID, info.grade_id);
    values.put(Routes.COLUMN_PLACE_ID, info.place_id);
    values.put(Routes.COLUMN_NOTES, info.notes);
    
    if (info._id > 0) {
      db.update(Routes.TABLE_NAME, values, Routes._ID + "=?", new String[]{ String.valueOf(info._id) });
    } else {
      // this is a new route
      db.insert(Routes.TABLE_NAME, null, values);
    }
    mObservable.notifyChanged();
  }
  
  // retrieve the ascent info by id
  public Ascents.Data getAscent(long ascent_id) {
    Ascents.Data info = null;
    Cursor c = db.rawQuery(QUERY_ASCENT, new String[]{ String.valueOf(ascent_id) });
    if (c.moveToFirst()) {
      info = new Ascents.Data();
      info._id = ascent_id;
      info.entry_id = c.getLong(c.getColumnIndex(Ascents.COLUMN_ENTRY_ID));
      info.route_id = c.getLong(c.getColumnIndex(Ascents.COLUMN_ROUTE_ID));
      info.type_id = c.getLong(c.getColumnIndex(Ascents.COLUMN_TYPE_ID));
      info.notes = c.getString(c.getColumnIndex(Ascents.COLUMN_NOTES));
      info.place_id = c.getLong(c.getColumnIndex(Routes.COLUMN_PLACE_ID));
      info.route_name = c.getString(c.getColumnIndex(Routes.COLUMN_NAME));
    }
    return info;
  }
  
  // update the ascent info
  public long updateAscent(Ascents.Data info) {
    ContentValues values = new ContentValues();
    long res = -1;
    
    // check/create the route for this ascent
    long route_id;
    Cursor c = db.query(Routes.TABLE_NAME, new String[]{ Routes._ID },
        Routes.COLUMN_NAME + " = ? AND " + Routes.COLUMN_PLACE_ID + " = ?",
        new String[]{ info.route_name, String.valueOf(info.place_id) }, null, null, null);
    if (c.moveToFirst()) {
      route_id = c.getLong(0);    // retrieve the route ID, but return -1 at the end
    } else {
      res = route_id = addRoute(info.place_id, info.route_name);  // create a new route and return its ID
    }

    // prep the info
    values.put(Ascents.COLUMN_ENTRY_ID, info.entry_id);
    values.put(Ascents.COLUMN_ROUTE_ID, route_id);
    values.put(Ascents.COLUMN_TYPE_ID, info.type_id);
    values.put(Ascents.COLUMN_NOTES, info.notes);
    
    // update/create the ascent
    if (info._id > 0) {
      db.update(Ascents.TABLE_NAME, values, Ascents._ID + "=?", new String[]{ String.valueOf(info._id) });
    } else {
      // this is a new route
      db.insert(Ascents.TABLE_NAME, null, values);
    }
    mObservable.notifyChanged();
    return res;
  }
}
