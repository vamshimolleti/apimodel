import sys

import apimodel.model

if len(sys.argv) < 2:
  print 'Usage: test <filename>'
  sys.exit(2)
  
apimodel.model.buildModel(sys.argv[1])
model = apimodel.model.getModel()
print model





