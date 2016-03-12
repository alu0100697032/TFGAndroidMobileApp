LATEX    = latex
BIBTEX   = bibtex
PDFLATEX = pdflatex
L2HTML   = latex2html
DVIPS    = dvips
BASURA   = *.dvi *.bbl *.aux *.toc *.lof *.lot *.lol *.log *.blg *.out *~ *.pdf *.ps *.backup *.pdf.old

MASTER   = memoria-TFG.tex
SRC      = \
						Prologo.tex 			     \
						Cap1_objetives.tex                  \
						Cap2_HerrSoft.tex                    \
						Cap3_DescApp.tex                     \
						Cap4_Desarrollo.tex                  \
						Cap5_Despliegue.tex		     \
						Cap6_todo.tex                        \
						Cap7_SummaryClonclusions.tex         \
						Cap8_Presupuesto.tex

DVI = $(MASTER:.tex=.dvi)
BIB = $(MASTER:.tex=.bib)
PS  = $(MASTER:.tex=.ps)
PDF = $(MASTER:.tex=.pdf)
MASTERSIN =  $(MASTER:.tex=)

all: 
	touch memoria-TFG.`date +%Y-%m-%d`.pdf
	$(MAKE) pdf
	mv memoria-TFG.`date +%Y-%m-%d`.pdf memoria-TFG.`date +%Y-%m-%d`.pdf.old
	cp memoria-TFG.pdf memoria-TFG.`date +%Y-%m-%d`.pdf

HTML: $(PDF)
	$(L2HTML) -dir HTML -split 4 -local_icons -long_titles 20 $(MASTER)

pdf: $(MASTER) $(SRC)
	$(PDFLATEX) $(MASTER) && $(MAKE) bib && $(PDFLATEX) $(MASTER) && $(PDFLATEX) $(MASTER)

ps: $(MASTER) $(SRC) $(DVI)
	$(DVIPS) -o $(PS) $(DVI)

bib: bibliography.bib
	$(BIBTEX) $(MASTERSIN)

$(DVI): $(MASTER) $(SRC)
	$(LATEX) $(MASTER); $(MAKE) bib; $(LATEX) $(MASTER); $(LATEX) $(MASTER)   

clean:
	$(RM) *.pdf *.old *.dvi *.bbl *.aux *.toc *.lof *.lot *.log *.blg *.lol *.brf *~ *.out HTML/*

del:
	$(RM) -R $(BASURA);
	cd bibliografia;    $(RM) -R $(BASURA);  \
	cd ../capitulos;    $(RM) -R $(BASURA);  \
	cd ../previo;       $(RM) -R $(BASURA);  \

mrproper:
	$(MAKE) delete

