package com.ldbc.snb.janusgraph.drivers.interactive;

import com.ldbc.driver.DbException;
import com.ldbc.driver.OperationHandler;
import com.ldbc.driver.ResultReporter;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery1;
import com.ldbc.driver.workloads.ldbc.snb.interactive.LdbcQuery1Result;
import javafx.util.Pair;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraphTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.directory.SchemaViolationException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of LDBC Interactive workload query 1
 * @author Tomer Sagi
 * @modified Arnau Prat
 */
public class LdbcQuery1Handler implements OperationHandler<LdbcQuery1,JanusGraphDb.BasicDbConnectionState> {
    final static Logger logger = LoggerFactory.getLogger(LdbcQuery1Handler.class);

    @Override
    public void executeOperation(final LdbcQuery1 operation, JanusGraphDb.BasicDbConnectionState dbConnectionState, ResultReporter resultReporter) throws DbException {
        /*List<LdbcQuery1Result> result = new ArrayList<>();
        long person_id = operation.personId();
        final String friend_first_name = operation.firstName();
        final int limit = operation.limit();
        JanusGraphDb.BasicClient client = dbConnectionState.client();
        JanusGraphTransaction transaction = client.graph.newTransaction();
        try {
            logger.debug("Query 1 called on person id: {}", person_id);


            final PipeFunction<Vertex, Boolean> HAS_NAME = new PipeFunction<Vertex, Boolean>() {
                public Boolean compute(Vertex v) {
                    return v.getProperty("firstName").equals(friend_first_name);
                }
            };

            final Set<Vertex> sv1 = new HashSet<>();
            final Set<Vertex> sv2 = new HashSet<>();
            final Set<Vertex> sv3 = new HashSet<>();

            final PipeFunction<Pair<Vertex, Vertex>, Integer> COMP_Q1 = new PipeFunction<Pair<Vertex, Vertex>, Integer>() {

                @Override
                public Integer compute(Pair<Vertex, Vertex> argument) {
                    Vertex v1 = argument.getA();
                    Vertex v2 = argument.getB();
                    int d1 = (sv1.contains(v1) ? 1 : (sv2.contains(v1) ? 2 : 3));
                    int d2 = (sv1.contains(v2) ? 1 : (sv2.contains(v2) ? 2 : 3));
                    if (d1 == d2) {
                        String L1 = v1.getProperty("lastName");
                        String L2 = v2.getProperty("lastName");
                        if (L1.compareToIgnoreCase(L2) == 0) {
                            Long id1 = (Long) v1.getId();
                            Long id2 = (Long) v2.getId();
                            return id1.compareTo(id2);
                        } else
                            return L1.compareToIgnoreCase(L2);
                    } else
                        return Integer.compare(d1, d2);
                }
            };

            for (Vertex v : (transaction.traversal().V().has("Person.id",person_id)).as("0")
                    .out("knows").store("step1").as("1")
                    .out("knows").except("0", "1").store(sv2).as("2")
                    .gather().scatter().out("knows").except("0", "1", "2").store(sv3).as("3")
                    .dedup().filter(HAS_NAME).order(COMP_Q1).range(0, limit - 1)) {

                Vertex cityV = QueryUtils.getPersonCity(v);
                String city = (cityV == null ? "" : (String) cityV.getProperty("name"));
                int d1 = (sv1.contains(v) ? 1 : (sv2.contains(v) ? 2 : 3));
                ArrayList<String> emails = new ArrayList<>();
                ArrayList emailRes = v.getProperty("email");
                if (emailRes != null)
                    for (Object emailRe : emailRes)
                        emails.add((String) emailRe);

                ArrayList<String> languages = new ArrayList<>();
                ArrayList<String> languageRes = v.getProperty("language");
                if (languageRes != null)
                    for (String languageRe : languageRes)
                        languages.add(languageRe);

                @SuppressWarnings("unchecked")


                LdbcQuery1Result res = new LdbcQuery1Result(client.getVLocalId((Long) v.getId()),
                        (String) v.getProperty("lastName"), d1,
                        (Long) v.getProperty("birthday"), (Long) v.getProperty("creationDate"),
                        (String) v.getProperty("gender"), (String) v.getProperty("browserUsed"),
                        (String) v.getProperty("locationIP"), emails,
                        languages, city, QueryUtils.collectUnis(v), QueryUtils.collectComps(v));

                result.add(res);
            }
            resultReporter.report(result.size(), result, operation);
        } catch (SchemaViolationException e) {
        e.printStackTrace();
        resultReporter.report(-1, null, operation);
    }

    */
    }


}