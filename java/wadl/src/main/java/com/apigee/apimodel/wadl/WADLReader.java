package com.apigee.apimodel.wadl;

import com.apigee.api.wadl._2010._07.AuthenticationType;
import com.apigee.api.wadl._2010._07.ExampleType;
import com.apigee.api.wadl._2010._07.TagType;
import com.apigee.api.wadl._2010._07.TagsType;
import com.apigee.apimodel.*;
import com.apigee.apimodel.impl.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WADLReader
  implements APIReader
{
  private static final WADLReader myself = new WADLReader();
  
  private WADLReader()
  {
  }
  
  public static WADLReader get() {
    return myself;
  }
  
  public List<API> readModel(String name, InputStream in, Logger log)
    throws APIModelException, IOException
  {
    net.java.dev.wadl._2009._02.Application root;
    try {
      JAXBContext context = JAXBContext.newInstance(net.java.dev.wadl._2009._02.Application.class);
      Unmarshaller un = context.createUnmarshaller();
      root = (net.java.dev.wadl._2009._02.Application)un.unmarshal(in); 
      
    } catch (JAXBException e) {
      throw new APIModelException(e);
    }
    
    return parseWadl(name, root, log);
  }
  
  public List<API> readModel(String name, InputStream in)
      throws APIModelException, IOException
  {
    return readModel(name, in, Logger.getLogger(DEFAULT_READER_LOGGER));
  }

  private boolean isEmpty(String s) 
  {
    return ((s == null) || s.length() == 0);
  }
  
  /**
   * This method tries to collect all the various parts of the "Doc" elements in WADL
   * into the simpler representation that we have in the API model. This is the
   * beginning of the solution but since the models don't match well it's a bit icky.
   */
  private void collectDocs(List<net.java.dev.wadl._2009._02.Doc> docs, Documented t)
  {
    StringBuilder docStr;
    if (t.getDocumentation() != null) {
      docStr = new StringBuilder(t.getDocumentation());
    } else {
      docStr = new StringBuilder();
    }
    
    for (net.java.dev.wadl._2009._02.Doc d : docs) {
      if ((docs.size() > 1) || !isEmpty(d.getTitle())) {
        if (!isEmpty(d.getTitle())) {
          docStr.append("<b>")
                .append(d.getTitle())
                .append("</b></br>");
        }
        if (!isEmpty(d.getUrl())) {
          docStr.append("<a href=\"")
                .append(d.getUrl())
                .append("\">")
                  .append(d.getUrl())
                .append("</a>");
          if (t.getDocumentationURI() == null) {
            t.setDocumentationURI(d.getUrl());
          }
        }
        for (Object dc : d.getContent()) {
          String ct = dc.toString();
          if (ct != null) {
            docStr.append(TextUtil.trimWhitespace(ct));
            docStr.append("</br>");
          }
        }
        t.setDocumentation(docStr.toString());
      } else {
        if (!isEmpty(d.getUrl())) {
          t.setDocumentationURI(d.getUrl());
        }
        if (!d.getContent().isEmpty()) {
          for (Object dc : d.getContent()) {
            String ct = dc.toString();
            if (ct != null) {
              docStr.append(TextUtil.trimWhitespace(ct));
            }
          }
          t.setDocumentation(docStr.toString());
        }
      }
    }
  }
  
  private List<API> parseWadl(String name, net.java.dev.wadl._2009._02.Application root, Logger log)
    throws APIModelException
  {
    // TODO -- call getGrammars and add a grammar table
    // TODO -- call getDocs and set a high-level doc table
    for (Object o : root.getResourceTypeOrMethodOrRepresentation()) {
      if (o instanceof net.java.dev.wadl._2009._02.ResourceType) {
        rejectModel("Top-level resource types not supported", log);
      } else if (o instanceof net.java.dev.wadl._2009._02.Method) {
        rejectModel("Top-level methods not supported", log);
      } else if (o instanceof net.java.dev.wadl._2009._02.Representation) {
        rejectModel("Top-level representations not supported", log);
      } else {
        throw new AssertionError();
      }
    }
    
    int count = 0;
    ArrayList<API> models = new ArrayList<API>();
    for (net.java.dev.wadl._2009._02.Resources resList : root.getResources()) {
      API model = new APIImpl();
      if (count > 0) {
        // Is this the best way to handle WADLs that have multiple resource lists in them?
        model.setName(name + count);
      } else {
        model.setName(name);
      }
      model.setBasePath(resList.getBase());
      if (count == 0) {
        // This is a bit of a hack in case there are top-level docs in the WADL.
        collectDocs(root.getDoc(), model);
      }
      collectDocs(resList.getDoc(), model);
      for (net.java.dev.wadl._2009._02.Resource r : resList.getResource()) {
        parseResource(r, r.getPath(), model, log);
      }
      models.add(model);
      count++;
    }
    return models;
  }
  
  private void parseResource(net.java.dev.wadl._2009._02.Resource r,
                             String path, API model, Logger log)
    throws APIModelException
  { 
    Resource res = new ResourceImpl();
    
    if (!r.getType().isEmpty()) {
      rejectModel("Resource types not supported", log);
    }
    if (!r.getQueryType().equals("application/x-www-form-urlencoded")) {
      rejectModel("Non-default query types are not supported", log);
    }
    res.setPath(path);
    collectDocs(r.getDoc(), res);
    
    for (Object o : r.getParamOrChoice()) {
      if (o instanceof net.java.dev.wadl._2009._02.Param) {
        parseParamDef((net.java.dev.wadl._2009._02.Param)o, res, log);
      } else if (o instanceof com.apigee.api.wadl._2010._07.ChoiceType) {
        parseChoiceParamDef((com.apigee.api.wadl._2010._07.ChoiceType)o, res, log);
        
      } else {
        throw new AssertionError();
      }
    }
    
    for (Object o : r.getMethodOrResource()) {
      if (o instanceof net.java.dev.wadl._2009._02.Method) {
        net.java.dev.wadl._2009._02.Method meth = (net.java.dev.wadl._2009._02.Method)o;
        if (meth.getResponse().isEmpty()) {
          parseMethod(meth, null, res, log);
        } else {
          for (net.java.dev.wadl._2009._02.Response resp : meth.getResponse()) {
            parseMethod(meth, resp, res, log);
          }
        }
      } else if (o instanceof net.java.dev.wadl._2009._02.Resource) {
        net.java.dev.wadl._2009._02.Resource newRes = (net.java.dev.wadl._2009._02.Resource)o;
        parseResource(newRes, appendPath(path, newRes.getPath()), model, log);
      } else {
        throw new AssertionError();
      }
    }
    model.addResource(res);
  }
  
  private void parseChoiceParamDef(com.apigee.api.wadl._2010._07.ChoiceType cp, 
                                   Resource res, Logger log)
  {
    if (cp.getParam().isEmpty()) {
      return;
    }
    ChoiceParameterImpl choice = new ChoiceParameterImpl();
    if (cp.getCountMin() != null) {
      choice.setMinCount(cp.getCountMin().intValue());
    }
    if (cp.getCountMax() != null) {
      choice.setMaxCount(cp.getCountMax().intValue());
    }
    if (((cp.isRequired() != null) && cp.isRequired()) || 
        ((cp.getCountMin() != null) && (cp.getCountMin().intValue() > 0))) {
      choice.setRequired(true);
    }
    // Since choice goes in a map we need to name it something 
    // TODO we might want to clean this up
    choice.setName(cp.getParam().get(0).getName());
    
    switch (cp.getParam().get(0).getStyle()) {
    case TEMPLATE:
      res.addTemplateParameter(choice);
      break;
    case MATRIX:
      res.addMatrixParameter(choice);
      break;
    case QUERY:
      res.addQueryParameter(choice);
      break;
    case HEADER:
      res.addRequestHeader(choice);
      break;
    default:
      throw new AssertionError();
    }
    
    for (net.java.dev.wadl._2009._02.Param p : cp.getParam()) {
      Parameter param = parseParam(p);
      choice.addChoice(param);
    }
  }
  
  private void parseParamDef(net.java.dev.wadl._2009._02.Param p, Resource res, Logger log)
    throws APIModelException
  {
    if (p.getHref() != null) {
      rejectModel("Parameters with hrefs are not supported", log);
    }
    Parameter param;
    switch (p.getStyle()) {
    case TEMPLATE:
      param = parseParam(p);
      res.addTemplateParameter(param);
      break;
    case MATRIX:
      param = parseParam(p);
      res.addMatrixParameter(param);
      break;
    case QUERY:
      param = parseParam(p);
      res.addQueryParameter(param);
      break;
    case HEADER:
      param = parseParam(p);
      res.addRequestHeader(param);
      break;
    default:
      throw new AssertionError();
    }
  }
  
  private void parseMethod(net.java.dev.wadl._2009._02.Method m,
                           net.java.dev.wadl._2009._02.Response resp,
                           Resource res, Logger log)
    throws APIModelException
  {
    if (m.getHref() != null) {
      rejectModel("Methods with hrefs not supported", log);
    }
    OperationImpl op = new OperationImpl();
    op.setNodeId(m.getId());
    op.setMethod(m.getName());
    op.setDisplayName(m.getDisplayName());
    collectDocs(m.getDoc(), op);
    
    // TODO collect authentication from higher levels in the hierarchy
    AuthenticationType apigeeAuth = m.getAuthentication();
    if ((apigeeAuth != null) && Boolean.valueOf(apigeeAuth.getRequired())) {
      AuthenticationMethodImpl auth = new AuthenticationMethodImpl();
      if (isEmpty(apigeeAuth.getValue())) {
        auth.setName(AuthenticationMethodImpl.UNSPECIFIED);
      } else {
        auth.setName(apigeeAuth.getValue());
      }
      op.addAuthenticationMethod(auth);
    }
    
    // TODO collect examples from higher levels of the hierarchy
    ExampleType apigeeExample = m.getExample();
    if (apigeeExample != null) {
      ExampleImpl ex = new ExampleImpl();
      if (!isEmpty(apigeeExample.getUrl())) {
        ex.setUri(apigeeExample.getUrl());
      }
      if (!isEmpty(apigeeExample.getValue())) {
        ex.setContents(apigeeExample.getValue());
      }
      op.setExampleRequest(ex);
    }
    
    TagsType apigeeTags = m.getTags();
    if (apigeeTags != null) {
      for (TagType apigeeTag : apigeeTags.getTag()) {
        TagImpl tag = new TagImpl();
        if (Boolean.valueOf(apigeeTag.getPrimary())) {
          tag.setType(Tag.PRIMARY_CATEGORY_TYPE);
        } else {
          tag.setType(Tag.CATEGORY_TYPE);
        }
        tag.setValue(apigeeTag.getValue());
        op.addTag(tag);
      }
    }
    
    if (m.getRequest() != null) {
      for (net.java.dev.wadl._2009._02.Param p : m.getRequest().getParam()) {
        if (p.getHref() != null) {
          rejectModel("Parameters with hrefs are not supported", log);
        }
        Parameter param;
        switch (p.getStyle()) {
        case QUERY:
          param = parseParam(p);
          op.addQueryParameter(param);
          break;
        case HEADER:
          param = parseParam(p);
          op.addRequestHeader(param);
          break;
        default:
          throw new AssertionError();
        }
      }
      if (m.getRequest().getRepresentation().size() > 1) {
        rejectModel("Operations with multiple request representations are not supported", log);
      } else if (m.getRequest().getRepresentation().size() > 0) {
        op.setRequestRepresentation(parseRepresentation(m.getRequest().getRepresentation().get(0), log));
      }
    }
    
    if (resp != null) {
      for (net.java.dev.wadl._2009._02.Param p : resp.getParam()) {
        if (p.getHref() != null) {
          rejectModel("Parameters with hrefs are not supported", log);
        }
        Parameter param;
        switch (p.getStyle()) {
        case HEADER:
          param = parseParam(p);
          op.addResponseHeader(param);
          break;
        default:
          throw new AssertionError();
        }
      }
      if (resp.getRepresentation().size() > 1) {
        rejectModel("Operations with multiple response representations are not supported", log);
      } else if (resp.getRepresentation().size() > 0) {
        op.setResponseRepresentation(parseRepresentation(resp.getRepresentation().get(0), log));
      }
      for (Long code : resp.getStatus()) {
        ResponseCodeImpl rc = new ResponseCodeImpl();
        rc.setCode(code.intValue());
        rc.setSuccess(true);
        op.addResponseCode(rc);
      }
    }
   
    res.addOperation(op);
  }
  
  private Parameter parseParam(net.java.dev.wadl._2009._02.Param p)
  {
    ParameterImpl param = new ParameterImpl();
    param.setName(p.getName());
    param.setDataType(p.getType().getLocalPart());
    param.setRepeating(p.isRepeating());
    param.setRequired(p.isRequired());
    param.setDefault(p.getDefault());
    collectDocs(p.getDoc(), param);
    
    for (net.java.dev.wadl._2009._02.Option opt : p.getOption()) {
      ValidValueImpl choice = new ValidValueImpl();
      choice.setValue(opt.getValue());
      choice.setMediaType(opt.getMediaType());
      param.addValidValue(choice);
    }
    
    return param;
  }
  
  private Representation parseRepresentation(net.java.dev.wadl._2009._02.Representation rep, Logger log)
    throws APIModelException
  {
    if (!rep.getParam().isEmpty()) {
      rejectModel("Representation with parameters not supported", log);
    }
    if (!rep.getProfile().isEmpty()) {
      rejectModel("Representation with profile not supported", log);
    }
    RepresentationImpl r = new RepresentationImpl();
    // TODO need to understand WADL representations better -- not sure if this is right
    r.setDefinition(rep.getPayload());
    r.setMediaType(r.getMediaType());
    r.setLink(rep.getHref());
    collectDocs(rep.getDoc(), r);
    return r;
  }
  
  private static String appendPath(String base, String path)
  {
    StringBuilder r = new StringBuilder(base);
    if (!base.endsWith("/") && !path.startsWith("/")) {
      r.append('/');
    }
    r.append(path);
   
    return r.toString();
  }
  
  private static void rejectModel(String msg, Logger log)
    throws APIModelException
  {
    log.severe(msg);
    throw new APIModelException(msg);
  }
}
