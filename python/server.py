import sys
import webapp2

import apimodel.apihandler
import apimodel.model

if len(sys.argv) != 2:
  print 'Usage: server <YAML file>'
  sys.exit(2)
  
apimodel.model.buildModel(sys.argv[1])

app = webapp2.WSGIApplication([(r'/.*', apimodel.apihandler.APIHandler)])

def main():
  from paste import httpserver
  httpserver.serve(app, host='0.0.0.0', port='9010')
  
if __name__ == '__main__':
  main()

