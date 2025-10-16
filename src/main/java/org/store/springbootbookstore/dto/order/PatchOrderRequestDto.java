package org.store.springbootbookstore.dto.order;

import org.store.springbootbookstore.model.Order;

public record PatchOrderRequestDto(
        Order.Status status
) {
}
