package com.mrtan.androidarc.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import com.mrtan.androidarc.db.DatabaseCreator;
import com.mrtan.androidarc.db.entity.ProductEntity;
import java.util.List;

/**
 * @author mrtan on 12/25/17.
 */

public class ProductListViewModel extends AndroidViewModel {
  private static final MutableLiveData ABSENT = new MutableLiveData();
  {
    ABSENT.setValue(null);
  }

  private final LiveData<List<ProductEntity>> mObservabelProducts;

  public ProductListViewModel(@NonNull Application application) {
    super(application);

    final DatabaseCreator creator = DatabaseCreator.getsInstance(application);
    LiveData<Boolean> databaseCreated = creator.isDatabaseCreated();
    mObservabelProducts = Transformations.switchMap(databaseCreated,
        (Function<Boolean, LiveData<List<ProductEntity>>>) input -> {
          if (!Boolean.TRUE.equals(input)){
            return ABSENT;
          } else {
            assert creator.getDatabase() != null;
            return creator.getDatabase().productDao().loadAllProducts();
          }
        });
    creator.createDb(application);
  }

  public LiveData<List<ProductEntity>> getProducts() {
    return mObservabelProducts;
  }
}
