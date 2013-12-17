def neo4jLocation = '/Users/miv/Desktop/temp/2013-04-13'
def neo4jDirectory = new File(neo4jLocation)
def g = new Neo4jGraph(neo4jLocation)

//g.createIndex("full-vertex-class", Vertex.class)
println g.idx('full-vertex-class')[[text:'Metabolomics']]
g.idx('full-vertex-class')[[text:'Metabolomics']].outE.each {
	println it.map()
}

//g.V.has("text","PW:0000001").each {
//	println it.map()
//}


// types of V's
// HAS_NOTATION
// HAS_TAG
// HAS_URL
// HAS_SOURCE
// HAS_LABEL
// HAS_NOTE

if (1 == 2){

	def pathways = []

	def startIdx = 18000000
	def vs = 1000000
	//(startIdx..(startIdx+vs)).each { id ->
	(22842890..22848688).each { id ->

		//g.v(id)?.outE('HAS_LABEL').each { e ->
		//g.v(id)?.outE('HAS_NOTATION').each { e ->
		g.v(id)?.outE.each { e ->
			//println e.map()
			e.inV.unique().each { v ->
				if (v.text && "${v.text}".contains('PW:')){
					pathways << "${v.text}".trim()
					println ">> ID: " + id
					println e.map()
					println v.map()
				}
			}
		}
	}
	
	//println pathways.unique()	
}



// shutdown neo4j properly
g.shutdown()
