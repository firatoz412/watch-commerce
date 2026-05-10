package com.watch.commerce.service.favorite;

import java.util.List;
import java.util.Set;

import com.watch.commerce.dto.ProductDto;

public interface IFavoriteService {

    void addFavorite(String email, Long productId);

    void removeFavorite(String email, Long productId);

    List<ProductDto> getFavorites(String email);

    boolean isFavorite(String email, Long productId);

    Set<Long> getFavoriteProductIds(String email);

}
