##---scripts para gerar os arquivos de teste-----
FILES=OUT-iscas_bench_aig_II/*.eqn
#FILES=tcc_ElisFM/*.eqn
for f in $FILES
do
	echo "Avaliando Xtores $f"
	##ARGUMENTOS PYTHON = "Eqn"
	python countXtores.py $f avaliacaoIscasK4
done
