import logging
import re
import StringIO
import yaml

import apimodel

# Given an array of strings, assemble a URI path starting with a '/'
def assemblePath(ps):
  buf = StringIO.StringIO()
  for p in ps:
    buf.write('/')
    buf.write(p)
  return buf.getvalue()

# Given a path from the YAML, produce a list of variables that we will extract from each call
def pathToVars(p):
  v = []
  for e in p:
    if re.match('\$.*', e) != None:
      v.append(e[1:])
  return v

# Given a path from the YAML, construct a regular expression that we can use to match it
def pathToRegex(p):
  rep = list()
  for e in p:
    if e == None or e == '':
      continue
    if re.match('\$.*', e) != None:
      rep.append('[^/]+')
    else:
      rep.append(e)
  path = '%s\Z' % assemblePath(rep)
  ret = re.compile(path)
  logging.getLogger('APIModel').debug('%s = %s', p, ret.pattern)
  return ret

def trimPathEnd(path):
  p = path.split('/')
  return assemblePath(p[:len(p) - 1])

# This class is derived directly from the YAML and contains its data plus defaults
class Collection:
  def preInit(self):
    self.name = ''
    self.group = ''
    self.path = ''
    self.docs = ''
    self.readOnly = False
    self.singleton = False
    self.deleteAll = False
    
  def __init__(self, groupName, d):
    self.preInit()
    self.group = groupName
    if 'name' in d:
      self.name = d['name']
    if 'path' in d:
      self.path = d['path']
    if 'docs' in d:
      self.docs = d['docs']
    if 'readOnly' in d:
      self.readOnly = bool(d['readOnly'])
    if 'singleton' in d:
      self.singleton = bool(d['singleton'])
    if 'deleteAll' in d:
      self.deleteAll = bool(d['deleteAll'])
      
  def validate(self):
    if self.name == '':
      return '"name" must be set'
    if self.group == '':
      return '"group" must be set'
    if self.path == '':
      return '"path" must be set'
    return None

class GenericData:
  def superInit(self, path, readOnly):
    self.path = path
    self.readOnly = readOnly
    sp = path.split('/')
    self.params = pathToVars(sp)
    self.regEx = pathToRegex(sp)
    self.pathLen = len(sp)
    
  def __str__(self):
    buf = StringIO.StringIO()
    buf.write('path = %s\n' % self.path)
    buf.write('  verbs = %s\n' % self.verbs)
    buf.write('  params = %s\n' % self.params)
    buf.write('  regex = %s\n' % self.regEx.pattern)
    return buf.getvalue()

# This class is part of the model that represents a collection
class CollectionData(GenericData):
  def __init__(self, path, readOnly, deleteAll):
    self.superInit(path, readOnly)
    self.verbs = { 'GET' }
    if not readOnly:
      self.verbs.add('POST')
      if deleteAll:
        self.verbs.add('DELETE')
    
# This class is part of the model that represents a resource
class ResourceData(GenericData):
  def __init__(self, path, readOnly):
    self.superInit(path, readOnly)
    self.verbs = { 'GET' }
    if not readOnly:
      self.verbs.add('PUT')
      self.verbs.add('DELETE')
      
def getPath(o):
  return o.path

def getPathLen(o):
  return o.pathLen

# This is the model of the whole API.
class APIModel:
  def __init__(self):
    self.collections = list()
    self.collectionsByName = dict()
    self.uriSearchList = list()
    self.uriList = list()
    self.logger = logging.getLogger('APIModel')
    
  def build(self):
    for c in self.collections:
      if not c.singleton:
        o = CollectionData(trimPathEnd(c.path), c.readOnly, c.deleteAll)
        self.uriList.append(o)
      r = ResourceData(c.path, c.readOnly)
      self.uriList.append(r)
    self.uriList = sorted(self.uriList, key=getPath)
    # TODO sort this with longer URIs first
    self.uriSearchList = self.uriList
  
  def validate(self):
    for c in self.collections:
      v = c.validate()
      if v != None:
        return v
    return None
  
  # Given a path, return the first "GenericData" object that matches.
  # This works using a linear list of regular expressions. There are likely
  # more efficient ways to do this.
  def matchPath(self, path):
    for u in self.uriSearchList:
      # self.logger.debug('%s : %s', path, u.regEx.pattern)
      if u.regEx.match(path) != None:
        return u
    return None
  
  def __str__(self):
    buf = StringIO.StringIO()
    for u in self.uriList:
      buf.write(str(u))
    return buf.getvalue()
    
def buildModel(fileName):
  f = open(fileName, 'r')
  try:
    yam = yaml.safe_load(f)
  finally:
    f.close()
  
  mod = APIModel()
  
  for n in yam.items():
    groupName = n[0]
    for c in n[1]:
      collection = Collection(groupName, c)
      mod.collections.append(collection)
      if collection.name in mod.collectionsByName:
        print 'Error: Duplicate collection named ' , collection.name
      else:
        mod.collectionsByName[collection.name] = collection
      
  val = mod.validate()
  if val != None:
    print val  
  mod.build()
  
  apimodel.defaultModel = mod

  
def getModel():
  return apimodel.defaultModel


      
    
    
    