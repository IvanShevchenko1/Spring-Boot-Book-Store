package org.store.springbootbookstore.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.cartitem.CartItemResponseDto;
import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;
import org.store.springbootbookstore.model.CartItem;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mappings({
            @Mapping(target = "book",
                     source = "bookId",
                     qualifiedByName = "bookFromId"),
            @Mapping(target = "shoppingCart",
                     ignore = true)

    })
    CartItem toModel(CreateCartItemRequestDto newCartItemRequestDto);

    @Mappings({
            @Mapping(target = "bookId", source = "book.id")
    })
    CartItemResponseDto toDto(CartItem cartItem);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateQuantityCartItemDto requestDto, @MappingTarget CartItem entity);
}
