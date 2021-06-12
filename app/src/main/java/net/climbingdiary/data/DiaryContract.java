package net.climbingdiary.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class DiaryContract {

  public DiaryContract() {}
  
  /*****************************************************************************************************
   *                                          DIARY ENTRIES
   *****************************************************************************************************/
  public static class DiaryEntry implements BaseColumns {
    public static final String TABLE_NAME = "dentry";
    public static final String COLUMN_DATE = "entry_date";
    public static final String COLUMN_TYPE_ID = "typeid";     // what type of climbing
    public static final String COLUMN_PLACE_ID = "placeid";   // which place
    
    private static final String SQL_CREATE_TABLE = 
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_DATE + " INTEGER,"
        + COLUMN_TYPE_ID + " INTEGER,"
        + COLUMN_PLACE_ID + " INTEGER );";
    
    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    
    public static class Data {
      public long _id;
      public String date;
      public long type_id;
      public long place_id;
      public String type_desc;
      public String place_name;
    }
  }
  
  /*****************************************************************************************************
   *                                          CLIMBING TYPES
   *****************************************************************************************************/
  public static abstract class ClimbingTypes implements BaseColumns {
    public static final String TABLE_NAME = "ctypes";
    public static final String COLUMN_DESCRIPTION = "climb_desc";

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_DESCRIPTION + " TEXT );";
    
    private static final String[] default_types =
        new String[] { "Gym", "Wall", "Crag", "Trad" };
    
    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
      
      // insert default climbing types
      ContentValues values = new ContentValues();
      for (int i=0; i<default_types.length; i++) {
        values.put(COLUMN_DESCRIPTION, default_types[i]);
        db.insert(TABLE_NAME, null, values);
      }
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
  }

  /*****************************************************************************************************
   *                                          PLACES
   *****************************************************************************************************/
  public static abstract class Places implements BaseColumns {
    public static final String TABLE_NAME = "places";
    public static final String COLUMN_NAME = "place_name";

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_NAME + " TEXT );";
    
    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
  }
  
  /*****************************************************************************************************
   *                                          ROUTES
   *****************************************************************************************************/
  public static abstract class Routes implements BaseColumns {
    public static final String TABLE_NAME = "routes";
    public static final String COLUMN_NAME = "route_name";            // name of the route
    public static final String COLUMN_GRADE_ID = "grade_id";          // grade of the route
    public static final String COLUMN_PLACE_ID = "place_id";          // where the route is
    public static final String COLUMN_NOTES = "route_notes";          // extra info

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_NAME + " TEXT,"
        + COLUMN_GRADE_ID + " INTEGER,"
        + COLUMN_PLACE_ID + " INTEGER,"
        + COLUMN_NOTES + " TEXT"
        + ");";
    
    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    
    public static class Data {
      public long _id;
      public String name;
      public long grade_id;
      public long place_id;
      public String notes;
      public String place_name; // derived
      public String grade_yds;
      public String grade_fr;
    }
  }
  
  /*****************************************************************************************************
   *                                          GRADES
   * This table allows to have multiple combinations of YDS/French values,
   * thus capturing the uneven relationship between the two systems.
   *****************************************************************************************************/
  public static abstract class Grades implements BaseColumns {
    public static final String TABLE_NAME = "grades";
    public static final String COLUMN_GRADE_YDS = "grade_yds";  // grade (YDS)
    public static final String COLUMN_GRADE_FR = "grade_fr";    // grade (French)

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_GRADE_YDS + " TEXT,"
        + COLUMN_GRADE_FR + " TEXT"
        + ");";

    // from wikipedia
    private static final String[] yds = new String[] {
            //"5.6", "5.6+", "5.6/7", "5.7",
            //"5.7+", "5.7/8", "5.8", "5.8/9",
            //"5.9", "5.9+", "5.9/10a", "5.10a",
      "5.4", "5.5", "5.6", "5.7", "5.8", "5.9",
      "5.10a", "5.10b", "5.10c", "5.10d",
      "5.11a", "5.11b", "5.11c", "5.11d",
      "5.12a", "5.12b", "5.12c", "5.12d",
      "5.13a", "5.13b", "5.13c", "5.13d",
      "5.14a", "5.14b", "5.14c", "5.14d",
      "5.15a", "5.15b", "5.15c", "5.15d" };
    private static final String[] french = new String[] {
            //to use the following grades, I need to update the entire routes table
            //"4a", "4a+", "4b", "4b+",
            //"4c", "4c+", "5a", "5a+",
            //"5b", "5b+", "5c", "5c+",
      "4a", "4b", "4c", "5a", "5b", "5c",
      "6a", "6a+", "6b", "6b+",
      "6b+/c", "6c", "6c+", "7a",
      "7a+", "7b", "7b+", "7c",
      "7c+", "8a", "8a+", "8b",
      "8b+", "8c", "8c+", "9a",
      "9a+", "9b", "9b+", "9c" };

    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
      
      // insert wikipedia grades
      ContentValues values = new ContentValues();
      for (int i=0; i<yds.length; i++) {
        values.put(COLUMN_GRADE_YDS, yds[i]);
        values.put(COLUMN_GRADE_FR, french[i]);
        db.insert(TABLE_NAME, null, values);
      }
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public static class Data {
      public long _id;
      public String yds;
      public String french;
    }
  }
  
  /*****************************************************************************************************
   *                                          ASCENT TYPES
   * This table allows to distinguish the nature and outcome of ascents.
   *****************************************************************************************************/
  public static abstract class AscentTypes implements BaseColumns {
    public static final String TABLE_NAME = "asctypes";
    public static final String COLUMN_NAME = "name";          // name of the ascent type
    public static final String COLUMN_DESC = "description";   // description of the ascent type

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_NAME + " TEXT,"
        + COLUMN_DESC + " TEXT"
        + ");";

    // from www.thecrag.com/article/ticktypes
    private static final String[] default_types = new String[] {
      "Tick", "Clean",
      "Lead", "Onsight", "Flash", "Redpoint", "Pinkpoint", "Dog", "Lead solo",
      "Second", "Second clean", "Second with rest",
      "Top rope", "Top rope onsight", "Top rope flash", "Top rope clean", "Top rope with rest", "Roped solo",
      "Aid", "Aid solo",
      "Solo",
      "First ascent", "First free ascent",
      "Attempt", "Working", "Retreat" };
    private static final String[] descriptions = new String[] {
      "I climbed this route.", "I climbed this route without resting.",
      "I lead this route.", "I lead this route, without falling or resting," +
      		" on my first attempt without prior inspection or beta.",
      "I led this route, without falling or resting, on my first attempt, but used beta.",
      "I led this route, without falling or resting, but not on my first attempt.",
      "I led this route, without falling or resting, but not on my first attempt, using pre-placed gear.",
      "I led this route, but rested on gear or fell on the way up.",
      "I led this route, using a lead solo device, anchor rope at the bottom.",
      "I seconded this route.", "I seconded this route, without resting on the rope.",
      "I seconded this route, but rested on the rope.",
      "I top roped this route.", "", "", "", "", "",
      "I lead this route, using fixed or placed protection to make upward progress.", "",
      "I soloed this route.",
      "I was the first person to ever climb this route.",
      "I was the first person to ever climb this route free of aid.",
      "I attempted, but did not complete, this route.",
      "I am working on this route or problem as a personal project.",
      "I attempted this route but retreated (eg too hard, weather turned, not enough gear, injury)." };
    private static final int[] scores = new int[] {
      2, 3,
      40, 49, 48, 47, 46, 45, 44,
      30, 39, 38,
      10, 19, 18, 17, 16, 15,
      20, 29,
      100,
      200, 201,
      1, 5, 0
    };
    public static final String completed = "4,5,6,7,21,22,23";

    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
      
      // insert default types
      ContentValues values = new ContentValues();
      for (int i=0; i<default_types.length; i++) {
        values.put(COLUMN_NAME, default_types[i]);
        values.put(COLUMN_DESC, descriptions[i]);
        db.insert(TABLE_NAME, null, values);
      }
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    
    public static String getBest(String multivalues) {
      if (multivalues == null) {
        return "---";
      } else {
        String[] values = multivalues.split(",");
        int best = 0, score = -1;
        for (int i=0; i<values.length; i++) {
          int idx = Integer.valueOf(values[i]);
          if (idx > 0 && scores[idx-1] > score) {
            best = idx-1;
            score = scores[best];
          }
        }
        return default_types[best];
      }
    }
  }
  
  /*****************************************************************************************************
   *                                          ASCENTS
   *****************************************************************************************************/
  public static class Ascents implements BaseColumns {
    public static final String TABLE_NAME = "ascents";
    public static final String COLUMN_ENTRY_ID = "entry_id";  // when the ascent happened
    public static final String COLUMN_ROUTE_ID = "route_id";  // which route
    public static final String COLUMN_TYPE_ID = "type_id";    // what type of ascent
    public static final String COLUMN_NOTES = "notes";        // extra info on what happened
    
    private static final String SQL_CREATE_TABLE = 
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_ENTRY_ID + " INTEGER,"
        + COLUMN_ROUTE_ID + " INTEGER,"
        + COLUMN_TYPE_ID + " INTEGER,"
        + COLUMN_NOTES + " TEXT"
        + ");";

    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);
    }
    
    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    
    public static class Data {
      public long _id;
      public long entry_id;
      public long route_id;
      public long type_id;
      public String notes;
      public long place_id; // extra
      public String route_name;
    }
  }

  /*****************************************************************************************************
   *                                          SETTINGS
   *****************************************************************************************************/
  public static class Settings implements BaseColumns {
    public static final String TABLE_NAME = "settings";
    public static final String COLUMN_SETTING_NAME = "name";       // shortname
    public static final String COLUMN_SETTING_VALUE = "value";     // value

    private static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + TABLE_NAME + " ("
        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + COLUMN_SETTING_NAME + " TEXT,"
        + COLUMN_SETTING_VALUE + " TEXT"
        + ");";

    private static final String[] names = new String[] {
      "useFrenchGrades"
    };
    private static final String[] default_values = new String[] {
      "off"
    };

    public static void create(SQLiteDatabase db) {
      db.execSQL(SQL_CREATE_TABLE);

      // insert default valued settings
      ContentValues values = new ContentValues();
      for (int i=0; i<names.length; i++) {
        values.put(COLUMN_SETTING_NAME, names[i]);
        values.put(COLUMN_SETTING_VALUE, default_values[i]);
        db.insert(TABLE_NAME, null, values);
      }
    }

    public static void destroy(SQLiteDatabase db) {
      db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
  }
}
