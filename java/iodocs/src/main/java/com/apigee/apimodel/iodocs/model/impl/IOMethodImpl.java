package com.apigee.apimodel.iodocs.model.impl;

import com.apigee.apimodel.iodocs.model.IOMethod;
import com.apigee.apimodel.iodocs.model.IOParameter;
import com.apigee.apimodel.iodocs.model.json.YesNoBooleanDeserializer;
import com.apigee.apimodel.iodocs.model.json.YesNoBooleanSerializer;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

public class IOMethodImpl
  implements IOMethod
{
  private String MethodName;
  private String Synopsis;
  private String HTTPMethod;
  private String URI;
  private boolean RequiresOAuth;
  @JsonDeserialize(contentAs = IOParameterImpl.class)
  private final List<IOParameter> parameters = new ArrayList<IOParameter>();

  @JsonProperty(value = "MethodName")
  public String getMethodName()
  {
    return MethodName;
  }

  @JsonProperty(value = "MethodName")
  public void setMethodName(String methodName)
  {
    MethodName = methodName;
  }

  @JsonProperty(value = "Synopsis")
  public String getSynopsis()
  {
    return Synopsis;
  }

  @JsonProperty(value = "Synopsis")
  public void setSynopsis(String synopsis)
  {
    Synopsis = synopsis;
  }

  @JsonProperty(value = "HTTPMethod")
  public String getHTTPMethod()
  {
    return HTTPMethod;
  }

  @JsonProperty(value = "HTTPMethod")
  public void setHTTPMethod(String HTTPMethod)
  {
    this.HTTPMethod = HTTPMethod;
  }

  @JsonProperty(value = "URI")
  public String getURI()
  {
    return URI;
  }

  @JsonProperty(value = "URI")
  public void setURI(String URI)
  {
    this.URI = URI;
  }

  @JsonProperty(value = "RequiresOAuth")
  @JsonSerialize(using = YesNoBooleanSerializer.class)
  public boolean isRequiresOAuth()
  {
    return RequiresOAuth;
  }

  @JsonProperty(value = "RequiresOAuth")
  @JsonDeserialize(using = YesNoBooleanDeserializer.class)
  public void setRequiresOAuth(boolean requiresOAuth)
  {
    RequiresOAuth = requiresOAuth;
  }

  public List<IOParameter> getParameters()
  {
    return parameters;
  }

  public void addParameter(IOParameter parameter)
  {
    parameters.add(parameter);
  }

}
