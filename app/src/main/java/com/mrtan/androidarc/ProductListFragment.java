package com.mrtan.androidarc;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mrtan.androidarc.databinding.ListFragmentBinding;
import com.mrtan.androidarc.ui.ProductAdapter;
import com.mrtan.androidarc.ui.ProductClickCallback;
import com.mrtan.androidarc.viewmodel.ProductListViewModel;

/**
 * @author mrtan on 12/25/17.
 */

public class ProductListFragment extends Fragment {
  public static final String TAG = "ProductListViewModel";
  private ProductAdapter mProductAdapter;
  private ListFragmentBinding mBinding;

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
    mProductAdapter = new ProductAdapter(mProductClickCallback);
    mBinding.productsList.setAdapter(mProductAdapter);
    return mBinding.getRoot();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    ProductListViewModel model =
        ViewModelProviders.of(this).get(ProductListViewModel.class);
    subscribeUi(model);
  }

  private void subscribeUi(ProductListViewModel model) {
    model.getProducts().observe(this, productEntities -> {
      if (productEntities != null){
        mBinding.setIsLoading(false);
        mProductAdapter.setProducts(productEntities);
      } else {
        mBinding.setIsLoading(true);
      }
    });
    mBinding.executePendingBindings();
  }

  private final ProductClickCallback mProductClickCallback= product -> {
    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)){
      assert getActivity() != null;
      ((MainActivity) getActivity()).show(product);
    }
  };
}
