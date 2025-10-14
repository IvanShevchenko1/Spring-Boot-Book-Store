package org.store.springbootbookstore.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;
import org.store.springbootbookstore.dto.shoppingcart.ShoppingCartResponserDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.CartItemMapper;
import org.store.springbootbookstore.mapper.ShoppingCartMapper;
import org.store.springbootbookstore.model.CartItem;
import org.store.springbootbookstore.model.ShoppingCart;
import org.store.springbootbookstore.model.User;
import org.store.springbootbookstore.repository.CartItemRepository;
import org.store.springbootbookstore.repository.ShoppingCartRepository;
import org.store.springbootbookstore.repository.UserRepository;
import org.store.springbootbookstore.service.ShoppingCartService;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartResponserDto getShoppingCart() {
        ShoppingCart cart = getOrCreateCurrentUserCart();
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponserDto createNewItem(CreateCartItemRequestDto requestDto) {
        ShoppingCart cart = getOrCreateCurrentUserCart();

        Optional<CartItem> existingOpt =
                cartItemRepository.findByShoppingCartIdAndBookId(cart.getId(), requestDto.bookId());

        CartItem toSave;
        if (existingOpt.isPresent()) {
            CartItem existing = existingOpt.get();
            int newQty = existing.getQuantity() + requestDto.quantity();
            existing.setQuantity(newQty);
            toSave = existing;
        } else {
            CartItem newItem = cartItemMapper.toModel(requestDto); // maps bookId -> Book
            newItem.setShoppingCart(cart);
            toSave = newItem;
        }

        cartItemRepository.save(toSave);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartResponserDto updateById(Long id, UpdateQuantityCartItemDto requestDto) {
        ShoppingCart cart = getOrCreateCurrentUserCart();
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + id));

        cartItemMapper.updateEntity(requestDto, cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void deleteById(Long id) {
        ShoppingCart cart = getOrCreateCurrentUserCart();
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item by id: "
                        + id));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void createCartForUser(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        shoppingCartRepository.save(cart);
    }

    private ShoppingCart getOrCreateCurrentUserCart() {
        User user = getCurrentUser();
        Optional<ShoppingCart> cartOpt = shoppingCartRepository.findByUserId(user.getId());
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        User authenticatedUser = (User) authentication.getPrincipal();
        return userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: "
                        + authenticatedUser.getId()));
    }
}
