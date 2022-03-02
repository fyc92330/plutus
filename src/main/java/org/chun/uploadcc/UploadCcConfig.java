package org.chun.uploadcc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class UploadCcConfig {
  static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false)
      .enable(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)
      .registerModule(new JavaTimeModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .setSerializationInclusion(JsonInclude.Include.NON_NULL);
}
