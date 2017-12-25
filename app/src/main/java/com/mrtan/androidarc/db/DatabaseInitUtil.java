package com.mrtan.androidarc.db;

import com.mrtan.androidarc.db.entity.CommentEntity;
import com.mrtan.androidarc.db.entity.ProductEntity;
import com.mrtan.androidarc.model.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author mrtan on 12/22/17.
 */

class DatabaseInitUtil {
  public static final String[] FIRST = new String[] {
      "Special edition", "New", "Cheap", "Quality", "Used"
  };

  public static final String[] SECOND = new String[] {
      "Three-headed Monkey", "Rubber Chicken", "Pint of Grog", "Monocle"
  };

  public static final String[] DESCRIPTION = new String[] {
      "is Finally here", "is recommended by Stan S. Stanman",
      "is the best sold product on Mêlée Island", "is \uD83D\uDCAF", "is ❤️", "is fine"
  };

  public static final String[] COMMENTS = new String[] {
      "Comment 1", "Comment 2", "Comment 3", "Comment 4", "Comment 5", "Comment 6"
  };

  static void initializeDb(AppDatabase db) {
    List<ProductEntity> products = new ArrayList<>(FIRST.length * SECOND.length);
    List<CommentEntity> comments = new ArrayList<>();

    generateData(products, comments);
    insertData(db, products, comments);
  }

  private static void generateData(List<ProductEntity> products, List<CommentEntity> comments) {
    Random rnd = new Random();
    for (int i = 0; i < FIRST.length; i++) {
      for (int j = 0; j < SECOND.length; j++) {
        String name = FIRST[i] + SECOND[j];
        ProductEntity pro = ProductEntity.builder()
            .name(name)
            .description(name + DESCRIPTION[j])
            .price(rnd.nextInt(240))
            .id(FIRST.length * i + j)
            .build();
        products.add(pro);
      }
    }

    for (ProductEntity product : products) {
      int commentsNumber = rnd.nextInt(5) + 1;
      for (int i = 0; i < commentsNumber; i++) {
        CommentEntity comment = CommentEntity.builder()
            .productId(product.getId())
            .text(COMMENTS[i] + "for" + product.getName())
            .postedAt(new Date(
                System.currentTimeMillis() - TimeUnit.DAYS.toMillis(commentsNumber - i)
                    + TimeUnit.HOURS.toMillis(i)))
            .build();
        comments.add(comment);
      }
    }
  }

  private static void insertData(AppDatabase db, List<ProductEntity> products, List<CommentEntity> comments) {
    db.beginTransaction();
    try {
      db.productDao().insertAll(products);
      db.commentDao().insertAll(comments);
      db.setTransactionSuccessful();
    } finally {
      db.endTransaction();
    }
  }
}
