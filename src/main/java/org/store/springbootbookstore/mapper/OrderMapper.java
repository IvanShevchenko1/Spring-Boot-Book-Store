package org.store.springbootbookstore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.model.Order;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "orderItems", source = "orderItems")
    })
    OrderResponseDto toDto(Order model);

}
