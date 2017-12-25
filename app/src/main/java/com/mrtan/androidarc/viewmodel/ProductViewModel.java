package com.mrtan.androidarc.viewmodel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import com.mrtan.androidarc.db.DatabaseCreator;
import com.mrtan.androidarc.db.entity.CommentEntity;
import com.mrtan.androidarc.db.entity.ProductEntity;
import java.util.List;

/**
 * @author mrtan on 12/25/17.
 */

public class ProductViewModel extends AndroidViewModel {

  private static final MutableLiveData ABSENT = new MutableLiveData();
  {
    ABSENT.setValue(null);
  }

  private final LiveData<ProductEntity> mObserverProduct;

  public ObservableField<ProductEntity> product = new ObservableField<>();

  private final int mProductId;

  private final LiveData<List<CommentEntity>> mObservableComments;

  public ProductViewModel(@NonNull Application application, int productId) {
    super(application);
    mProductId = productId;
    final DatabaseCreator databaseCreator = DatabaseCreator.getsInstance(application);
    mObservableComments = Transformations.switchMap(databaseCreator.isDatabaseCreated(),
        (Function<Boolean, LiveData<List<CommentEntity>>>) input -> {
          if (!input) {
            return ABSENT;
          }
          assert databaseCreator.getDatabase() != null;
          return databaseCreator.getDatabase().commentDao().loadComments(productId);
        });

    mObserverProduct = Transformations.switchMap(databaseCreator.isDatabaseCreated(), (Function<Boolean, LiveData<ProductEntity>>) input -> {
      if (!input){
        return ABSENT;
      }
      assert databaseCreator.getDatabase() != null;
      return databaseCreator.getDatabase().productDao().loadProduct(mProductId);
    });
    databaseCreator.createDb(getApplication());
  }

  public LiveData<List<CommentEntity>> getComments() {
    return mObservableComments;
  }

  public LiveData<ProductEntity> getObserverProduct() {
    return mObserverProduct;
  }

  public void setProduct(ProductEntity product) {
    this.product.set(product);
  }

  public static class Factory extends ViewModelProvider.NewInstanceFactory {
    @NonNull private final Application mApplication;
    private final int mProducId;

    public Factory(@NonNull Application application, int producId) {
      mApplication = application;
      mProducId = producId;
    }

    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
      return (T) new ProductViewModel(mApplication, mProducId);
    }
  }
}
