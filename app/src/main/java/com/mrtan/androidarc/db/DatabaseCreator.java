package com.mrtan.androidarc.db;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import timber.log.Timber;

import static com.mrtan.androidarc.db.AppDatabase.DATABASE_NAME;

/**
 * @author mrtan on 12/21/17.
 */

public class DatabaseCreator {
  private static DatabaseCreator sInstance;

  private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

  private AppDatabase mDb;

  private final AtomicBoolean mInitializing = new AtomicBoolean(true);

  private static final Object LOCK = new Object();

  public synchronized static DatabaseCreator getsInstance(Context context) {
    if (sInstance == null) {
      synchronized (LOCK) {
        if (sInstance == null) {
          sInstance = new DatabaseCreator();
        }
      }
    }
    return sInstance;
  }

  private DatabaseCreator() {
  }

  public LiveData<Boolean> isDatabaseCreated() {
    return mIsDatabaseCreated;
  }

  @Nullable public AppDatabase getDatabase() {
    return mDb;
  }

  @SuppressLint("StaticFieldLeak") public void createDb(Context context) {
    Timber.d("create DB from %s", Thread.currentThread().getName());
    if (!mInitializing.compareAndSet(true, false)) {
      return;
    }

    mIsDatabaseCreated.setValue(false);
    new AsyncTask<Context, Void, Void>() {

      @Override protected Void doInBackground(Context... contexts) {
        Timber.d("starting bg job %s", Thread.currentThread().getName());
        Context context = contexts[0];

        context.deleteDatabase(DATABASE_NAME);

        AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
            AppDatabase.class, DATABASE_NAME).build();
        addDelay();

        DatabaseInitUtil.initializeDb(db);
        Timber.d("DB was populated in thread %s", Thread.currentThread().getName());

        mDb = db;
        return null;
      }

      @Override protected void onPostExecute(Void aVoid) {
        mIsDatabaseCreated.setValue(true);
      }
    }.execute(context.getApplicationContext());
  }

  private void addDelay() {
    SystemClock.sleep(4000);
  }
}
