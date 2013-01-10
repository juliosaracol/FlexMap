#scrip para testa de benchs do Aig
FILES=aig_bench_iscas/*.aag
for f in $FILES
do
	echo "Simulando FlexMap $f"
#	java -jar dist/FlexMap.jar $f -BFS >> logBFS.txt
#	java -jar dist/FlexMap.jar $f -BFS >> logBFS.txt
#	java -jar dist/FlexMap.jar $f -DFS >> logDFS.txt
#	java -jar dist/FlexMap.jar $f -A 3 1.0 "OUT-"${f:0:lenght-4}"_Area.eqn" >>  "OUT-"${f:0:lenght-4}"_AreaLOG.txt"
	java -jar dist/FlexMap.jar $f -EQN "OUT-"${f:0:lenght-4}"_Eqn.eqn" >>  "OUT-"${f:0:lenght-4}"_EqnLOG.txt"
#	java -jar dist/FlexMap.jar $f -T "OUT-"${f:0:lenght-4}"_TreeEqn.eqn" >>  "OUT-"${f:0:lenght-4}"_TreeLOG.txt"
	java -jar dist/FlexMap.jar $f -E 3 2 "OUT-"${f:0:lenght-4}"_ElisEqn.eqn" >>  "OUT-"${f:0:lenght-4}"_ElisLOG.txt"
done
FILES=iscas_bench_aig_II/*.aag
for f in $FILES
do
	echo "Simulando FlexMap $f"
#	java -jar dist/FlexMap.jar $f -BFS >> logBFS.txt
#	java -jar dist/FlexMap.jar $f -BFS >> logBFS.txt
#	java -jar dist/FlexMap.jar $f -DFS >> logDFS.txt
#	java -jar dist/FlexMap.jar $f -A 3 1.0 "OUT-"${f:0:lenght-4}"_Area.eqn" >>  "OUT-"${f:0:lenght-4}"_AreaLOG.txt"
	java -jar dist/FlexMap.jar $f -EQN "OUT-"${f:0:lenght-4}"_Eqn.eqn" >>  "OUT-"${f:0:lenght-4}"_EqnLOG.txt"
#	java -jar dist/FlexMap.jar $f -T "OUT-"${f:0:lenght-4}"_TreeEqn.eqn" >>  "OUT-"${f:0:lenght-4}"_TreeLOG.txt"
	java -jar dist/FlexMap.jar $f -E 3 2 "OUT-"${f:0:lenght-4}"_ElisEqn.eqn" >>  "OUT-"${f:0:lenght-4}"_ElisLOG.txt"
done
