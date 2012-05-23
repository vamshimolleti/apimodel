import logging
import webapp2

import apimodel.model

class APIHandler(webapp2.RequestHandler):
  def __init__(self, request, response):
    self.logger = logging.getLogger('APIModel')
    self.initialize(request, response)
    
  def invokeMethod(self, verb, ref):
    self.response.body = 'Invoked handler for %s %s' % (verb, self.request.path)
     
  def handle(self, verb):
    model = apimodel.model.getModel()
    ref = model.matchPath(self.request.path)
    self.logger.debug('%s %s == %s', verb, self.request.path, ref)
    if ref == None:
      self.error(404)
    else:
      if verb in ref.verbs:
        self.invokeMethod(verb, ref)
      else:
        self.error(405)
  
  def get(self):
    return self.handle('GET')

  def put(self):
    return self.handle('PUT')

  def post(self):
    return self.handle('POST')

  def delete(self):
    return self.handle('DELETE')

  def patch(self):
    return self.handle('PATCH')
  
  