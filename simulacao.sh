#scrip para testa de benchs do Aig
#FILES=iscas_bench_aig_II/*.aag
#for f in $FILES
#do
#	echo "Simulando FlexMap $f"
#	java -jar dist/FlexMap.jar $f -A 3 1.0 "OUT-"${f:0:lenght-4}"_Area.eqn" >> "OUT-"${f:0:lenght-4}"_AreaLOG.txt"
#done
FILES=aig_bench_iscas/*.aag
for f in $FILES
do
	echo "Simulando FlexMap $f"
	java -jar dist/FlexMap.jar $f -A 3 1.0 "OUT-"${f:0:lenght-4}"_Area.eqn" >>  "OUT-"${f:0:lenght-4}"_AreaLOG.txt"
	java -jar dist/FlexMap.jar $f -EQN "OUT-"${f:0:lenght-4}"_Eqn.eqn" >>  "OUT-"${f:0:lenght-4}"_EqnLOG.txt"
done
#for f in $FILES
#do#
#	echo "Simulando FlexMap $f"
#	java -jar dist/FlexMap.jar $f -EQN "OUT-"${f:0:lenght-3}"eqn" >> "OUT-"${f:0:lenght-3}"txt"
#done
#"OUT-"${f:0:lenght-4}"_areaFlowLibrary.eqn" >> "OUT-"${f:0:lenght-4}"_terminal__areaFlowLibrary.txt"
