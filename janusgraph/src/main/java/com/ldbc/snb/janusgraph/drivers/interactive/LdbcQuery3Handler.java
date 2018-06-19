package com.ldbc.snb.janusgraph.drivers.interactive;

import com.ldbc.driver.DbException;
import com.ldbc.driver.OperationHandler;
import com.ldbc.driver.ResultReporter;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery3;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery3Result;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.ldbc.snb.janusgraph.drivers.interactive.QueryUtils.CODE_OK;
/**
 * Created by Tomer Sagi on 06-Oct-14.
 * Handles Q3 by running a groupBy pipe and then sorting using
 * a sortable map.
 */
public class LdbcQuery3Handler implements OperationHandler<LdbcQuery3,JanusGraphDb.RemoteDBConnectionState> {
    final static Logger logger = LoggerFactory.getLogger(LdbcQuery3Handler.class);

    @Override
    public void executeOperation(final LdbcQuery3 operation, JanusGraphDb.RemoteDBConnectionState dbConnectionState, ResultReporter
            resultReporter) throws DbException {

        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("$person_id", operation.personId());
        parameters.put("$country_x", operation.countryXName());
        parameters.put("$country_y", operation.countryYName());
        parameters.put("$min_date", operation.startDate().getTime());
        int days = operation.durationDays();
        long max_date = operation.startDate().getTime() + days * 24 * 60 * 60 * 1000;
        parameters.put("$max_date", max_date);
        parameters.put("$limit", operation.limit());

        String query = "g.V().has('Person.id', $person_id)" +
                ".repeat(__.out('knows')).emit().times(2)" +
                ".has('Person.id', P.neq($person_id))" +
                ".dedup()" +
                ".where(" +
                "__.out('isLocatedIn')" +
                ".out('isPartOf')" +
                ".and(" +
                "__.has('name', P.neq($country_x))," +
                "__.has('name', P.neq($country_y))" +
                ")" +
                ")" +
                ".as('person', 'xCount', 'yCount')" +
                ".select('person', 'xCount', 'yCount')" +
                ".by()" +
                ".by(" +
                "__.in('hasCreator')" +
                ".where(" +
                "__.and(" +
                "__.has(('creationDate'), P.gte($min_date)), " +
                "__.has(('creationDate'), P.lt($max_date))" +
                ")" +
                ")" +
                ".out('isLocatedIn')" +
                ".has('name', $country_x).count())" +
                ".by(" +
                "__.in('hasCreator')" +
                ".where(" +
                "__.and(" +
                "__.has(('creationDate'), P.gte($min_date)), " +
                "__.has(('creationDate'), P.lt($max_date))" +
                ")" +
                ")" +
                ".out('isLocatedIn')" +
                ".has('name', $country_y).count())" +
                ".limit($limit)" +
                ".order().by('xCount',Order.decr).by('Person.id', Order.incr)";

        try {
            ResultSet resultSet = dbConnectionState.runQuery(query, parameters);
            ArrayList<LdbcQuery3Result> results = new ArrayList<LdbcQuery3Result>();
            for (Result r : resultSet) {
                Map<String,List<Object>> map = (Map<String,List<Object>>)r.getObject();
                Map<String,List<Object>> person = (Map<String,List<Object>>)map.get("person");
//                long  xCount = (Long)((Map<String,List<Object>>)map.get("xCount").get(0);
//                long  yCount = (Long)(Map<String,List<Object>>)map.get("yCount").get(0);
                Map<String,List<Object>> xCountMap = (Map<String,List<Object>>)map.get("xCount");
                Map<String,List<Object>> yCountMap = (Map<String,List<Object>>)map.get("yCount");
                long xCount = (Long) xCountMap.get("xCount").get(0);
                long yCount = (Long) yCountMap.get("yCount").get(0);
                LdbcQuery3Result ldbcResult = new LdbcQuery3Result(
                        (Long)person.get("Person.id").get(0),
                        (String)person.get("Person.firstName").get(0),
                        (String)person.get("Person.lastName").get(0),
                        xCount,
                        yCount,
                        xCount+yCount);
                results.add(ldbcResult);
            }
            resultReporter.report(CODE_OK, results, operation);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


    }
}
