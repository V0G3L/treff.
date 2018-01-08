DOC_NAME=entwurf
PRES_NAME=entwurf-presentation

dev: 	latex-force
dev: 	CONT_FLAG = -pvc
doc:	build/$(DOC_NAME).pdf
pres:	build/$(PRES_NAME).pdf

default: svg uml pres
all: svg uml doc pres
dev: svg uml

svg: $(patsubst %.svg,build/%.pdf,$(wildcard images/*.svg))

uml: $(patsubst uml-source/%.plantuml,build/uml/%.png,$(wildcard uml-source/*.plantuml))

build/images/%.pdf: images/%.svg
	mkdir -p build/images
	inkscape -D -z --file=$< --export-pdf=$@ --export-latex

build/uml/%.png: uml-source/%.plantuml
	mkdir -p build/uml
	plantuml $<
	mv $(patsubst build/uml/%.png,uml-source/%.png,$@) $@

build/%.pdf: %.tex *.sty
	mkdir -p build
	latexmk $(CONT_FLAG) -pdf -g -f -auxdir=build -outdir=build $<

clean:
	rm -f *.tmp *.tui *.log *.tuc *.mp *.bbl *.blg *.fls *.idx *.aux *.out *.fdb_latexmk *.ilg *.ind
	rm -rf build

latex-force:
	rm -f build/$(DOC_NAME).pdf
	rm -f build/$(PRES_NAME).pdf
