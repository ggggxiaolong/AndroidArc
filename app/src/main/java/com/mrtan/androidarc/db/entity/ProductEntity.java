package com.mrtan.androidarc.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.mrtan.androidarc.model.Product;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mrtan on 12/21/17.
 */
@Entity(tableName = "products")
@Setter
@NoArgsConstructor
public class ProductEntity implements Product {
  @PrimaryKey private int id;
  private String name;
  private String description;
  private int price;

  @Override public int getId() {
    return id;
  }

  @Override public String getName() {
    return name;
  }

  @Override public String getDescription() {
    return description;
  }

  @Override public int getPrice() {
    return price;
  }

  public ProductEntity(Product product) {
    id = product.getId();
    name = product.getName();
    description = product.getDescription();
    price = product.getPrice();
  }
}
