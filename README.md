# cljnio

A wrapper around NIO2.

## Usage

NIO2 provides three useful IO Classes.

### Server

Write a handler to run when a client connects:

	(require 'cljnio.server)
	(cljnio.server/with-server {:port 4004} client
	  (println "Got a client" (second client)))

### Client

TBD

### Files

TBD

## core.async

I didn't want to force the dep so it's optional.
Make sure it's on the classpath.
Check the cljnio.async ns docstring.

## TODO

Maybe add nodejs support for cljs.
Having an unified API'd be useful.

## License

Copyright © 2014 Ricardo Gomez

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
