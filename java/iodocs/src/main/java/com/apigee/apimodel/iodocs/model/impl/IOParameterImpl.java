package com.apigee.apimodel.iodocs.model.impl;

import com.apigee.apimodel.iodocs.model.IOParameter;
import com.apigee.apimodel.iodocs.model.json.YesNoBooleanDeserializer;
import com.apigee.apimodel.iodocs.model.json.YesNoBooleanSerializer;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class IOParameterImpl
  implements IOParameter
{
  private String Name;
  private boolean Required;
  private String Default;
  private String Type;

  @JsonProperty(value = "EnumeratedList")
  private final List<String> EnumeratedList = new ArrayList<String>();

  private String Description;

  @JsonProperty(value = "Name")
  public String getName()
  {
    return Name;
  }

  @JsonProperty(value = "Name")
  public void setName(String name)
  {
    Name = name;
  }

  @JsonProperty(value = "Required")
  @JsonSerialize(using = YesNoBooleanSerializer.class)
  public boolean isRequired()
  {
    return Required;
  }

  @JsonProperty(value = "Required")
  @JsonDeserialize(using = YesNoBooleanDeserializer.class)
  public void setRequired(boolean required)
  {
    Required = required;
  }

  @JsonProperty(value = "Default")
  public String getDefault()
  {
    return Default;
  }

  @JsonProperty(value = "Default")
  public void setDefault(String aDefault)
  {
    Default = aDefault;
  }

  @JsonProperty(value = "Type")
  public String getType()
  {
    return Type;
  }

  @JsonProperty(value = "Type")
  public void setType(String type)
  {
    Type = type;
  }

  @JsonProperty(value = "EnumeratedList")
  @JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
  public List<String> getEnumeratedList()
  {
    return EnumeratedList;
  }

  public void addEnumeratedValue(String value)
  {
    EnumeratedList.add(value);
  }

  @JsonProperty(value = "Description")
  public String getDescription()
  {
    return Description;
  }

  @JsonProperty(value = "Description")
  public void setDescription(String description)
  {
    Description = description;
  }
}
