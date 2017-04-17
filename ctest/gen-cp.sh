#!/bin/bash -x

[ "$(ls -at project.clj .dir-locals.el | head -n 1 | grep -c project.clj)" -eq 1 ] && \
    echo "((nil . (eval . (setq inf-clojure-lein-cmd (concat \"lumo -c \" \"$(lein classpath)\")))))" \
         > .dir-locals.el
