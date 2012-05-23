package com.apigee.apimodel.wadl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.apigee.api.wadl._2010._07.AuthenticationType;
import com.apigee.api.wadl._2010._07.ChoiceType;
import com.apigee.api.wadl._2010._07.ExampleType;
import com.apigee.api.wadl._2010._07.TagType;
import com.apigee.api.wadl._2010._07.TagsType;
import com.apigee.apimodel.API;
import com.apigee.apimodel.APIWriter;
import com.apigee.apimodel.AuthenticationMethod;
import com.apigee.apimodel.ChoiceParameter;
import com.apigee.apimodel.Documented;
import com.apigee.apimodel.Operation;
import com.apigee.apimodel.Parameter;
import com.apigee.apimodel.Representation;
import com.apigee.apimodel.Resource;
import com.apigee.apimodel.ResponseCode;
import com.apigee.apimodel.Tag;
import com.apigee.apimodel.ValidValue;

import net.java.dev.wadl._2009._02.ParamStyle;

public class WADLWriter 
  implements APIWriter
{
  private static final WADLWriter writer = new WADLWriter();
  
  private static net.java.dev.wadl._2009._02.ObjectFactory factory = new net.java.dev.wadl._2009._02.ObjectFactory();
  private static com.apigee.api.wadl._2010._07.ObjectFactory apigeeFactory = new com.apigee.api.wadl._2010._07.ObjectFactory();
  
  private WADLWriter()
  {
    
  }
  
  public static WADLWriter get() {
    return writer;
  }
  
  public void writeModel(List<API> models, OutputStream out, Logger log)
    throws IOException
  {
    net.java.dev.wadl._2009._02.Application root = factory.createApplication();
    for (API m : models) {
      net.java.dev.wadl._2009._02.Resources resList = factory.createResources();
      resList.setBase(m.getBasePath());
      for (Resource r : m.getResources()) {
        dumpResource(resList, r, log);
      }
      root.getResources().add(resList);
    }
    
    try {
      JAXBContext context = JAXBContext.newInstance(net.java.dev.wadl._2009._02.Application.class);
      Marshaller marsh = context.createMarshaller();
      marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marsh.marshal(root, out);
    } catch (JAXBException jaxe) {
      throw new IOException(jaxe);
    }
  }
  
  public void writeModel(List<API> models, OutputStream out)
    throws IOException
  {
    writeModel(models, out, Logger.getLogger(DEFAULT_WRITER_LOGGER));
  }
  
  private void dumpResource(net.java.dev.wadl._2009._02.Resources resList, Resource r, Logger log)
  {
    net.java.dev.wadl._2009._02.Resource res = factory.createResource();
    res.setPath(r.getPath());
    
    for (Parameter up : r.getTemplateParameters()) {
      res.getParamOrChoice().add(createParameter(up, ParamStyle.TEMPLATE, log));
    }
    for (Parameter hh : r.getRequestHeaders().values()) {
      res.getParamOrChoice().add(createParameter(hh, ParamStyle.HEADER, log));
    }
    for (Parameter qp : r.getQueryParameters().values()) {
      res.getParamOrChoice().add(createParameter(qp, ParamStyle.QUERY, log));
    }
    
    for (Operation o : r.getOperations()) {
      res.getMethodOrResource().add(createMethod(o, log));
    }
    
    net.java.dev.wadl._2009._02.Doc docs = attachDocs(r);
    if (docs != null) {
      res.getDoc().add(docs);
    }
    
    resList.getResource().add(res);
  }
  
  private Object createParameter(Parameter p, ParamStyle style, Logger log)
  {
    if (p instanceof ChoiceParameter) {
      ChoiceParameter cp = (ChoiceParameter)p;
      ChoiceType apigeeChoice = apigeeFactory.createChoiceType();
      if (p.isRequired()) {
        apigeeChoice.setRequired(true);
      }
      apigeeChoice.setCountMin(BigInteger.valueOf(cp.getMinCount()));
      apigeeChoice.setCountMax(BigInteger.valueOf(cp.getMaxCount()));
      for (Parameter cpp : cp.getChoices()) {
        apigeeChoice.getParam().add(createRegularParameter(cpp, style, log));
      }
      return apigeeChoice;
    }
    return createRegularParameter(p, style, log);
  }
  
  private net.java.dev.wadl._2009._02.Param createRegularParameter(Parameter p, ParamStyle style, Logger log)
  {
    net.java.dev.wadl._2009._02.Param np = factory.createParam();
    np.setName(p.getName());
    np.setType(new QName("xsd:" + p.getDataType()));
    np.setStyle(style);
    np.setDefault(p.getDefault());
    if (p.isRequired()) {
      np.setRequired(true);
    }
    if (p.isRepeating()) {
      np.setRepeating(true);
    }
    
    for (ValidValue c: p.getValidValues()) {
      net.java.dev.wadl._2009._02.Option opt = factory.createOption();
      opt.setValue(c.getValue());
      opt.setMediaType(c.getMediaType());
      np.getOption().add(opt);
    }
    
    net.java.dev.wadl._2009._02.Doc docs = attachDocs(p);
    if (docs != null) {
      np.getDoc().add(docs);
    }
    
    return np;
  }
  
  private net.java.dev.wadl._2009._02.Method createMethod(Operation op, Logger log)
  {
    net.java.dev.wadl._2009._02.Method meth = factory.createMethod();

    meth.setName(op.getMethod());
    meth.setId(op.getNodeId());
    if (!isEmpty(op.getDisplayName())) {
      meth.setDisplayName(op.getDisplayName());
    }
    
    if (op.getAuthenticationMethods().isEmpty()) {
      AuthenticationType apigeeAuth = apigeeFactory.createAuthenticationType();
      apigeeAuth.setRequired("false");
      meth.setAuthentication(apigeeAuth);
    } else {
      for (AuthenticationMethod auth : op.getAuthenticationMethods()) {
        AuthenticationType apigeeAuth = apigeeFactory.createAuthenticationType();
        apigeeAuth.setRequired("true");
        if (!auth.getName().equals(AuthenticationMethod.UNSPECIFIED)) {
          apigeeAuth.setValue(auth.getName());
        }
        meth.setAuthentication(apigeeAuth);
      }
    }
    
    if (op.getExampleRequest() != null) {
      ExampleType apigeeEx = apigeeFactory.createExampleType();
      apigeeEx.setUrl(op.getExampleRequest().getUri());
      apigeeEx.setValue(op.getExampleRequest().getContents());
      meth.setExample(apigeeEx);
    }
    
    if (!op.getTags().isEmpty()) {
      TagsType apigeeTags = apigeeFactory.createTagsType();
      for (Tag tag : op.getTags()) {
        if (tag.getType().equals(Tag.PRIMARY_CATEGORY_TYPE) ||
            tag.getType().equals(Tag.CATEGORY_TYPE)) {
          TagType aTag = apigeeFactory.createTagType();
          if (tag.getType().equals(Tag.PRIMARY_CATEGORY_TYPE)) {
            aTag.setPrimary("true");
          }
          aTag.setValue(tag.getValue());
          apigeeTags.getTag().add(aTag);
        }
      }
      meth.setTags(apigeeTags);
    }
    
    if (!op.getRequestHeaders().isEmpty() || !op.getQueryParameters().isEmpty() ||
        (op.getRequestRepresentation() != null)) {
      net.java.dev.wadl._2009._02.Request req = factory.createRequest();
      meth.setRequest(req);

      for (Parameter hh : op.getRequestHeaders().values()) {
        req.getParam().add(createRegularParameter(hh, ParamStyle.HEADER, log));
      }
      for (Parameter qp : op.getQueryParameters().values()) {
        req.getParam().add(createRegularParameter(qp, ParamStyle.QUERY, log));
      }
      if (op.getRequestRepresentation() != null) {
        req.getRepresentation().add(createRepresentation(op.getRequestRepresentation(), log));
      }
    }
    
    if (!op.getRequestHeaders().isEmpty() || !op.getResponseCodes().isEmpty() ||
        (op.getResponseRepresentation() != null)) {
      net.java.dev.wadl._2009._02.Response resp = factory.createResponse();
      meth.getResponse().add(resp);
      
      for (Parameter hh : op.getResponseHeaders().values()) {
        resp.getParam().add(createRegularParameter(hh, ParamStyle.HEADER, log));
      }
      for (ResponseCode rc : op.getResponseCodes().values()) {
        resp.getStatus().add(Long.valueOf(rc.getCode()));
      }
      if (op.getResponseRepresentation() != null) {
        resp.getRepresentation().add(createRepresentation(op.getResponseRepresentation(), log));
      }
    }
    
    net.java.dev.wadl._2009._02.Doc docs = attachDocs(op);
    if (docs != null) {
      meth.getDoc().add(docs);
    }

    return meth;
  }
  
  private net.java.dev.wadl._2009._02.Representation createRepresentation(Representation rep, Logger log)
  {
    net.java.dev.wadl._2009._02.Representation ret = factory.createRepresentation();
    ret.setHref(rep.getLink());
    ret.setMediaType(rep.getMediaType());
    ret.setPayload(rep.getDefinition());
    return ret;
  }
  
  private boolean isEmpty(String s)
  {
    return (s == null) || s.isEmpty();
  }
  
  private net.java.dev.wadl._2009._02.Doc attachDocs(Documented d)
  {
    if (d == null ||
        (isEmpty(d.getDocumentation()) && isEmpty(d.getDocumentationURI()))) {
      return null;
    }
    net.java.dev.wadl._2009._02.Doc docs = factory.createDoc();
    if (!isEmpty(d.getDocumentation())) {
      docs.getContent().add(d.getDocumentation());
    }
    if (!isEmpty(d.getDocumentationURI())) {
      docs.setUrl(d.getDocumentationURI());
    }
    return docs;
  }
}
