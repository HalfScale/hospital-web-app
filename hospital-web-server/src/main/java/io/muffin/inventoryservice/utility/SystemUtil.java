package io.muffin.inventoryservice.utility;

import io.muffin.inventoryservice.model.dto.GenericPageableResponse;
import org.springframework.data.domain.Page;

public class SystemUtil {

    public static <T> GenericPageableResponse<T> mapToGenericPageableResponse(Page<T> page) {
        return new GenericPageableResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
