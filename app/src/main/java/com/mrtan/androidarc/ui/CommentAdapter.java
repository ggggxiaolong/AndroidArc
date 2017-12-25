package com.mrtan.androidarc.ui;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.mrtan.androidarc.R;
import com.mrtan.androidarc.databinding.CommentItemBinding;
import com.mrtan.androidarc.model.Comment;
import java.util.List;
import java.util.Objects;

/**
 * @author mrtan on 12/22/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

  private List<? extends Comment> mComments;

  @Nullable private final CommentClickCallBack mCommentClickCallBack;

  public CommentAdapter(@Nullable CommentClickCallBack commentClickCallBack) {
    mCommentClickCallBack = commentClickCallBack;
  }

  public void setComments(final List<? extends Comment> comments) {
    if (mComments == null) {
      mComments = comments;
      notifyItemRangeInserted(0, mComments.size());
    } else {
      DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
        @Override public int getOldListSize() {
          return mComments.size();
        }

        @Override public int getNewListSize() {
          return comments.size();
        }

        @Override public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
          Comment old = mComments.get(oldItemPosition);
          Comment comment = comments.get(newItemPosition);
          return old.getId() == comment.getId();
        }

        @Override public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
          Comment old = mComments.get(oldItemPosition);
          Comment comment = comments.get(newItemPosition);
          return old.getId() == comment.getId()
              && old.getPostedAt() == comment.getPostedAt()
              && old.getProductId() == comment.getProductId()
              && Objects.equals(old.getText(), comment.getText());
        }
      });
      mComments = comments;
      diffResult.dispatchUpdatesTo(this);
    }
  }

  @Override public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    CommentItemBinding binding =
        DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.comment_item,
            parent, false);
    assert binding != null;
    binding.setCallback(mCommentClickCallBack);
    return new CommentViewHolder(binding);
  }

  @Override public void onBindViewHolder(CommentViewHolder holder, int position) {
    holder.mBinding.setComment(mComments.get(position));
    holder.mBinding.executePendingBindings();
  }

  @Override public int getItemCount() {
    return mComments == null? 0 : mComments.size();
  }

  static class CommentViewHolder extends RecyclerView.ViewHolder {
    final CommentItemBinding mBinding;
    CommentViewHolder(CommentItemBinding binding) {
      super(binding.getRoot());
      mBinding = binding;
    }
  }

}
