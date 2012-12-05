#scrip para testa de benchs de saida do FlexMap
FILES=OUT-aig_bench_iscas/*Area.eqn
for f in $FILES
do
	echo "read_eqn ../NetBeansProjects/FlexMap/"$f  >> saidaCecAbcAreaAreaLibrary
        echo "short_names"  >> saidaCecAbcAreaAreaLibrary
	echo "write_eqn ../"${f:0:lenght-4}"Abc.eqn"  >> saidaCecAbcAreaAreaLibrary
	echo "read_eqn ../NetBeansProjects/FlexMap/"${f:0:lenght-8}"Eqn.eqn"  >> saidaCecAbcAreaAreaLibrary
	echo "short_names"  >> saidaCecAbcAreaAreaLibrary
	echo "write_eqn ../"${f:0:lenght-8}"EqnAbc.eqn"  >> saidaCecAbcAreaAreaLibrary
	echo "cec ../"${f:0:lenght-8}"EqnAbc.eqn ../"${f:0:lenght-4}"Abc.eqn" >> saidaCecAbcAreaAreaLibrary
        echo " " >> saidaCecAbcAreaAreaLibrary 
	echo " " >> saidaCecAbcAreaAreaLibrary
	echo " " >> saidaCecAbcAreaAreaLibrary	
done
#FILES=OUT-iscas_bench_aig_II/*.eqn
#for f in $FILES
#do
#	echo "cec -s -v ../NetBeansProjects/FlexMap/"${f:0:lenght-3}"area.eqn" "../NetBeansProjects/FlexMap/"$f >> saidaCecAbcAreaAreaLibrary
#done

#cd 
#cd abc
#rodando no abc 
#./abc -F ../NetBeansProjects/aig/saidaCecABS
