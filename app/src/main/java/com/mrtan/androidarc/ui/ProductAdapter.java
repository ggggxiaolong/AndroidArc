package com.mrtan.androidarc.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mrtan.androidarc.R;
import com.mrtan.androidarc.databinding.ProductItemBinding;
import com.mrtan.androidarc.model.Product;
import java.util.List;
import java.util.Objects;

/**
 * @author mrtan on 12/25/17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

  List<? extends Product> mProducts;

  @Nullable private final ProductClickCallback mProductClickCallback;

  public ProductAdapter(@Nullable ProductClickCallback productClickCallback) {
    mProductClickCallback = productClickCallback;
  }

  public void setProducts(final List<? extends Product> products) {
    if (mProducts == null) {
      mProducts = products;
      notifyItemRangeInserted(0, products.size());
    } else {
      DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
        @Override public int getOldListSize() {
          return mProducts.size();
        }

        @Override public int getNewListSize() {
          return products.size();
        }

        @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
          return mProducts.get(oldItemPosition).getId() ==
              products.get(newItemPosition).getId();
        }

        @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
          Product oldP = mProducts.get(oldItemPosition);
          Product newP = products.get(newItemPosition);
          return newP.getId() == oldP.getId()
              && Objects.equals(newP.getDescription(), oldP.getDescription())
              && Objects.equals(newP.getName(), oldP.getName())
              && oldP.getPrice() == newP.getPrice();
        }
      });
      mProducts = products;
      result.dispatchUpdatesTo(this);
    }
  }

  @Override public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ProductItemBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item,
            parent, false);
    assert binding != null;
    binding.setCallback(mProductClickCallback);
    return new ProductViewHolder(binding);
  }

  @Override public void onBindViewHolder(ProductViewHolder holder, int position) {
    holder.binding.setProduct(mProducts.get(position));
    holder.binding.executePendingBindings();
  }

  @Override public int getItemCount() {
    return mProducts == null ? 0 : mProducts.size();
  }

  static class ProductViewHolder extends RecyclerView.ViewHolder {
    final ProductItemBinding binding;

    ProductViewHolder(ProductItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
