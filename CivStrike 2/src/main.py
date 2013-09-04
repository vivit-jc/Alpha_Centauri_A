#! /usr/bin/python
# -*- coding: utf-8 -*-

from google.appengine.ext.webapp import template

from google.appengine.ext import webapp
from google.appengine.ext.webapp.util import run_wsgi_app
import os

from google.appengine.api import datastore
from google.appengine.api.datastore import Entity
from google.appengine.api.datastore_types import Key

class MainPage(webapp.RequestHandler):
    
    def get(self):
        player_entity = Entity('mykind', name = 'e_name')
        player_entity['var'] = 1192
        datastore.Put(player_entity)
        path = os.path.join(os.path.dirname(__file__), 'view/index.html')
        self.response.out.write(template.render(path, {}))


def main():
    application = webapp.WSGIApplication([
                                          ('/', MainPage)],debug=True)
    run_wsgi_app(application)

if __name__ == "__main__":
    main()