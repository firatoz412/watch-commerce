package com.watch.commerce.service.favorite;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Favorite;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.FavoriteRepository;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.repository.UserRepository;
import com.watch.commerce.service.product.ProductService;

@Service
public class FavoriteService implements IFavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           ProductService productService) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Override
    public void addFavorite(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + email));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + productId));

        // Aynı ürünün tekrar eklenmesini engelle
        if (favoriteRepository.existsByUserAndProduct(user, product)) {
            return;
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + email));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + productId));

        favoriteRepository.deleteByUserAndProduct(user, product);
    }

    @Override
    public List<ProductDto> getFavorites(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + email));

        List<Favorite> favorites = favoriteRepository.findByUser(user);
        return favorites.stream()
                .map(fav -> productService.convertToDto(fav.getProduct()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(String email, Long productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + email));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Ürün bulunamadı: " + productId));

        return favoriteRepository.existsByUserAndProduct(user, product);
    }

    @Override
    public Set<Long> getFavoriteProductIds(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + email));

        List<Favorite> favorites = favoriteRepository.findByUser(user);
        Set<Long> ids = new HashSet<>();
        for (Favorite fav : favorites) {
            ids.add(fav.getProduct().getId());
        }
        return ids;
    }

}
