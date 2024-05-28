package com.marceljsh.binarfud.payload.response;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

@Data
@Builder
@JsonSerialize(using = PagedResponseSerializer.class)
public class PagedResponse<T> {

  // use transient to avoid normal serialization
  private transient String contentName;

  private List<T> content;

  private int currentPage;

  private int totalPages;

  private int size;

  public static <T> PagedResponse<T> of(String contentName, Page<T> page) {
    return PagedResponse.<T>builder()
        .contentName(contentName)
        .content(page.getContent())
        .currentPage(page.getNumber() + 1)
        .totalPages(page.getTotalPages())
        .size(page.getSize())
        .build();
  }
}

class PagedResponseSerializer<T> extends JsonSerializer<PagedResponse<T>> {

  @Override
  public void serialize(PagedResponse<T> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeObjectField(value.getContentName(), value.getContent());
    gen.writeNumberField("current_page", value.getCurrentPage());
    gen.writeNumberField("total_pages", value.getTotalPages());
    gen.writeNumberField("size", value.getSize());
    gen.writeEndObject();
  }
}
