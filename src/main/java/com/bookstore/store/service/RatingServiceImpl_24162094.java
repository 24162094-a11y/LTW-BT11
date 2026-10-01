package com.bookstore.store.service;

import com.bookstore.store.dao.IRatingDAO_24162094;
import com.bookstore.store.entity.Rating_24162094;

import java.util.List;

public class RatingServiceImpl_24162094 implements IRatingService_24162094 {
    private final IRatingDAO_24162094 ratingDAO;

    public RatingServiceImpl_24162094(IRatingDAO_24162094 ratingDAO) {
        this.ratingDAO = ratingDAO;
    }

    @Override
    public Rating_24162094 save(Rating_24162094 rating) {
        return ratingDAO.save(rating);
    }

    @Override
    public Rating_24162094 saveRating(Rating_24162094 rating) {
        return ratingDAO.saveRating(rating);
    }

    @Override
    public List<Rating_24162094> getRatingsByBookId(Integer bookId) {
        return ratingDAO.getRatingsByBookId(bookId);
    }
}