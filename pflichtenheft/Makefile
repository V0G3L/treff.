NAME=pflichtenheft

.PHONY: show clean distclean dev

default dev all: distclean build/$(NAME).pdf

dev: CONT_FLAG = -pvc

build/%.pdf: %.tex *.sty
	mkdir -p build
	latexmk $(CONT_FLAG) -pdf -g -f -auxdir=build -outdir=build $<

show: build/$(NAME).pdf
	xdg-open $<

clean:
	rm -f *.tmp *.tui *.log *.tuc *.mp *.bbl *.blg *.fls *.idx *.aux *.out *.fdb_latexmk *.ilg *.ind
	rm -rf build

distclean: clean
	rm -f build/$(NAME).pdf
