import groovy.json.JsonSlurper

def locationDB = '/Users/miv/Desktop/neo4j/neo4j-community-1.8.3/data/graph.db'
def outputFile = 'out_tag_count.text'
new File(outputFile).delete()

// remove any old data
def neo4jDirectory = new File(locationDB)

// set Neo4j location
def g = new Neo4jGraph(locationDB)

new File(outputFile) << "Using Graph: ${g}\n"
new File(outputFile) << "Tags: ${g.V[2].inE('IS_A').count()}\n"
new File(outputFile) << "\n"
new File(outputFile) << "Tag_UUID\t#Concept\tTag_Name(s)\n"

def tagConceptCount = [:]
g.V[2].inE('IS_A').outV.each { tag -> 

	def tagLabels = []
	tag.outE('HAS_LABEL').each { labelE -> 
		if (labelE.type == 'PREFERRED'){
			labelE.inV.each { 
				if (it.lang == 'en'){
					tagLabels << it.text
				}
			}
		}
	}
	
	def line = "${tag.uuid}\t${tag.inE('HAS_TAG').outV.collect { concept -> concept.uuid }.unique().size()}\t${tagLabels.unique().join('; ')}\n"
	//println line
	new File(outputFile) << line

//	tagConceptCount["${tag.uuid}"] = [	
//		'concepts': tag.inE('HAS_TAG').outV.collect { concept -> concept.uuid }.unique().size() , 
//		'label': tagLabels.unique().join(', ')
//	]

}

//tagConceptCount.each { uuid, count ->
//
//	def jsonResponse = new URL('http://conceptwiki.nbiceng.net/web-ws/concept/get?uuid='+ uuid).text
//	def concept = new JsonSlurper().parseText(jsonResponse)
//
//	def labels = []
//	concept.labels.each { label ->
//		if (label.type == 'PREFERRED' && label.language.code == 'en'){
//			labels << "${label.text}".trim()
//		}
//	}
//
//	def line = "${uuid}\t${count}\t${labels.join(', ')}\n"
//	new File(outputFile) << line
//}

//g.V[0..5].each { vertex ->
//
//	println vertex.map()
//	
//	vertex.inE('HAS_TAG').each { hasTag ->
//		println hasTag.map()
//	}
//	
//}

// find a tag: 05d0f699-5fc5-4cbb-8d9a-4d4a8ec5f0a1
//g.V('uuid','d214dd45-c736-4074-a008-2860ea3b79e5').each { pathwayOntology -> 
//	println " - ${pathwayOntology.map()}"
//	pathwayOntology.inE()[0..1].each {
//		println it.map()
//	}
//}
//println g.V[[uuid:'05d0f699-5fc5-4cbb-8d9a-4d4a8ec5f0a1']]

// tags
//g.V[2].each { node ->
//	println node.map()
//	//node.inE()[0..10].eachWithIndex { edge, idx -> println idx + " - " + edge.map() }
//	node.in()[0..100].eachWithIndex { v1st, idx -> 
//		println idx + " - " + v1st.map() 
//		v1st.in().each { v2nd -> 
//			v2nd.in().each { v3rd ->
//				println "\t -- " + v3rd.map()
//				v3rd.inE().each { println "\t\t -- " it.map() }
//			} 
//		}
//	}
//}

//g.V[1..20].each { node ->
//	println node.map()
//	if (node.in()){
//		println "${node.in().size()} Nodes (in): "
//		node.in()[0..10].each { println " - " + it.map() }
//	}
//	
//	if (node.inE()){
//		println "${node.inE().size()} Edges (in): "
//		node.inE()[0..10].each { println " - " + it.map() }
//	}
//}

//g.createIndex("full-vertex-class", Vertex.class)
//println g.idx('full-vertex-class')[[text:'Metabolomics']]
//g.idx('full-vertex-class')[[text:'Metabolomics']].outE.each {
//	println it.map()
//}

//println g.getIndex('node_auto_index', Vertex.class).get('name','I')._().out.sort{it.name}

// list verts (persons)
//println "Vertex(es): ${g.V.collect { v -> "${v}" }.join(', ')}."

// list edges (relations)
//println "Edge(s): ${g.E.collect { e -> "${e}" }.join(', ')}."

// shutdown neo4j properly
g.shutdown()
