package com.mrtan.androidarc.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.mrtan.androidarc.db.entity.ProductEntity;
import java.util.List;

/**
 * @author mrtan on 12/21/17.
 */
@Dao
public interface ProductDao {

  @Query("SELECT * FROM products")
  LiveData<List<ProductEntity>> loadAllProducts();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertAll(List<ProductEntity> products);

  @Query("SELECT * FROM products WHERE id = :productId")
  LiveData<ProductEntity> loadProduct(int productId);

  @Query("SELECT * FROM products WHERE id = :productId")
  ProductEntity loadProductSync(int productId);
}
