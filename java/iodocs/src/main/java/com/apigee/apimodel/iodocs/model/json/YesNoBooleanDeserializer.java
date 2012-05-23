package com.apigee.apimodel.iodocs.model.json;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

public class YesNoBooleanDeserializer
  extends JsonDeserializer<Boolean>
{
  private static final Class<?> valueClass = Boolean.class;

  @Override
  public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
    throws IOException, JsonProcessingException
  {
    JsonToken t = jsonParser.getCurrentToken();

    if (t == JsonToken.VALUE_TRUE) {
      return Boolean.TRUE;
    } else if (t == JsonToken.VALUE_FALSE) {
      return Boolean.FALSE;
    } else if (t == JsonToken.VALUE_STRING) {
      String text = jsonParser.getText().trim();

      if (text.equals(YesNoBoolean.YES.toString())) {
        return Boolean.TRUE;
      } else if (text.equals(YesNoBoolean.NO.toString())) {
        return Boolean.FALSE;
      }

      throw deserializationContext.weirdStringException(
        valueClass,
        "value not recognized"
      );
    }

    throw deserializationContext.mappingException(valueClass);
  }
}
