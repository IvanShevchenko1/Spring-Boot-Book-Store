package org.store.springbootbookstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.dto.order.PatchOrderRequestDto;
import org.store.springbootbookstore.model.Order;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "orderItems", source = "orderItems")
    })
    OrderResponseDto toDto(Order model);

    Order toModel(CreateOrderRequestDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PatchOrderRequestDto dto, @MappingTarget Order entity);

}
