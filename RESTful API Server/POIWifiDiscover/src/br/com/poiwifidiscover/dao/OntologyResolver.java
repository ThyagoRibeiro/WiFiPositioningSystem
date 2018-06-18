package br.com.poiwifidiscover.dao;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.omwg.logicalexpression.LogicalExpression;
import org.omwg.logicalexpression.terms.Term;
import org.omwg.ontology.Ontology;
import org.omwg.ontology.Variable;
import org.wsml.reasoner.api.WSMLReasoner;
import org.wsml.reasoner.api.WSMLReasonerFactory;
import org.wsml.reasoner.impl.DefaultWSMLReasonerFactory;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsmo.common.IRI;
import org.wsmo.common.TopEntity;
import org.wsmo.factory.Factory;
import org.wsmo.factory.LogicalExpressionFactory;
import org.wsmo.wsml.Parser;

import br.com.poiwifidiscover.util.Constants;

public final class OntologyResolver {

	private LogicalExpressionFactory leFactory;
	private Parser wsmlparser;
	private Ontology ontology;
	private WSMO4JManager wsmoManager;
	private WSMLReasoner reasoner;

	public OntologyResolver() {

		try {
			setUpFactories();
			ontology = parseAndLoadOntology(Constants.WIFIDATA_FILE);
			reasoner = getReasoner();
			reasoner.registerOntology(ontology);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private WSMLReasoner getReasoner() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(WSMLReasonerFactory.PARAM_BUILT_IN_REASONER, WSMLReasonerFactory.BuiltInReasoner.IRIS);
		WSMLReasoner reasoner = DefaultWSMLReasonerFactory.getFactory().createWSMLFlightReasoner(params);
		return reasoner;
	}

	private Ontology parseAndLoadOntology(String ontologyFile) {

		try {
			final TopEntity[] identifiable = wsmlparser.parse(new FileReader(ontologyFile));

			return (Ontology) identifiable[0];

		} catch (Exception e) {
			System.out.println("Unable to parse ontology: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Executes a query.
	 * 
	 * @param reasoner
	 *            Reasoner which will execute the query
	 * @param o
	 *            Ontology over which the query will be answered
	 * @param queryString
	 *            WSML query represented as a string
	 * @return Result after the query evaluation
	 * @throws Exception
	 */
	private Set<Map<Variable, Term>> performQuery(WSMLReasoner reasoner, Ontology o, String queryString)
			throws Exception {
		LogicalExpression query = this.leFactory.createLogicalExpression(queryString, o);
		// Executes query request
		Set<Map<Variable, Term>> result = reasoner.executeQuery((IRI) o.getIdentifier(), query);
		return result;
	}

	public Set<Map<Variable, Term>> runProgram(String query) {

		Set<Map<Variable, Term>> result = null;
		try {
			result = performQuery(reasoner, ontology, query);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private void setUpFactories() {
		wsmoManager = new WSMO4JManager();
		leFactory = wsmoManager.getLogicalExpressionFactory();
		wsmlparser = Factory.createParser(null);
	}
}
