package com.apigee.apimodel.iodocs.model.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class YesNoBooleanSerializer
  extends JsonSerializer<Boolean>
{
  @Override
  public void serialize(Boolean value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException, JsonProcessingException
  {
    jsonGenerator.writeString(value ? YesNoBoolean.YES.toString() : YesNoBoolean.NO.toString());
  }
}
