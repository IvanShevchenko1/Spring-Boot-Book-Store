package org.store.springbootbookstore.mapper;

import org.mapstruct.Mapper;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.shoppingcart.ShoppingCartResponserDto;
import org.store.springbootbookstore.model.ShoppingCart;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    ShoppingCartResponserDto toDto(ShoppingCart shoppingCart);
}
