##Universidade Federal de Pelotas
##Julio Saracol 

import sys
import random;
import string;

##-------carrega os parametros definidos no shell-script countXtores.sh
parametros = [];
for param in sys.argv :
    parametros.append(param);
arquivoIn 	= parametros[1]; 		#------nome do arquivo de entradas
##----------------------------------------------------------------------

#-------------------conta entradas da linha-----------------------------
def contaEntradas(token):
 i=0;
 contador=0;
 listaEntradas=[];
 token2 = "";
 token2 = token[1];
 token2 = token2.replace("["," ");
 token2 = token2.replace("]"," ");
 token2 = token2.replace("("," ");
 token2 = token2.replace(")"," ");
 token2 = token2.replace("*"," ");
 token2 = token2.replace("+"," ");
 token2 = token2.replace(";"," ");
 token2 = token2.replace("\n"," ");
 token3 = string.split(token2," ");
 while(i < len(token3)): 
	if(token3[i] != ''):
		if(token3[i] == "!"):
			contador= contador+1;
		else:
#			if(listaEntradas.count(token3[i]) == 0)and(token3[i].find("pi")==-1):
			if(token3[i].find("pi")==-1):
				contador= contador+1;
				listaEntradas.append(token3[i]);
	i=i+1;
 return contador*2;
#------------------------------------------------

conteudo = open(arquivoIn,"r");
listaLinhas = [];
while True:
		numero = conteudo.readline();
		if len(numero) > 0:
			token = [];
			token    = string.split(numero,'=');   # quebra a string em uma lista
			if(token[0] != "\n")and(token[0].find('#'))and(token[0].find("INORDER"))and(token[0].find("OUTORDER"))and(token[0].find("pi"))and(token[0].find("po"))and(token[0].find("[0]"))and(token[0].find("[1]")):
				listaLinhas.append(token);
	#			print token;
		else:
			break;
j=0;
cost = [];
while True:
	entradas = 0;
	if len(listaLinhas) > j:
		costFinal = contaEntradas(listaLinhas[j]);
		cost.append(costFinal);
		#print str(listaLinhas[j][1]) +"com o custo " + str(costFinal);
		j = j+1;
	else:
		break;
conteudo.close();

#------------------------------------grava os resultados num arquivo xls
arquivoResult = open('resultados/'+arquivoIn.replace(".","")+'Resultados'+'.xls','w');
i=0;
arquivoResult.write("celula;custo em transistores;\n");
while i < len(listaLinhas):
	#print str(listaLinhas[i][1].replace("\n",""))+str(cost[i])+';';
	arquivoResult.write(str(listaLinhas[i][1].replace("\n",""))+str(cost[i])+';\n');
	i=i+1;
arquivoResult.close();
