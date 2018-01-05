NAME=entwurf

default dev all: distclean svg uml build/$(NAME).pdf

dev: CONT_FLAG = -pvc

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

show: build/$(NAME).pdf
	xdg-open $<

clean:
	rm -f *.tmp *.tui *.log *.tuc *.mp *.bbl *.blg *.fls *.idx *.aux *.out *.fdb_latexmk *.ilg *.ind
	rm -rf build

distclean:
	rm -f build/$(NAME).pdf
