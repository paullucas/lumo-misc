#!/bin/bash -x

[ "$(ls -at | head -n 1 | grep -c project.clj)" -e 1 ] && \
    echo "((nil . (eval . (setq inf-clojure-lein-cmd (concat \"lumo -c \" $(lein classpath))))))" \
         > .dir-locals.el
