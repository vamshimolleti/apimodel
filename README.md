# API Model

This project consists of a few components for API modelling.

First, it consists of Java code that defines a generic API model. The API model is an 
object model that contains JPA annotations so that it may be persisted to a database,
and Jackson annotations so that it may be serialized to JSON (and soon to XML).

Second, it consists of import and export utilities to read and write the API model
in a number of formats:
  
* WADL (using the Apigee extensions)
* Swagger (from Wordnik)
* IODocs (from Mashery)
* apitext

(The last format is an experimental format that we're working on that allows API models
to be written in Markdown format. The "code" portions of the Markdown document are parsed
and turned into a document that formally describes the API.)

Third, this project defines an API that makes it possible to import and export the
different API definitions, and to query them. The API is, of course, documented in
"apitext" format, and the documentation may be found here: 
<https://github.com/apigee/apimodel/blob/master/java/api/src/main/resources/apiapi.md>

Of course, you can also use the API itself to retrieve the definition of the API in 
various formats. For instance, to see it in WADL format, query:
<http://apihub.apigeng.com/o/apigee/apis/apihub?format=wadl>

## Structure of the project

The top-level components of the project are in the following directories:

* apigee: This contains an API proxy that proxies the API using Apigee Enterprise. (Is
that "meta" enough for you?)
* java: This contains the bulk of the project.
* python: Nothing to see here yet, please move along.

## Java modules

The Java part of the project is built using Maven. It contains the following modules:

* api: The code to implement the API, and the definition of the API in "apitext"
format. It is built using the API framework defined by the "servlet" module.
* apitext: Import and export code for the "apitext" format.
* common: Test utilities.
* iodocs: Import and export for the "IODocs" format.
* jpa: Utilities for managing and testing the JPA API model.
* model: The object model for the API itself.
* processor: A library that uses an API model to classify HTTP calls. It is used by the "servlet" module and could be used in other projects.
* servlet: A framework that runs inside a servlet container. It reads an API model, uses the "processor" module to classify incoming calls, and dispatches them to either servlets or simpler objects that read and write Java objects. It takes advantage of JPA and Jackson.
* swagger: Import and export to Swagger.
* tools: A simple convertor tool.
* wadl: Import and export to WADL.
