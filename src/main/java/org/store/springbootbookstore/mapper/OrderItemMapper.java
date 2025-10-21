package org.store.springbootbookstore.mapper;

import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.orderitem.OrderItemResponseDto;
import org.store.springbootbookstore.model.CartItem;
import org.store.springbootbookstore.model.OrderItem;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mappings({
            @Mapping(target = "bookId", source = "book.id"),
    })
    OrderItemResponseDto toDto(OrderItem model);

    @Mappings({
            @Mapping(target = "order", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    OrderItem fromCartItemToOrderItem(CartItem cartItem);

    @AfterMapping
    default void calculatePrice(CartItem cartItem, @MappingTarget OrderItem orderItem) {
        BigDecimal unitPrice = cartItem.getBook().getPrice();
        int quantity = cartItem.getQuantity();
        orderItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)));
    }
}
