# LDBC SNB on Janusgraph

Janusgraph implementation

> ​	UCAS bigdata project

## Run Benchmark on JanusGraph



### Run Janusgraph

1. download Janusgraph with this link https://github.com/JanusGraph/janusgraph/releases/.
2. decompress [**janusgraph-0.2.0-hadoop2-doc.zip**](https://github.com/JanusGraph/janusgraph/releases/download/v0.2.0/janusgraph-0.2.0-hadoop2-doc.zip) 
3. run Janusgraph with backend storage configured, refer https://docs.janusgraph.org/latest/configuration.html

```code
bin/janusgraph start|status|stop|clean
# bin/gremlin-server.sh  #need configure backend storage
```

### Clone and Build 

```bash
git clone git@github.com:Junyangz/ldbc_snb_janusgraph.git
cd ldbc_snb_janusgraph/janusgraph
mvn clean assembly:assembly
```

### Run importer

```code
tar zxvf test-data-100m.tar.gz #for test data
#or use datagen generate and copy to ./test-data-1G/

#modify bdb.conf for Janusgraph backend storage.

#storage.backend=cassandrathrift | hbase |berkeleyje |hbase
#storage.hostname=localhost 
#storage.directory=../db/berkele  

./import-to-janusgraph.sh #for Cassandra backend 
./import-to-janusgraph-berkeley.sh # for BerkeleyDB backend
./import-to-janusgraph-Hbase.sh # for Hbase backend
```



### Configure and Run **interactive-benchmark** 

```code
vim ldbc_snb_interactive.properties
./interactive-benchmark.sh
```



### Reference

* [ldbc_snb_implementations/importer for janusgraph](https://github.com/ldbc/ldbc_snb_implementations/tree/master/interactive/janusgraph) 
* https://github.com/ldbc
* https://github.com/JanusGraph/ 



Many thanks to my teammate!