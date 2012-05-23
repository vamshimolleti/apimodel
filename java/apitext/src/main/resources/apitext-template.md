<#macro printDoc doc>
<#if doc.documentation??>

${doc.documentation?trim}

</#if>
<#if doc.documentationURI??>[${doc.documentationURI}](${doc.documentationURI})

</#if>
</#macro>
<#macro printParam p type>
<@printDoc doc=p/>
        ${type} ${p.name} (<#if p.dataType??>${p.dataType}</#if><#if p.required>, required</#if><#if !p.required>, optional</#if><#if p.repeating>, repeating</#if><#if p.default??>, default="${p.default}"</#if>)
  <#list p.validValues as vv>
  <#if vv.value??>
<@printDoc doc=vv/>
          valid ${vv.value}<#if vv.mediaType??> (${vv.mediaType})</#if>
  </#if>
  </#list>
</#macro>
<#list models as model>
<@printDoc doc=model/>
* ${model.name}

<#if model.onboardingURI??>[${model.onboardingURI}](${model.onboardingURI})

</#if>
    Name = ${model.name}
    BasePath = ${model.basePath}

<#list model.resources as resource>
<@printDoc doc=resource/>
    resource ${resource.path}
    <#if resource.hasRequestParameters>
      request
    <#if resource.exampleURI??>
        exampleURI ${resource.exampleURI}
    </#if>
    <#list resource.templateParameters as uriParam>
        <@printParam p=uriParam type="templateParam"/>
    </#list>
    <#list resource.queryParamValues as qp>
        <@printParam p=qp type="queryParam"/>
    </#list>
    <#list resource.requestHeaderValues as hdr>
        <@printParam p=hdr type="requestHeader"/>
    </#list>
    <#list resource.matrixParameters as mat>
        <@printParam p=mat type="matrixParam"/>
    </#list>
    <#if resource.requestRepresentation??>
        requires ${resource.requestRepresentation.mediaType} (element=${resource.requestRepresentation.element})
    </#if>
    </#if>
    <#if resource.hasResponseParameters>
    <#list resource.responseHeaderValues as hdr>
        <@printParam p=hdr type="requestHeader"/>
    </#list>
    <#if resource.responseRepresentation??>
      returns ${resource.responseRepresentation.mediaType} (element=${resource.responseRepresentation.element})
    </#if>
    <#list resource.responseCodes as code>
      responseCode ${code.code} (success=${code.success})
    </#list>
    </#if>
    
    <#list resource.operations as operation>
<@printDoc doc=operation/>
      operation ${operation.method}
      <#if operation.hasRequestParameters>
        request
      <#if resource.exampleURI??>
          exampleURI ${operation.exampleURI}
      </#if>
      <#list operation.queryParamValues as qp>
          <@printParam p=qp type="queryParam"/>
      </#list>
      <#list operation.requestHeaderValues as hdr>
          <@printParam p=hdr type="requestHeader"/>
      </#list>
      <#if operation.requestRepresentation??>
          requires ${operation.requestRepresentation.mediaType} (element=${operation.requestRepresentation.element})
      </#if>
      </#if>
      <#if operation.hasResponseParameters>
        response
      <#list operation.responseHeaderValues as hdr>
          <@printParam p=hdr type="responseHeader"/>
      </#list>
      <#list operation.responseCodeValues as code>
          responseCode ${code.code} (success=${code.success})
      </#list>
      <#if operation.responseRepresentation??>
          returns ${operation.responseRepresentation.mediaType} (element=${operation.responseRepresentation.element})
      </#if>
      </#if>
    </#list>
    
  </#list>
</#list>
