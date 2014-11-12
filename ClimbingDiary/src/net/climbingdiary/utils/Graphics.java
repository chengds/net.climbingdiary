package net.climbingdiary.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;

public class Graphics {
  
  /*
   * Draw a colored dot in the background of the given view based on the given type of ascent.
   */
  public static void drawDot(View view, String type, int[] inset) {
    ShapeDrawable dot = null;
    
    if (type.equalsIgnoreCase("redpoint")) {
      dot = new ShapeDrawable(new OvalShape());
      dot.getPaint().setColor(Color.RED);
    } else if (type.equalsIgnoreCase("onsight")) {
      dot = new ShapeDrawable(new OvalShape());
      dot.getPaint().setColor(Color.parseColor("#FFD700"));
    } else if (type.equalsIgnoreCase("flash")) {
      dot = new ShapeDrawable(new OvalShape());
      dot.getPaint().setColor(Color.parseColor("#EE7600"));
    } else if (type.equalsIgnoreCase("pinkpoint")) {
      dot = new ShapeDrawable(new OvalShape());
      dot.getPaint().setColor(Color.parseColor("#FF748C"));
    }

    if (dot != null) {
      LayerDrawable d = new LayerDrawable(new Drawable[]{dot});
      d.setLayerInset(0, inset[0], inset[1], inset[2], inset[3]);
      view.setBackgroundDrawable(d);
    } else {
      view.setBackgroundDrawable(null);
    }
  }
}
