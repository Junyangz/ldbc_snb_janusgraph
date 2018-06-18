package com.ldbc.snb.janusgraph.drivers.interactive;

import com.ldbc.driver.DbException;
import com.ldbc.driver.OperationHandler;
import com.ldbc.driver.ResultReporter;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery2;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery2Result;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;
import org.apache.tinkerpop.gremlin.process.traversal.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.directory.SchemaViolationException;
import java.util.*;

import static com.ldbc.snb.janusgraph.drivers.interactive.QueryUtils.CODE_OK;
/**
 * Created by Tomer Sagi on 06-Oct-14.
 * Strategy: Double expansion root->friend->post
 *      Given a start Person, find (most recent) Posts and Comments from all of that Person’s friends,
 *      that were created before (and including) a given date. Return the top 20 Posts/Comments, and the Person
 *      that created each of them. Sort results descending by creation date, and then ascending by Post identifier.
 */
public class LdbcQuery2Handler implements OperationHandler<LdbcQuery2,JanusGraphDb.RemoteDBConnectionState> {
    final static Logger logger = LoggerFactory.getLogger(LdbcQuery2Handler.class);

    @Override
    public void executeOperation(final LdbcQuery2 operation, JanusGraphDb.RemoteDBConnectionState dbConnectionState, ResultReporter
            resultReporter) throws DbException {
        // long person_id = operation.personId();
        // final long maxDate = operation.maxDate().getTime();//QueryUtils.addDays(operation.maxDate(),1);
        // logger.debug("Query 2 called on Person id: {} with max date {}", person_id, maxDate);
        // int limit = operation.limit();

        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("$id",operation.personId());
        parameters.put("$maxDate",operation.maxDate().getTime());
        parameters.put("$limit",operation.limit());
        // JanusGraphDb.BasicClient client = dbConnectionState.client();
        
        String query = "g.V().has('Person.id',$id)."+
                            "out('knows').as('friend').valueMap().as('x').in('hasCreator').has('creationDate', P.lte($maxDate))."+
                            "order().by('creationDate',decr).by('messageId',incr)."+
                            "limit($limit).as('post').valueMap().as('y')"+
                            ".select('x','y')\n";

        try {
            ResultSet resultSet = dbConnectionState.runQuery(query, parameters);
            ArrayList<LdbcQuery2Result> results = new ArrayList<LdbcQuery2Result>();
            for (Result r : resultSet) {
                Map<String,List<Object>>  map = (Map<String,List<Object>>)r.getObject();
                Map<String,List<Object>> postMap = (Map<String,List<Object>>)map.get("x");
                Map<String,List<Object>> friendMap = (Map<String,List<Object>>)map.get("y");
                Long messageId = 0L;
                if(postMap.get("Comment.id") != null) {
                    messageId = (Long)postMap.get("Comment.id").get(0);
                } else {
                    messageId = (Long)postMap.get("Post.id").get(0);
                }
                LdbcQuery2Result ldbcQuery2Result = new LdbcQuery2Result((Long)friendMap.get("Person.id").get(0),
                                                                        (String)friendMap.get("firstName").get(0),
                                                                        (String)friendMap.get("lastName").get(0),
                                                                        messageId,
                                                                        (String)postMap.get("content").get(0),
                                                                        (Long)postMap.get("creationDate").get(0));
                results.add(ldbcQuery2Result);
            }
            resultReporter.report(CODE_OK, results, operation);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        /*
        GremlinPipeline<Vertex, Vertex> gp = new GremlinPipeline<>(root);
        Iterable<Row> qResult = gp.out("knows").as("friend").in("hasCreator")
                .has("creationDate", Compare.LESS_THAN, maxDate)
                .as("post").select()
                .order(QueryUtils.COMP_CDate_Postid).range(0, limit - 1);

        List<LdbcQuery2Result> result = new ArrayList<>();
        for (Row r : qResult) {
            Vertex post = (Vertex) r.getColumn("post");
            Long pid = client.getVLocalId((Long) post.getId());
            Vertex friend = (Vertex) r.getColumn("friend");
            String content = post.getProperty("content");
            if (content.length() == 0)
                content = post.getProperty("imageFile");
            LdbcQuery2Result res = new LdbcQuery2Result(client.getVLocalId((Long) friend.getId()),
                    (String) friend.getProperty("firstName"), (String) friend.getProperty("lastName"),
                    pid, content, (Long) post.getProperty("creationDate"));

            result.add(res);
        }
        resultReporter.report(result.size(), result, operation);
        */
    }
}