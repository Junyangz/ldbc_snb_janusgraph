#!/bin/bash

java -cp target/janusgraphSNBInteractive-0.1-SNAPSHOT-jar-with-dependencies.jar com.ldbc.snb.janusgraph.importer.Main -n 6 -s 2000 -d test-data-100m/social_network -c bdb.conf
