PRES_NAME	:= abschluss

default: prep pres

dev: presdev

pres:	prep build/$(PRES_NAME).pdf

prep:	svg uml lst

presdev: CONT_FLAG = -pvc
presdev: latex-force prep pres

all: prep pres

svg: $(patsubst %.svg,build/%.pdf,$(wildcard images/*.svg))
uml: $(patsubst uml-source/%.plantuml,build/uml/%.png,$(wildcard uml-source/*.plantuml))
lst: $(patsubst %.tex,build/%.pdf,$(wildcard listings/*.tex))

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

build/listings/%.pdf: listings/%.tex *.sty
	mkdir -p build/listings
	latexmk -pdf -g -f -auxdir=build/listings -outdir=build/listings $<

clean:
	rm -f *.tmp *.tui *.log *.tuc *.mp *.bbl *.blg *.fls *.idx *.aux *.out *.fdb_latexmk *.ilg *.ind
	rm -rf build/listings
	rm -rf build

latex-force:
	rm -f build/$(PRES_NAME).pdf
