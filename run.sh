#Script para testar todos os nodos de um circuito 
#!/bin/bash
for((i =13776 ; i>=2; i--,i--))
do 
	echo java -jar dist/FlexMap.jar iscas_bench_aig_II/cordic.aag -BFS $i;
	java -jar dist/FlexMap.jar iscas_bench_aig_II/cordic.aag -BFS $i >> logRun2.txt;
done
