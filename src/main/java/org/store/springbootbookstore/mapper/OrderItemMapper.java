package org.store.springbootbookstore.mapper;

import org.mapstruct.*;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.orderItem.OrderItemResponseDto;
import org.store.springbootbookstore.model.CartItem;
import org.store.springbootbookstore.model.OrderItem;

import java.math.BigDecimal;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mappings({
            @Mapping(target = "bookId",
            source = "book.id"),
    })
    OrderItemResponseDto toDto(OrderItem model);

    @Mapping(target = "order", ignore = true)
    OrderItem fromCartItemToOrderItem(CartItem cartItem);

    @AfterMapping
    default void calculatePrice(CartItem cartItem, @MappingTarget OrderItem orderItem) {
        BigDecimal unitPrice = cartItem.getBook().getPrice();
        int quantity = cartItem.getQuantity();
        orderItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)));
    }

}
