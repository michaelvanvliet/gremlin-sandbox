/**
 * Use the Graph database to store Friends
 *
 * Install: https://github.com/tinkerpop/gremlin/wiki/Getting-Started
 * Execute: gremlin -e example1.groovy
 *
 * output
 * - List all people
 * - List all relations
 * - List all people with a list of friends how long they are friends
 *
 * Copyright 2012 Michael van Vliet, Netherlands Bioinformatics Centre (NBIC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

// config
def persons = 15 as int
def relations = 2 as int
def year = Calendar.getInstance().get(Calendar.YEAR)
def neo4jLocation = './db/neo4j_example1'

// remove any old data
def neo4jDirectory = new File(neo4jLocation)
if (neo4jDirectory.isDirectory()){
    neo4jDirectory.eachFileRecurse { it.delete() }
}

// set Neo4j location
def g = new Neo4jGraph(neo4jLocation)

// always handy when creating dummy data
def random = new Random()

persons.times {
    g.addVertex([no: it, name: "person_${it}" as String])
}

// add relations between persons
g.V.each { p1 ->

    if (p1){

        relations.times {

            //find a random person 2
            def p2 = null
            // make sure it has a name and is not the same as p1
            while (!p2?.name || p2?.no == p1.no){
                p2 = g.v(random.nextInt(persons+1))
            }

            //make them friends
            g.addEdge(p1,p2,'friendsWith',[since: year - random.nextInt(50)])
        }
    }
}

// list verts (persons)
println "Vertex(es): ${g.V.collect { v -> "${v}" }.join(', ')}."

// list edges (relations)
println "Edge(s): ${g.E.collect { e -> "${e}" }.join(', ')}."

// list all friends of persons
g.V.each { person ->

    // list friends
    if (person.name != null){
        println "${person.name} knows:"
        person.outE('friendsWith').each { relation ->
            relation.inV.each { friend ->
                println " - ${friend.name} for ${year-relation.since} year(s) since ${relation.since}"
            }
        }
        println " " // split output per person
    }
}

// shutdown neo4j properly
g.shutdown()