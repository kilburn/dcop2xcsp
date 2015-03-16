# USC-DCOP to XCSP problem converter

This project converts problems between the [USC DCOP Repository format](http://teamcore.usc.edu/dcop/) and the [Frodo2 XCSP format](http://thomas.leaute.name/frodo/manual/FRODO_User_Manual.html).

This is useful to test algorithms on the same problems, even if they are implemented in different solvers.

## Building

You need to have [Apache Ant](http://ant.apache.org/) installed. Then, you can build the project by simply running:
```
ant jar
```

This will create a `dist/dcop2xcsp.jar` file that contains the problem transformer.

## Usage

This is a very simple converter. To run it, just execute the binary `jar` file with the path to the problem to convert. You will get the converted problem in `stdout`. Example:

Given a USC-formatted problem:
```
$ cat /tmp/problem.fg 
VARIABLE x 2 2
VARIABLE y 2 2
VARIABLE z 2 2
VARIABLE t 2 2
VARIABLE u 2 2
VARIABLE v 2 2
CONSTRAINT x y
F 0 0 20
F 0 1 10
F 1 0 10
F 1 1 0
CONSTRAINT y t
F 0 0 0
F 0 1 4
F 1 0 4
F 1 1 12
CONSTRAINT z t
F 0 0 14
F 0 1 10
F 1 0 14
F 1 1 10
CONSTRAINT t u
F 0 0 3
F 0 1 2
F 1 0 2
F 1 1 0
CONSTRAINT z v
F 0 0 3
F 0 1 2
F 1 0 2
F 1 1 0
CONSTRAINT u v
F 0 0 0
F 0 1 10
F 1 0 10
F 1 1 0
```

We convert it to XCSP format by running:
```
$ java -jar dist/dcop2xcsp.jar /tmp/problem.fg 
<instance><presentation name="Undefined" maxConstraintArity="2" maximize="false" format="XCSP 2.1_FRODO" />
<agents nbAgents="6">
	<agent name="Agent0" />
	<agent name="Agent1" />
	<agent name="Agent2" />
	<agent name="Agent3" />
	<agent name="Agent4" />
	<agent name="Agent5" />
</agents>
<domains nbDomains="6">
	<domain name="Domain0" nbValues="2">0..1</domain>
	<domain name="Domain1" nbValues="2">0..1</domain>
	<domain name="Domain2" nbValues="2">0..1</domain>
	<domain name="Domain3" nbValues="2">0..1</domain>
	<domain name="Domain4" nbValues="2">0..1</domain>
	<domain name="Domain5" nbValues="2">0..1</domain>
</domains>
<variables nbVariables="6">
	<variable name="Variable0" domain="Domain0" agent="Agent0" />
	<variable name="Variable1" domain="Domain1" agent="Agent1" />
	<variable name="Variable2" domain="Domain2" agent="Agent2" />
	<variable name="Variable3" domain="Domain3" agent="Agent3" />
	<variable name="Variable4" domain="Domain4" agent="Agent4" />
	<variable name="Variable5" domain="Domain5" agent="Agent5" />
</variables>
<relations nbRelations="6">
	<relation name="RelationF(x,y)" arity="2" nbTuples="4" semantics="soft" defaultCost="0.0">
		20.0: 0 0 | 10.0: 0 1 | 10.0: 1 0 | 0.0: 1 1
	</relation>
	<relation name="RelationF(y,t)" arity="2" nbTuples="4" semantics="soft" defaultCost="0.0">
		0.0: 0 0 | 4.0: 0 1 | 4.0: 1 0 | 12.0: 1 1
	</relation>
	<relation name="RelationF(z,t)" arity="2" nbTuples="4" semantics="soft" defaultCost="0.0">
		14.0: 0 0 | 10.0: 0 1 | 14.0: 1 0 | 10.0: 1 1
	</relation>
	<relation name="RelationF(t,u)" arity="2" nbTuples="4" semantics="soft" defaultCost="0.0">
		3.0: 0 0 | 2.0: 0 1 | 2.0: 1 0 | 0.0: 1 1
	</relation>
	<relation name="RelationF(z,v)" arity="2" nbTuples="4" semantics="soft" defaultCost="0.0">
		3.0: 0 0 | 2.0: 0 1 | 2.0: 1 0 | 0.0: 1 1
	</relation>
	<relation name="RelationF(u,v)" arity="2" nbTuples="4" semantics="soft" defaultCost="0.0">
		0.0: 0 0 | 10.0: 0 1 | 10.0: 1 0 | 0.0: 1 1
	</relation>
</relations>
<constraints nbConstraints="6">
	<constraint name="F(x,y)" arity="2" scope="Variable0 Variable1" reference="RelationF(x,y)" />
	<constraint name="F(y,t)" arity="2" scope="Variable1 Variable3" reference="RelationF(y,t)" />
	<constraint name="F(z,t)" arity="2" scope="Variable2 Variable3" reference="RelationF(z,t)" />
	<constraint name="F(t,u)" arity="2" scope="Variable3 Variable4" reference="RelationF(t,u)" />
	<constraint name="F(z,v)" arity="2" scope="Variable2 Variable5" reference="RelationF(z,v)" />
	<constraint name="F(u,v)" arity="2" scope="Variable4 Variable5" reference="RelationF(u,v)" />
</constraints>
</instance>
```
