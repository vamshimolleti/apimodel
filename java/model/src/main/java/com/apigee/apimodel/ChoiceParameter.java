package com.apigee.apimodel;

import java.util.Collection;

/**
 * A ChoiceParameter is a special type of Parameter that represents a group of parameters.
 * The user must include zero, one, or more members of the group based on the "maxCount" and 
 * "minCount" parameters.
 */

public interface ChoiceParameter
  extends Parameter
{
  Collection<Parameter> getChoices();
  void addChoice(Parameter choice);
  
  int getMinCount();
  void setMinCount(int count);
  
  int getMaxCount();
  void setMaxCount(int count);
}
