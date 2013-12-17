/**
 * Use the Graph database to store hmdb compounds
 *
 * Install: https://github.com/tinkerpop/gremlin/wiki/Getting-Started
 * Execute: gremlin -e example2.groovy
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
def neo4jLocation = './db/neo4j_example2'

// remove any old data
def neo4jDirectory = new File(neo4jLocation)
if (neo4jDirectory.isDirectory()){
    neo4jDirectory.eachFileRecurse { it.delete() }
}

// set Neo4j location
def g = new Neo4jGraph(neo4jLocation)

// parse XML to


// shutdown neo4j properly
g.shutdown()