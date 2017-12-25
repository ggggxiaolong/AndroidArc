package com.mrtan.androidarc.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import com.mrtan.androidarc.db.converter.DateConverter;
import com.mrtan.androidarc.db.dao.CommentDao;
import com.mrtan.androidarc.db.dao.ProductDao;
import com.mrtan.androidarc.db.entity.CommentEntity;
import com.mrtan.androidarc.db.entity.ProductEntity;

/**
 * @author mrtan on 12/21/17.
 */
@Database(entities = { ProductEntity.class, CommentEntity.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

  static final String DATABASE_NAME = "basic-sample-db";

  public abstract ProductDao productDao();

  public abstract CommentDao commentDao();
}
