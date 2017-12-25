package com.mrtan.androidarc.ui;

import android.databinding.BindingAdapter;
import android.view.View;

/**
 * @author mrtan on 12/22/17.
 */

public class BindingAdapters {
  @BindingAdapter("visibleGone")
  public static void showHide(View view, boolean show) {
    view.setVisibility(show ? View.VISIBLE : View.GONE);
  }
}
