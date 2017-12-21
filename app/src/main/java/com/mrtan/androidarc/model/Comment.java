package com.mrtan.androidarc.model;

import java.util.Date;

/**
 * @author mrtan on 12/21/17.
 */

public interface Comment {
  int getId();
  int getProductId();
  String getText();
  Date getPostedAt();
}
