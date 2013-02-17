##---scripts para gerar os arquivos de teste-----
#FILES=iscas_bench_aig_II/*.aag
FILES=tcc_ElisFM/*.eqn
for f in $FILES
do
	echo "Avaliando Xtores $f"
	##ARGUMENTOS PYTHON = "Eqn"
	python countXtores.py $f >> avaliacao.txt
done
