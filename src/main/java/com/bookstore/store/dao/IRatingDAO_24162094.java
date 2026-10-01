package com.bookstore.store.dao;

import com.bookstore.store.entity.Rating_24162094;

import java.util.List;

public interface IRatingDAO_24162094 {
    Rating_24162094 save(Rating_24162094 rating);

    Rating_24162094 saveRating(Rating_24162094 rating);

    List<Rating_24162094> getRatingsByBookId(Integer bookId);
}