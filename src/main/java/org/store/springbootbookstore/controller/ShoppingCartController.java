package org.store.springbootbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;
import org.store.springbootbookstore.dto.shoppingcart.ShoppingCartResponserDto;
import org.store.springbootbookstore.service.ShoppingCartService;

@Tag(name = "Carts",
        description = "Endpoint for managing a shopping cart of a current user")
@RequiredArgsConstructor
@RestController
@RequestMapping("/carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a shopping cart",
            description = "Get a shopping cart that is attached to a User")
    public ShoppingCartResponserDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a book to a shopping cart",
            description = "Create a new cart item and attach it to a shopping cart")
    public ShoppingCartResponserDto createNewItem(
            @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.createNewItem(requestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update cart item",
            description = "Update an existing cart item with new quantity by its ID")
    public ShoppingCartResponserDto updateCartItemQuantity(@PathVariable Long id,
                              @RequestBody @Valid UpdateQuantityCartItemDto requestDto) {
        return shoppingCartService.updateById(id, requestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cart item",
            description = "Remove a cart item from the store by its ID")
    public void deleteCartItemById(@PathVariable Long id) {
        shoppingCartService.deleteById(id);
    }
}
