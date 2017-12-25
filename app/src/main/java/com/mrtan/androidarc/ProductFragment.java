package com.mrtan.androidarc;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mrtan.androidarc.databinding.ProductFragmentBinding;
import com.mrtan.androidarc.ui.CommentAdapter;
import com.mrtan.androidarc.ui.CommentClickCallBack;
import com.mrtan.androidarc.viewmodel.ProductViewModel;

/**
 * @author mrtan on 12/25/17.
 */

public class ProductFragment extends Fragment {

  private static final String KEY_PRODUCT_ID = "product_id";
  private ProductFragmentBinding mBinding;
  private CommentAdapter mCommentAdapter;

  private final CommentClickCallBack mCommentClickCallBack = comment -> {
    //nothing to do
  };

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false);
    mCommentAdapter = new CommentAdapter(mCommentClickCallBack);
    mBinding.commentList.setAdapter(mCommentAdapter);
    return mBinding.getRoot();
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    assert getActivity() != null;
    assert getArguments() != null;
    ProductViewModel.Factory factory =
        new ProductViewModel.Factory(getActivity().getApplication(),
            getArguments().getInt(KEY_PRODUCT_ID));
    final ProductViewModel model = ViewModelProviders.of(this, factory)
        .get(ProductViewModel.class);
    mBinding.setProductViewModel(model);
    subscribeToModel(model);
  }

  private void subscribeToModel(ProductViewModel model) {
    model.getObserverProduct().observe(this, model::setProduct);

    model.getComments().observe(this, commentEntities -> {
      if (commentEntities != null){
        mBinding.setIsLoading(false);
        mCommentAdapter.setComments(commentEntities);
      } else {
        mBinding.setIsLoading(true);
      }
    });
  }

  public static ProductFragment forProduct(int productId) {
    ProductFragment fragment = new ProductFragment();
    Bundle args = new Bundle();
    args.putInt(KEY_PRODUCT_ID, productId);
    fragment.setArguments(args);
    return fragment;
  }
}
