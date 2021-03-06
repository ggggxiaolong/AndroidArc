package com.mrtan.androidarc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.mrtan.androidarc.model.Product;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (savedInstanceState == null) {
      ProductListFragment fragment = new ProductListFragment();
      getSupportFragmentManager().beginTransaction()
          .add(R.id.fragment_container, fragment, ProductListFragment.TAG)
          .commit();
    }
  }

  public void show(Product product) {
    ProductFragment fragment = ProductFragment.forProduct(product.getId());
    getSupportFragmentManager()
        .beginTransaction()
        .addToBackStack("product")
        .replace(R.id.fragment_container, fragment, null)
        .commit();
  }
}
