import logging

APILogger = 'APIModel'

def initLogging():
  logger = logging.getLogger(APILogger)
  logger.setLevel(logging.DEBUG)
  logger.addHandler(logging.StreamHandler())
  logger.info('Initialized logging for %s', APILogger)