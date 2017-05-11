package analytics;

import ai.grakn.Grakn;
import ai.grakn.GraknGraph;
import ai.grakn.GraknSession;
import ai.grakn.GraknTxType;
import ai.grakn.concept.ConceptId;
import ai.grakn.graql.Graql;
import ai.grakn.graql.MatchQuery;
import ai.grakn.graql.Var;
import ai.grakn.graql.admin.Answer;
import ai.grakn.graql.analytics.DegreeQuery;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ai.grakn.graql.Graql.var;

/**
 *
 */
public class Main {

    private static final String keyspace = "biomed";

    public static void main(String[] args) {
        // initialise the connection to engine
        String engineHostname = Grakn.DEFAULT_URI;
        if (args.length>0) {engineHostname = args[0];}
        try (GraknSession session = Grakn.session(engineHostname, keyspace)) {
            testConnection(session);
            System.out.println("Calculating degrees...");
            Map<Long, Set<String>> degrees = degreeOfRelation(session, "interaction", Sets.newHashSet("interaction","reference"));
            System.out.println("Persisting degrees...");
            persistDegrees(session, degrees);
            System.out.println("Finished persisting.");
            System.out.println("Calculating degrees...");
            degrees = degreeOfRelation(session, "gene-target", Sets.newHashSet("gene-target","reference"));
            System.out.println("Persisting degrees...");
            persistDegrees(session, degrees);
            System.out.println("Finished persisting.");
            System.out.println("Task complete.");
        }
    }

    private static void persistDegrees(GraknSession session, Map<Long, Set<String>> degrees) {

        try (GraknGraph graph = session.open(GraknTxType.WRITE)) {

            // add the degrees to the cluster
            Set<Var> degreeMutation = new HashSet<>();
            degrees.forEach((degree, concepts) -> {
                concepts.forEach(concept -> {
                    degreeMutation.add(Graql.var().id(ConceptId.of(concept)).has("degree",degree));
                });
            });

            // execute the query
            graph.graql().insert(degreeMutation).execute();

            // don't forget to commit
            graph.commit();
        }
    }

    private static Map<Long, Set<String>> degreeOfRelation(GraknSession session, String relationType, Set<String> subGraph) {

        // open a graph (database transaction)
        try (GraknGraph graph = session.open(GraknTxType.READ)) {

            // construct the analytics cluster query
            DegreeQuery query = graph.graql().compute().degree().in(subGraph.toArray(new String[subGraph.size()])).of(relationType);

            // execute the analytics query
            Map<Long, Set<String>> degrees = query.execute();

            return degrees;
        }
    }

    private static void testConnection(GraknSession session) {

        // open a graph (database transaction)
        try (GraknGraph graph = session.open(GraknTxType.READ)) {

            // construct a match query to find people
            MatchQuery query = graph.graql().match(var("x").isa("mirna"));

            // execute the query
            List<Answer> result = query.limit(10).execute();

            // if there is no data throw error
            if (result.isEmpty()) {throw new RuntimeException("Expected data is not present in the graph.");}
            System.out.println("Connection OK.");
        }
    }
}
