package com.mrtan.androidarc.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.mrtan.androidarc.model.Comment;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author mrtan on 12/21/17.
 */

@Entity(tableName = "comments", foreignKeys = {
    @ForeignKey( entity = ProductEntity.class,
    parentColumns = "id",
    childColumns = "productId",
    onDelete = ForeignKey.CASCADE)}, indices = {
    @Index(value = "productId")
})
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity implements Comment {

  @PrimaryKey (autoGenerate = true) private int id;
  private int productId;
  private String text;
  private Date postedAt;

  @Override public int getId() {
    return id;
  }

  @Override public int getProductId() {
    return productId;
  }

  @Override public String getText() {
    return text;
  }

  @Override public Date getPostedAt() {
    return postedAt;
  }

  public CommentEntity(Comment comment) {
    id = comment.getId();
    productId = comment.getProductId();
    text = comment.getText();
    postedAt = comment.getPostedAt();
  }
}
